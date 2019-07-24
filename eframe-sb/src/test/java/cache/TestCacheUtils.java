package cache;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Transaction;
import redis.clients.util.Pool;


public class TestCacheUtils {
	
	private static Logger logger = Logger.getLogger(TestCacheUtils.class);
	
	public static String REDIS_SERVER = "172.27.137.3:6379";
	
	public static String REDIS_DATABASE = "6";
	
	public static String REDIS_PASSWORD = "";
	
	public static String REDIS_MAX_ACTTVE = "32";
	
	public static String REDIS_MAX_IDLE = "32";
	
	public static String REDIS_MAX_WAIT = "60000";
	
	public static String REDIS_TEST_ON_BORROW = "false";

	// Redis ping命令的返回值，表示连通
	public static final String PONG = "PONG";
	
	public static final int EXPIRE_SECONDS = 4 * 3600;
	
	private static TestCacheUtils instance;
	
	// 针对有监控环境的连接池对象
	private Pool<Jedis> pool;
	
	
	private TestCacheUtils(){
		init();
	}
	
	private void init(){
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
			String masterName = null;
			for (String sentinel : sentinesArr) {
				if (sentinel.contains(":")) {
					sentinels.add(sentinel);
				} else {
					masterName = sentinel;
				}
			}

			if (masterName != null) {
				// 构造Jedis的带监控功能的连接池，masterName需要与监控配置文件sentinel.conf中定义的名称相同
				pool = new JedisSentinelPool(masterName, sentinels, poolConfig);
			} else if (sentinesArr.length == 1) {
				String[] hostAndPort = sentinesArr[0].split(":");
				pool = new JedisPool(poolConfig, hostAndPort[0], Integer.parseInt(hostAndPort[1]));
			}
		} catch (Exception e) {
			logger.error("error creating JedisSentinelPool。REDIS_SERVER:"+REDIS_SERVER, e);
		}
	}
	
	public static TestCacheUtils getInstance() {
		if (instance == null) {
			instance = new TestCacheUtils();
		}
		return instance;
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
	
	public boolean removeExpire(String key) throws Exception{
		Jedis client = getClient();
		
		try{
			//1: the key is now persist. 
			//0: the key is not persist (only happens when key not set).
			return client.persist(key)==1l?true:false;
			
		}catch (Exception e) {
			logger.error("Error removeExpire key " + key, e);
			return false;
		} finally {
			returnClient(client);
		}
	}
	
	public String getByKey(String key) throws Exception {
		Jedis client = getClient();
		try {
			String value = client.get(key);
			return value;
		} catch (Exception e) {
			logger.error("Error getting value by key " + key, e);
			throw e;
		} finally {
			returnClient(client);
		}
	}
	
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
}
