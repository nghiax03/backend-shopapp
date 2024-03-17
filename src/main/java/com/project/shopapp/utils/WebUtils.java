package com.project.shopapp.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public class WebUtils {
public static HttpServletRequest  getCurrentRequest() {
	return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
}
}
