package com.base.util;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class PropertiesUtil {

	@Resource
	private Environment environmentAutowired;

	private static Environment environment;

	@PostConstruct
	public void init() {
		environment = environmentAutowired;
	}

	public static String getProperty(String key) {
		return environment.getProperty(key);
	}
}