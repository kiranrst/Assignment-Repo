package com.intuit.employee.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.annotations.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.intuit.employee.service.MetricsService;

import lombok.extern.slf4j.Slf4j;


@Component
@Order(1)	
@Slf4j
public class GlobalApplicationFilter extends OncePerRequestFilter {

	@Autowired
	MetricsService metricService;

	/*
	 * Perform the filter operations and updating the metrics of the application
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		String req = httpRequest.getMethod() + " - " + httpRequest.getRequestURI();
		log.debug("Processing Request URI : " + req);
		if(!shouldNotFilter(httpRequest)) {
			filterChain.doFilter(request, response);
			int status = response.getStatus();
			metricService.updateCount(req, status);
		}
		log.debug("Response for URI : {} , status: {} ",req, response.getStatus());
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		if(request.getRequestURI().equals(("/api/employee/viewMetrics"))) {
			return true;
		}
		return false;
	}

}