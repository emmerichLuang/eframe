package org.eframe.dataAccess.redis;

import java.util.UUID;

import org.eframe.dataAccess.cache.JedisHelper;

import redis.clients.jedis.Jedis;

public class JedisTest {
	public static void main(String[] args){
		final String ipAddr = "127.0.0.1";
	    final int port = 6379;
	    Jedis jedis = JedisHelper.getInstance().getJedis(ipAddr, port,"liangEE");
	    
	    String randomString = UUID.randomUUID().toString();
	    
	    jedis.set(randomString, randomString); 
	    
	    System.err.println(jedis.get(randomString));
	    JedisHelper.getInstance().closeJedis(jedis,ipAddr, port,"liangEE");
	}
}
