package org.eframe.dataAccess.redis;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class JedisSentinel {

	public static void main(String[] args) {

		Set<String> sentinels = new HashSet<String>();
		sentinels.add("120.25.161.235:26379");
		sentinels.add("120.25.161.235:26389");
	        
	        JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinels);  
	          
	        Jedis jedis = pool.getResource();  
	        //jedis.auth("liangEE");
	        
	        jedis.set("jedis", "jedis");  
	          
	        //pool.returnResource(jedis);  
	}

}
