package com.yahoo.ads.pb.platform.control.druid.ratelimiter;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.util.concurrent.Uninterruptibles;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * This class serves as distributed rate limiter. It helps restrict the overall frequency of some procedure on all servers to certain rate
 */
public class DistributedRateLimiter {

	private static final Logger logger = LoggerFactory.getLogger(DistributedRateLimiter.class);

	private static final String BASE = "/distributed_rate_limiter";

	private boolean connected;

	private boolean initialized;

	private InterProcessMutex mutex;

	private CuratorFramework client;

	private final String dataNode;

	private final String lockNode;

	// identifier for the local object of DistributedRateLimiter
	private final String identifier;

	private final Locator locator;

	private RateListener rateListener;

	/**
	 * This class describes a distributed rate limiter on ZooKeeper and is used to identify the rate limiter.
	 */
	public static class Locator {

		// name of the distributed rate limiter. it will be part of the path
		private final String name;

		// number of tokens produced per second
		private double tokenPerSecond;

		// the rate of this RateLimiter cannot be changed during @param rateUnchangeableInterval since the last rate changing
		// the unit is millisecond
		private final double rateUnchangeableInterval;

		private final String connectString;

		/**
		 * create a locator identifying a distributed rate limiter residing in ZooKeeper
		 *
		 * @param connectString            address set that is used to connect to ZooKeeper
		 * @param name                     name of the distributed rate limiter
		 * @param tokenPerSecond           number of tokens produced per second, in other words, the max rate allowed by the distributed rate limiter
		 * @param rateUnchangeableInterval time interval during which the rate of distributed rate limiter is unchangeable since the last rate changing (unit: millisecond)
		 * @return locator
		 */
		public static Locator create(String connectString, String name, double tokenPerSecond, double rateUnchangeableInterval) {
			Locator locator = new Locator(connectString, name, rateUnchangeableInterval);
			locator.tokenPerSecond = tokenPerSecond;
			return locator;
		}

		private Locator(String connectString, String name, double rateUnchangeableInterval) {
			this.name = name;
			this.connectString = connectString;
			this.rateUnchangeableInterval = rateUnchangeableInterval;
		}

		public boolean isSame(Locator locator) {
			if (locator == null) return false;
			if (locator.equals(this)) return true;
			return locator.name.equals(name) && locator.tokenPerSecond == tokenPerSecond && locator.rateUnchangeableInterval == rateUnchangeableInterval;
		}

		public double getRate() {
			return tokenPerSecond;
		}
	}

	/**
	 * @param identifier identifying the object of class DistributedRateLimiter
	 * @param locator    locating a distributed rate limiter on ZooKeeper
	 */
	public DistributedRateLimiter(String identifier, Locator locator) {
		this.identifier = identifier;
		dataNode = BASE + File.separator + "rate_" + locator.name;
		lockNode = BASE + File.separator + "lock_" + locator.name;
		this.locator = locator;
		initClient();
	}

	private void initClient() {

		client = makeClient(locator.connectString);
		client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
			@Override
			public void stateChanged(CuratorFramework client, ConnectionState newState) {
				if (!client.equals(DistributedRateLimiter.this.client)) return;
				logger.info("DistributedRateLimiter:: {} client state changes, RateLimiter's name:{}, newState:{}", identifier, locator.name, newState.name());
				switch (newState) {
					case LOST:
						connected = false;
						break;
					case CONNECTED:
					case RECONNECTED:
						connected = true;
				}
			}
		});
		client.start();
		mutex = new InterProcessMutex(client, lockNode);
		boolean lockAcquired = false;
		try {
			lockAcquired = mutex.acquire(1, TimeUnit.MINUTES);
			if (!lockAcquired) {
				initialized = false;
				logger.error("DistributedRateLimiter:: {} fails to acquire lock at initialization, RateLimiter's name:{}", identifier, locator.name);
				return;
			}
			if (client.checkExists().forPath(dataNode) == null) {
				RateInfo rateInfo = makeDefaultRateInfo();
				String path = client.create().withMode(CreateMode.PERSISTENT).forPath(dataNode, RateInfo.toByte(rateInfo));
				logger.info("DistributedRateLimiter:: {} create rate node : {}, RateInfo:{}, RateLimiter's name:{}", identifier, path, rateInfo, locator.name);
			} else {
				byte[] data = client.getData().forPath(dataNode);
				RateInfo rateInfo = RateInfo.fromByte(data);
				logger.info("DistributedRateLimiter:: {} exist rate node : {}, RateInfo:{}, RateLimiter's name:{}", identifier, dataNode, rateInfo, locator.name);
				if (rateInfo == null || rateInfo.version != RateInfo.CURRENT_VERSION) {
					logger.info("DistributedRateLimiter:: {} version changes, new version:{}, old version:{}, RateLimiter's name:{}",
							identifier, RateInfo.CURRENT_VERSION, rateInfo == null ? 0 : rateInfo.version, locator.name);
					client.setData().forPath(dataNode, RateInfo.toByte(makeDefaultRateInfo()));
				} else if (System.currentTimeMillis() - rateInfo.latestTpsMicros > locator.rateUnchangeableInterval) {
					long rateUnchangedDuration = System.currentTimeMillis() - rateInfo.latestTpsMicros;
					client.setData().forPath(dataNode, RateInfo.toByte(makeDefaultRateInfo()));
					logger.info("DistributedRateLimiter:: {} reset rate, new rate:{}, rateUnchangeableInterval:{} millisecond, rateUnchangedDuration:{} millisecond, RateLimiter's name:{}",
							identifier, locator.tokenPerSecond, locator.rateUnchangeableInterval, rateUnchangedDuration, locator.name);
				}
			}
			initialized = true;
		} catch (Exception e) {
			initialized = false;
			logger.error("DistributedRateLimiter:: {} initialize connection to zookeeper fails, RateLimiter's name:{}, reason:", identifier, locator.name, e);
		} finally {
			if (lockAcquired) {
				try {
					mutex.release();
				} catch (Exception e) {
					logger.error("DistributedRateLimiter:: {} release lock fails after initialization, RateLimiter's name:{}, reason", identifier, locator.name, e);
				}
			}
		}
	}

	/**
	 * request token from this rate limiter
	 *
	 * @param requiredToken number of token
	 * @return true/false
	 */
	public boolean acquireToken(double requiredToken) {
		if (!initialized || !connected) return false;
		boolean lockAcquired = false;
		try {
			lockAcquired = mutex.acquire(1, TimeUnit.MINUTES);
			if (lockAcquired) {
				return getToken(requiredToken);
			} else {
				logger.error("DistributedRateLimiter:: {} fails to acquire lock at initialization, RateLimiter's name:{}", identifier, locator.name);
			}
		} catch (Exception e) {
			logger.error("DistributedRateLimiter:: {} acquire token fails, RateLimiter's name:{}, reason:", identifier, locator.name, e);
		} finally {
			if (lockAcquired) {
				try {
					mutex.release();
				} catch (Exception e) {
					logger.error("DistributedRateLimiter:: {} release lock fails after acquireToken, RateLimiter's name:{}, reason:", identifier, locator.name, e);
				}
			}
		}
		return false;
	}

	private boolean getToken(double requiredToken) throws Exception {
		if (!connected) return false;
		byte[] data = client.getData().forPath(dataNode);
		RateInfo rateInfo = RateInfo.fromByte(data);
		logger.info("DistributedRateLimiter:: {} getToken starts processing, RateInfo:{}, RateLimiter's name:{}", identifier, rateInfo, locator.name);
		long curTime = System.currentTimeMillis();
		if (rateInfo == null || rateInfo.version != RateInfo.CURRENT_VERSION) {
			logger.info("DistributedRateLimiter:: {} getToken fails because rateInfo is null or its version is inconsistent with current version , RateLimiter's name:{}", identifier, locator.name);
			return false;
		}
		syncUpLocal(rateInfo);
		double timeInterval = 1000.0 / locator.tokenPerSecond;
		if (curTime >= rateInfo.nextFreeTicketMicros) {
			// we use stored token only produced during the past 0.2 second
			double availableToken = Math.min(locator.tokenPerSecond / 5, rateInfo.storedToken + (curTime - rateInfo.nextFreeTicketMicros) / timeInterval);
			double remainedToken = availableToken - requiredToken;
			if (remainedToken >= 0) {
				rateInfo.nextFreeTicketMicros = curTime;
				rateInfo.storedToken = remainedToken;
			} else {
				rateInfo.nextFreeTicketMicros = curTime + (long) ((-remainedToken) * timeInterval);
				rateInfo.storedToken = 0;
			}
		} else {
			long waitTime = rateInfo.nextFreeTicketMicros - curTime;
			Uninterruptibles.sleepUninterruptibly(waitTime, TimeUnit.MILLISECONDS);
			rateInfo.nextFreeTicketMicros = System.currentTimeMillis() + (long) (requiredToken * timeInterval);
			rateInfo.storedToken = 0;
		}
		client.setData().forPath(dataNode, RateInfo.toByte(rateInfo));
		logger.info("DistributedRateLimiter:: {} getToken succeeds, RateInfo:{}, RateLimiter's name:{}", identifier, rateInfo, locator.name);
		return true;
	}

	/**
	 * set a new rate (token per second) for the distributed rate limiter
	 * @param tokenPerSecond new rate
	 * @param ignoreUnchangeableInterval if this procedure considers {@link Locator#rateUnchangeableInterval} when setting new rate
	 */
	public void setRate(double tokenPerSecond, boolean ignoreUnchangeableInterval) {
		if (!initialized || !connected) return;
		boolean lockAcquired = false;
		try {
			lockAcquired = mutex.acquire(1, TimeUnit.MINUTES);
			if (!lockAcquired) {
				logger.error("DistributedRateLimiter:: {} fails to acquire lock at initialization, RateLimiter's name:{}", identifier, locator.name);
				return;
			}
			RateInfo rateInfo = RateInfo.fromByte(client.getData().forPath(dataNode));
			if (rateInfo == null || rateInfo.version != RateInfo.CURRENT_VERSION) {
				rateInfo = makeDefaultRateInfo();
			}
			if (ignoreUnchangeableInterval
					|| System.currentTimeMillis() - rateInfo.latestTpsMicros > locator.rateUnchangeableInterval) {
				updateWithNewRate(rateInfo, tokenPerSecond);
				client.setData().forPath(dataNode, RateInfo.toByte(rateInfo));
				syncUpLocal(rateInfo);
				logger.info("DistributedRateLimiter:: {} setting rate successes, new rate:{}, RateLimiter's name:{}", identifier, tokenPerSecond, locator.name);
			}
		} catch (Exception e) {
			logger.error("DistributedRateLimiter:: {} set rate fails, RateLimiter's name:{}, reason:", identifier, locator.name, e);
		} finally {
			if (lockAcquired) {
				try {
					mutex.release();
				} catch (Exception e) {
					logger.error("DistributedRateLimiter:: {} release lock fails after setting rate, RateLimiter's name:{}, reason:", identifier, locator.name, e);
				}
			}
		}
	}

	public void setRateListener(@Nonnull RateListener rateListener) {
		this.rateListener = rateListener;
	}

	private RateInfo makeDefaultRateInfo() {
		RateInfo rateInfo = new RateInfo();
		rateInfo.version = RateInfo.CURRENT_VERSION;
		rateInfo.storedToken = 0;
		rateInfo.tokenPerSecond = locator.tokenPerSecond;
		rateInfo.nextFreeTicketMicros = System.currentTimeMillis();
		rateInfo.latestTpsMicros = System.currentTimeMillis();
		return rateInfo;
	}

	private void updateWithNewRate(RateInfo rateInfo, double newRate) throws Exception {
		double oldTokenPerSecond = rateInfo.tokenPerSecond;
		double oldStoredToken = rateInfo.storedToken;
		if (oldTokenPerSecond != 0.0) {
			rateInfo.storedToken = newRate / oldTokenPerSecond * oldStoredToken;
		}
		rateInfo.tokenPerSecond = newRate;
		rateInfo.latestTpsMicros = System.currentTimeMillis();
	}

	private void syncUpLocal(RateInfo rateInfo) {
		double oldRate = locator.tokenPerSecond;
		locator.tokenPerSecond = rateInfo.tokenPerSecond;
		if (oldRate != rateInfo.tokenPerSecond && rateListener != null) {
			rateListener.onChange(oldRate, locator.tokenPerSecond);
		}
	}

	@VisibleForTesting
	CuratorFramework makeClient(String connectString) {
		return CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
	}

	/**
	 * Closing connection to ZooKeeper server.<br>
	 * Calling this method must be the last operation on the class.
	 */
	public void close() {
		CloseableUtils.closeQuietly(client);
	}
}
