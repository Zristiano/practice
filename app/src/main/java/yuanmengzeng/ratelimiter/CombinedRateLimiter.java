//package yuanmengzeng.ratelimiter;
//
//import com.google.common.util.concurrent.Uninterruptibles;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.HashSet;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicLong;
//
///**
// * This class is used to acquire token at a rate defined in {@link CombinedRateLimiter#sharedDistributedRateLimiter}.
// * Thread calling {@link CombinedRateLimiter#acquirePermit()} will be blocked until a token is delivered to it to resume.
// * Every time this class get {@link CombinedRateLimiter#tokenBunch} tokens from remote, it will deliver those tokens at the same rate of {@link CombinedRateLimiter#sharedDistributedRateLimiter}.<br><br>
// * For example, Assuming the distributed rate is 20 TPS (token per second), {@link CombinedRateLimiter#tokenBunch} is 4, at 10:00:00,000 am this class gets
// * {@link CombinedRateLimiter#tokenBunch} tokens, then it will use one token every 50 millisecond, that is, it will wake up one waiting thread at 10:00:00,000 am,
// * 10:00:00,050 am, 10:00:00,100 am and 10:00:00,050 am respectively
// */
//public class CombinedRateLimiter {
//	private static final Logger logger = LoggerFactory.getLogger(CombinedRateLimiter.class);
//	private final static int MAX_RETRY = 3;
//	private final Object monitor = new Object();
//	private final double tokenBunch;
//	private SharedDistributedRateLimiter sharedDistributedRateLimiter;
//	private AtomicLong tokenRequestCount = new AtomicLong(0);
//	private volatile boolean isFetchingDeliveringToken;
//	private long tokenRequestId = 0;
//	private Set<Long> tokenRequestIds = new HashSet<>();
//
//
//	/**
//	 * @param tokenBunch                   number of tokens acquiring from DistributedRateLimiter per request
//	 * @param sharedDistributedRateLimiter sharedDistributedRateLimiter
//	 */
//	public CombinedRateLimiter(double tokenBunch, SharedDistributedRateLimiter sharedDistributedRateLimiter) {
//		this.tokenBunch = tokenBunch;
//		this.sharedDistributedRateLimiter = sharedDistributedRateLimiter;
//	}
//
//	/**
//	 * This method must be called at least once after creating the instance of this class
//	 * if the following procedure wants to use that instance. It informs {@link CombinedRateLimiter#sharedDistributedRateLimiter}
//	 * to increase the reference count
//	 */
//	public synchronized void prepareRateLimiter() {
//		sharedDistributedRateLimiter.prepareRateLimiter();
//	}
//
//
//	/**
//	 * This method must be called at least once if stopping using this class. <br>
//	 * Calling this method does not mean the nested distributed rate limiter will shutdown immediately,
//	 * it just informs {@link CombinedRateLimiter#sharedDistributedRateLimiter} to decrease the reference count
//	 */
//	public synchronized void close() {
//		sharedDistributedRateLimiter.close();
//	}
//
//	public boolean acquirePermit() {
//		synchronized (monitor) {
//			tokenRequestCount.incrementAndGet();
//			long requestId = ++tokenRequestId;
//			tokenRequestIds.add(requestId);
//			logger.info(" enter acquirePermit tokenRequestCount:{}, requestId:{}", tokenRequestCount, requestId);
//			if (!isFetchingDeliveringToken) {
//				startNewFetcherDelivererThread();
//			}
//			try {
//				monitor.wait();
//				boolean success = tokenRequestIds.remove(requestId);
//				logger.debug("acquirePermit request return, tokenRequestCount:{}, success:{}", tokenRequestCount, success);
//				return success;
//			} catch (Exception e) {
//				logger.error("acquirePermit monitor.wait exception: ", e);
//				return false;
//			}
//		}
//	}
//
//	private void startNewFetcherDelivererThread() {
//		isFetchingDeliveringToken = true;
//		new Thread(new FetcherDeliverer()).start();
//	}
//
//	/**
//	 * This thread will fetch {@link CombinedRateLimiter#tokenBunch} tokens from remote,
//	 * and then deliver them at a constant rate.
//	 */
//	private class FetcherDeliverer implements Runnable {
//		@Override
//		public void run() {
//			logger.info("start FetcherDelivererThread tokenRequestCount:{}", tokenRequestCount.get());
//			// fetching tokens from distributed rate limiter, thread will be suspended until it is done.
//			boolean success = fetchToken(tokenBunch);
//			if (success) {
//				// if succeed to get required tokens,
//				// it will deliver them at the same rate as distributed rate limiter
//				deliverToken();
//				checkTokenRequest();
//			} else {
//				// if it fails to get token from remote, we notify all the threads waiting for the token
//				notifyFailure();
//			}
//			logger.info("exit FetcherDelivererThread, fetchToken success:{}", success);
//		}
//
//		private boolean fetchToken(double requiredToken) {
//			for (int i = 0; i < MAX_RETRY; i++) {
//				boolean success = sharedDistributedRateLimiter.acquireToken(requiredToken);
//				logger.info("finish fetching token from remote, requiredToken:{}, success:{}, retry:{}", requiredToken, success, i);
//				if (success) return true;
//			}
//			return false;
//		}
//
//
//		/**
//		 * notify all the waiting threads that they fail to acquire permit from this class
//		 */
//		private void notifyFailure() {
//			synchronized (monitor) {
//				// clear the request id set.
//				// Once the waiting thread wake up, it will checkTokenRequest the set to determine if it get the permit.
//				tokenRequestIds.clear();
//				monitor.notifyAll();
//				isFetchingDeliveringToken = false;
//			}
//		}
//
//		private void deliverToken() {
//			long startTime = System.currentTimeMillis();
//			double interval = 1000 / sharedDistributedRateLimiter.getRate();
//			for (int i = 0; i < tokenBunch; i++) {
//				long fireTime = (long) (startTime + i * interval);
//				long waitTime = fireTime - System.currentTimeMillis();
//				Uninterruptibles.sleepUninterruptibly(waitTime, TimeUnit.MILLISECONDS);
//				logger.info("prepare to deliver token : {},  waitTime:{}", i, waitTime);
//				synchronized (monitor) {
//					monitor.notify();
//					if (tokenRequestCount.get() > 0) {
//						tokenRequestCount.decrementAndGet();
//					}
//				}
//			}
//		}
//
//		private void checkTokenRequest() {
//			synchronized (monitor) {
//				if (tokenRequestCount.get() > 0) {
//					// if there are still some token requests, we fetch tokens from distributed rate limiter again
//					startNewFetcherDelivererThread();
//				} else {
//					isFetchingDeliveringToken = false;
//				}
//			}
//		}
//	}
//}
