package com.intuit.employee.service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class MetricsService {
	private final ConcurrentHashMap<Integer, Integer> statusMetric;

	public MetricsService() {
		statusMetric = new ConcurrentHashMap<Integer, Integer>();
	}

	/*
	 * Store the metrics of HTTP Status Codes on the server
	 * on the map
	 */
	public synchronized  void updateCount(String request, int status) {
		Integer statusCount = statusMetric.get(status);
		if (statusCount == null) {
			statusMetric.put(status, 1);
		} else {
			statusMetric.put(status, statusCount + 1);
		}
	}

	/*
	 * Returns unmodifiedMap collections
	 */
	public Map<Integer, Integer> getStatusMetricMap() {
		return Collections.unmodifiableMap(statusMetric);
	}
}
