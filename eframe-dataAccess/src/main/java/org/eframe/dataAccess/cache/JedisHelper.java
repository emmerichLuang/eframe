package org.eframe.dataAccess.cache;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

/**
 * 
 * 单个redis的情况。 
 * 默认数据库。可以有密码。
 * @author liangrl
 * @date   2017年2月28日
 *
 */
public class JedisHelper {
	
	private JedisHelper() {
	}

	private static class RedisUtilHolder {
		private static final JedisHelper instance = new JedisHelper();
	}

	public static JedisHelper getInstance() {
		return RedisUtilHolder.instance;
	}

	private static Map<String, JedisPool> maps = new HashMap<String, JedisPool>();

	private static JedisPool getPool(String ip, int port, String password) {
		String key = ip + ":" + port;
		JedisPool pool = null;
		if (!maps.containsKey(key)) {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(RedisConfig.MAX_ACTIVE);
			config.setMaxIdle(RedisConfig.MAX_IDLE);
			config.setMaxWaitMillis(RedisConfig.MAX_WAIT);
			config.setTestOnBorrow(true);
			config.setTestOnReturn(true);

			//pool = new JedisPool(config, ip, port, RedisConfig.TIMEOUT);
			
			/*JedisPool(final GenericObjectPoolConfig poolConfig,
				    final String host, int port, int timeout, final String password,
				    final int database, final String clientName)*/
			
			pool = new JedisPool(config, ip, port, RedisConfig.TIMEOUT, password,
					Protocol.DEFAULT_DATABASE);
			
			maps.put(key, pool);
		} else {
			pool = maps.get(key);
		}
		return pool;
	}

	public Jedis getJedis(String ip, int port, String password) {
		Jedis jedis = null;
		int count = 0;
		do {
			try {
				jedis = getPool(ip, port,password).getResource();
			} catch (Exception e) {
				e.printStackTrace();
				getPool(ip, port,password).returnBrokenResource(jedis);
			}
		} while (jedis == null && count < RedisConfig.RETRY_NUM);
		return jedis;
	}

	public void closeJedis(Jedis jedis, String ip, int port, String password) {
		if (jedis != null) {
			getPool(ip, port,password).returnResource(jedis);
		}
	}

	public static class RedisConfig {
		// 可用连接实例的最大数目，默认值为8；
		// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
		public static int MAX_ACTIVE = 1024;

		// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
		public static int MAX_IDLE = 200;

		// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
		public static int MAX_WAIT = 10000;

		public static int TIMEOUT = 10000;

		public static int RETRY_NUM = 5;
	}
	
}
