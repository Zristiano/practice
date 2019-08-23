package com.yahoo.ads.pb.platform.control.druid.ratelimiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * A wrap class of {@link DistributedRateLimiter} that helps retain shared ownership of an instance of {@link DistributedRateLimiter}<br>
 * This class provides wrapper methods for {@link DistributedRateLimiter}'s public methods
 */
public class SharedDistributedRateLimiter implements RateListener {
	private static final Logger logger = LoggerFactory.getLogger(SharedDistributedRateLimiter.class);

	private final String identifier;
	private final DistributedRateLimiter.Locator locator;
	private final List<RateListener> rateListeners = new LinkedList<>();
	private DistributedRateLimiter rateLimiter;
	private int rateLimiterRefCount;

	public SharedDistributedRateLimiter(String identifier, DistributedRateLimiter.Locator locator) {
		this.identifier = identifier;
		this.locator = locator;
	}

	/**
	 * This method must be called at least once after creating the instance of this class
	 * if the following procedure wants to use that instance
	 */
	public synchronized void prepareRateLimiter() {
		if (rateLimiter == null) {
			rateLimiter = new DistributedRateLimiter(identifier, locator);
			rateLimiter.setRateListener(this);
		}
		rateLimiterRefCount++;
	}

	public synchronized void close() {
		logger.info("attempt to close the distributed rate limiter");
		if (rateLimiterRefCount > 0) {
			if (--rateLimiterRefCount == 0) {
				logger.info("succeed to close the distributed rate limiter");
				rateLimiter.close();
				rateLimiter = null;
			}
		}
	}

	public void addRateListener(RateListener rateListener) {
		synchronized (rateListeners) {
			rateListeners.add(rateListener);
		}
	}

	public void setRate(double tokenPerSecond, boolean ignoreUnchangeableInterval) {
		if (rateLimiter == null) {
			throw new IllegalStateException("rate limiter is not prepared");
		}
		rateLimiter.setRate(tokenPerSecond, ignoreUnchangeableInterval);
	}

	public boolean acquireToken(double requiredToken) {
		if (rateLimiter == null) {
			throw new IllegalStateException("rate limiter is not prepared");
		}
		return rateLimiter.acquireToken(requiredToken);
	}

	public double getRate() {
		return locator.getRate();
	}

	@Override
	public void onChange(double oldRate, double newRate) {
		synchronized (rateListeners) {
			for (RateListener l : rateListeners) {
				l.onChange(oldRate, newRate);
			}
		}
	}
}
