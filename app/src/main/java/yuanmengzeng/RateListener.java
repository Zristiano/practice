package com.yahoo.ads.pb.platform.control.druid.ratelimiter;

public interface RateListener {
	void onChange(double oldRate, double newRate);
}
