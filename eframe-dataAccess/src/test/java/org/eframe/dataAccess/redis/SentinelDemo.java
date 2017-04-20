package org.eframe.dataAccess.redis;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class SentinelDemo {

	public static void main(String[] args) {

		Set<String> sentinels = new HashSet<String>();
		sentinels.add("120.25.161.235:26379");
		sentinels.add("120.25.161.235:26389");
		
		// "liangEE"
		//sentinel 自己另外有password的！
		JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinels, new GenericObjectPoolConfig(), 1000, null, 0);
		Jedis jedis = pool.getResource();
		try {
		  jedis.set("foo", "bar");
		  String foobar = jedis.get("foo");
		  jedis.zadd("sose", 0, "car"); jedis.zadd("sose", 0, "bike"); 
		  Set<String> sose = jedis.zrange("sose", 0, -1);
		} finally {
		  if (null != jedis) {
		    jedis.close();
		  }
		}
		pool.destroy();
	}

}
