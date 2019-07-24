package com.base.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisService {
    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);
    
	@Autowired
    private RedisTemplate<Object,Object> redisTemplate; 
	
	
	
	public void set(String key, Object value) {
		
		redisTemplate.opsForValue().set(key, value);
	}
	public void set(String key, Object value, long validTime) {
		set(key, value);
		refreshTime(key, validTime);
	}
	
	public void refreshTime(String key, long validTime) {
		redisTemplate.expire(key, validTime, TimeUnit.SECONDS);
	}
	public boolean exists(String key) {
    	return redisTemplate.hasKey(key);
    }
	
	public Object get(String key) {
	    return redisTemplate.opsForValue().get(key);
	}
	public void delete(String key) {
		redisTemplate.delete(key);
	}
	public void batchDel(String pattern) {
		redisTemplate.delete(redisTemplate.keys(pattern + "*"));
	}
	public long incr(String key, long delta, long validTime) {
		long count = redisTemplate.opsForValue().increment(key, delta);
		if (count == 1) {
			redisTemplate.expire(key, validTime, TimeUnit.SECONDS);
		}
		return count;
	}
}
