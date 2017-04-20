package org.eframe.dataAccess.redis;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class JedisSentinel {

	public static void main(String[] args) {

		Set<String> sentinels = new HashSet<String>();
		sentinels.add("127.0.0.1:26379");
		sentinels.add("127.0.0.1:26389");
	        
	        JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinels);  
	          
	        Jedis jedis = pool.getResource();  
	        //jedis.auth("liangEE");
	        
	        jedis.set("jedis", "jedis");  
	          
	        //pool.returnResource(jedis);  
	}

}
