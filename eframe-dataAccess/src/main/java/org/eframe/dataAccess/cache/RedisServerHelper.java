package org.eframe.dataAccess.cache;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Transaction;
import redis.clients.util.Pool;

public class RedisServerHelper {

	private static Logger logger = Logger.getLogger(RedisServerHelper.class);
	
	public static String REDIS_SERVER = "120.25.161.235:26379;120.25.161.235:26389";
	
	public static String masterName = "mymaster";
	
	public static String REDIS_DATABASE = "0";
	
	//public static String REDIS_PASSWORD = "liangEE";
	public static String REDIS_PASSWORD = null;
	
	public static String REDIS_MAX_ACTTVE = "100";
	
	public static String REDIS_MAX_IDLE = "32";
	
	public static String REDIS_MAX_WAIT = "60000";
	
	public static String REDIS_TEST_ON_BORROW = "false";

	// Redis ping命令的返回值，表示连通
	public static final String PONG = "PONG";

	// 过期时间，4个小时
	public static final int EXPIRE_SECONDS = 4 * 3600;

	private static RedisServerHelper instance;

	// 针对有监控环境的连接池对象
	private Pool<Jedis> pool;
	
	/**
	 * 获取RedisServerHelper的实例
	 * 
	 * @return
	 */
	public static RedisServerHelper getInstance() {
		if (instance == null) {
			instance = new RedisServerHelper();
		}
		return instance;
	}

	private RedisServerHelper() {
		init();
	}

	private void init() {
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		if (StringUtils.isNotBlank(REDIS_MAX_ACTTVE)) {
			poolConfig.setMaxTotal(Integer.parseInt(REDIS_MAX_ACTTVE));
		}
		if (StringUtils.isNotBlank(REDIS_MAX_IDLE)) {
			poolConfig.setMaxIdle(Integer.parseInt(REDIS_MAX_IDLE));
		}
		if (StringUtils.isNotBlank(REDIS_MAX_WAIT)) {
			poolConfig.setMaxWaitMillis(Long.parseLong(REDIS_MAX_WAIT));
		}

		// 监控节点的集合，直接使用host和port连接的字符串即可
		Set<String> sentinels = new HashSet<String>();
		String[] sentinesArr = REDIS_SERVER.split(";");
		try {
			for (String sentinel : sentinesArr) {
				sentinels.add(sentinel);
			}

			if (masterName != null) {
				// 构造Jedis的带监控功能的连接池，masterName需要与监控配置文件sentinel.conf中定义的名称相同
				pool = new JedisSentinelPool(masterName, sentinels, poolConfig);
			} else if (sentinesArr.length == 1) {	//只有一个，就是ip端口啦。一般测试环境是这样子的
				String[] hostAndPort = sentinesArr[0].split(":");
				pool = new JedisPool(poolConfig, hostAndPort[0], Integer.parseInt(hostAndPort[1]));
			}
		} catch (Exception e) {
			logger.error("error creating JedisSentinelPool。REDIS_SERVER:"+REDIS_SERVER, e);
		}
	}

	private Pool<Jedis> getPool() {
		try {
			if (pool == null) {
				init();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (pool != null) {
				pool.destroy();
			}
			
			init();
		}
		return pool;
	}

	/**
	 * 获取当前服务的客户端连接，使用后需调用returnClient将连接返回池中
	 * 
	 * @return
	 */
	public Jedis getClient() {
		Jedis client = getPool().getResource();
		if (StringUtils.isNotBlank(REDIS_PASSWORD)) {
			client.auth(REDIS_PASSWORD);
		}
		if (StringUtils.isNotBlank(REDIS_DATABASE)) {
			client.select(Integer.parseInt(REDIS_DATABASE));
		}
		return client;
	}

	public void returnClient(Jedis client) {
		pool.returnResource(client);
	}

	public void stop() {
		try {
			pool.destroy();
		} catch (Exception e) {
			// Do nothing.
		}
	}

	/**
	 * 测试连接是否连通
	 * 
	 * @param client
	 * @return
	 */
	public boolean canPing(Jedis client) {
		try {
			if (client != null && StringUtils.isNotBlank(REDIS_PASSWORD)) {
				client.auth(REDIS_PASSWORD);
			}
			return PONG.equals(client.ping());
		} catch (Exception e) {
			return false;
		}
	}

	// ************************** 基本操作方法 begin *****************************
	/**
	 * 指定key获取value
	 * 
	 * @param key
	 *            key统一使用字符串
	 * @return
	 * @throws Exception
	 */
	public byte[] getValueByKey(String key) throws Exception {
		Jedis client = getClient();
		try {
			byte[] value = client.get(key.getBytes());
			return value;
		} catch (Exception e) {
			logger.error("Error getting value by key " + key, e);
			throw e;
		} finally {
			returnClient(client);
		}
	}

	/**
	 * 指定key获取list
	 * 
	 * @param key
	 *            key统一使用字符串
	 * @return
	 * @throws Exception
	 */
	public List<byte[]> getListByKey(String key) throws Exception {
		Jedis client = getClient();
		try {
			List<byte[]> list = client.lrange(key.getBytes(), 0, -1);
			return list;
		} catch (Exception e) {
			logger.error("Error getting list by key " + key, e);
			throw e;
		} finally {
			returnClient(client);
		}
	}

	/**
	 * 指定key获取hash
	 * 
	 * @param key
	 *            key统一使用字符串
	 * @return
	 * @throws Exception
	 */
	public Map<byte[], byte[]> getHashByKey(String key) throws Exception {
		Jedis client = getClient();
		try {
			Map<byte[], byte[]> hash = client.hgetAll(key.getBytes());
			return hash;
		} catch (Exception e) {
			logger.error("Error getting hash by key " + key, e);
			throw e;
		} finally {
			returnClient(client);
		}
	}

	/**
	 * 指定key和hash中的元素获取hash的某个值
	 * 
	 * @param key
	 *            key统一使用字符串
	 * @param field
	 *            field统一使用字符串
	 * @return
	 * @throws Exception
	 */
	public byte[] getHashFieldByKey(String key, String field) throws Exception {
		Jedis client = getClient();
		try {
			byte[] hashField = client.hget(key.getBytes(), field.getBytes());
			return hashField;
		} catch (Exception e) {
			logger.error("Error getting hash field by key " + key + ", " + field, e);
			throw e;
		} finally {
			returnClient(client);
		}
	}

	/**
	 * 指定key和value进行设置
	 * 
	 * @param key
	 *            key统一使用字符串
	 * @param value
	 *            value统一使用序列化后的字节数组
	 * @param expire
	 *            超时时间，仅大于0时有效，单位为s
	 * @throws Exception
	 */
	public void setKeyValue(String key, byte[] value, Integer expire) throws Exception {
		Jedis client = getClient();
		try {
			client.set(key.getBytes(), value);

			if (expire != null && expire > 0) {
				client.expire(key, EXPIRE_SECONDS);
			}
		} catch (Exception e) {
			logger.error("Error setting value of key " + key, e);
			throw e;
		} finally {
			returnClient(client);
		}
	}

	/**
	 * 指定key和list进行列表设置，由于该操作需要先删除旧list再设置，加入了事务控制
	 * 
	 * @param key
	 *            key统一使用字符串
	 * @param list
	 *            list使用二维字节数组
	 * @param expire
	 *            超时时间，仅大于0时有效，单位为s
	 * @throws Exception
	 */
	public void setKeyList(String key, byte[][] list, Integer expire) throws Exception {
		Jedis client = getClient();
		try {
			Transaction trans = client.multi();
			trans.del(key);
			trans.rpush(key.getBytes(), list);
			if (expire != null && expire > 0) {
				trans.expire(key, EXPIRE_SECONDS);
			}
			trans.exec();

		} catch (Exception e) {
			logger.error("Error setting hash of key " + key, e);
			throw e;
		} finally {
			returnClient(client);
		}
	}

	/**
	 * 指定key和map进行hash设置，由于该操作需要先删除旧hash再设置，加入了事务控制
	 * 
	 * @param key
	 *            key统一使用字符串
	 * @param hash
	 *            hash用Map表示，其中key为字符串直接转直接数组，value为对象序列化
	 * @param expire
	 *            超时时间，仅大于0时有效，单位为s
	 * @throws Exception
	 */
	public void setKeyHash(String key, Map<byte[], byte[]> hash, Integer expire) throws Exception {
		Jedis client = getClient();
		try {
			Transaction trans = client.multi();
			trans.del(key);
			trans.hmset(key.getBytes(), hash);
			if (expire != null && expire > 0) {
				trans.expire(key, EXPIRE_SECONDS);
			}
			trans.exec();
		} catch (Exception e) {
			logger.error("Error setting hash.", e);
			throw e;
		} finally {
			returnClient(client);
		}
	}

	/**
	 * 指定key和map中的单元进行hash设置，用于设置hash中的单个值
	 * 
	 * @param key
	 *            key统一使用字符串
	 * @param field
	 *            field使用字符串
	 * @param fieldValue
	 *            fieldValue使用序列化后的字节数组
	 * @param expire
	 *            超时时间，仅大于0时有效，单位为s
	 * @throws Exception
	 */
	public void setKeyHashField(String key, String field, byte[] fieldValue, Integer expire) throws Exception {
		Jedis client = getClient();
		try {
			client.hset(key.getBytes(), field.getBytes(), fieldValue);
			if (expire != null && expire > 0) {
				client.expire(key, EXPIRE_SECONDS);
			}
		} catch (Exception e) {
			logger.error("Error setting hash field.", e);
			throw e;
		} finally {
			returnClient(client);
		}
	}
	
	public Long deleteKey(String key) throws Exception {
		Jedis client = getClient();
		try {
			return client.del(key);
		} catch (Exception e) {
			logger.error("Error delete key " + key, e);
			throw e;
		} finally {
			returnClient(client);
		}
	}

	// ************************** 基本操作方法 end *****************************

	public static void main(String[] args) {

		try {
			RedisServerHelper redisHelper = RedisServerHelper.getInstance();
			Jedis client = redisHelper.getClient();
			System.out.println("client1 = " + client.info());
			System.out.println("PING1 = " + client.ping());
			long start = System.currentTimeMillis();
			for (int i = 0; i < 50; i++) {
				client.set("key2", UUID.randomUUID().toString());
			}
			System.out.println((System.currentTimeMillis() - start));
			redisHelper.returnClient(client);
			start = System.currentTimeMillis();
			/*try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}*/

			client = redisHelper.getClient();
			System.out.println("client2 = " + client);
			System.out.println("PING2 = " + client.ping());
			for (int i = 0; i < 10; i++) {
				String key2 = client.get("key2");
				System.out.println("key2 = " + key2);
			}
			redisHelper.returnClient(client);

			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
