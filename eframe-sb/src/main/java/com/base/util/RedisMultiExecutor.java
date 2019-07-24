package com.base.util;


import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * redis排他操作。
 * 
 * 只能对没有autoWired的类操作。
 * @Date 2019年7月22日
 * @author E.E.
 *
 */
public class RedisMultiExecutor implements MethodInterceptor{
	
	private static final Logger logger = LoggerFactory.getLogger(RedisMultiExecutor.class);
	
	/**
	 * 把增强之后的代理对象缓存起来，避免重复创建代理对象，影响性能
	 */
	private Map<String, Object> proxyCache = new ConcurrentHashMap<String, Object>();
	
	/**
	 * 线程池
	 */
	
	private static volatile  RedisMultiExecutor instance = null;
	
	
	private RedisMultiExecutor() {
		init();
	}
	
	private void init() {
		//可以做些工作
	}
	
	/**
	 * 获取异步执行器实例
	 * @return
	 */
	public static RedisMultiExecutor getInstance() {
		if(instance == null) {
			synchronized (RedisMultiExecutor.class) {
				if(instance == null) {
					instance = new RedisMultiExecutor();
				}			
			}			
		}
		return instance;
	}
	
	/**
	 * 返回指定业务service类的代理对象
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getProxy(Class<T> clazz) {
		return (T)this.enhance(clazz);
	}
	
	/**
	 * 根据类对象，返回增强之后的代理对象
	 * @param clazz
	 * @return
	 */
	private  Object enhance(Class<?> clazz) {
		if(proxyCache.get(clazz.getName()) != null) {
			logger.debug("缓存中存在{}的增强对象", clazz.getName());
			return proxyCache.get(clazz.getName());
		}
		Enhancer enhancer = new Enhancer();
		enhancer.setCallback(this);
		enhancer.setSuperclass(clazz);
		Object o = enhancer.create();
		proxyCache.put(clazz.getName(), o);
		return o;
	}

	public Object intercept(final Object obj, final Method method, final Object[] args,
			final MethodProxy methodProxy) throws Throwable {
		
		Jedis conn = CacheUtils.getInstance().getClient();
		
		//这个key，目前只在这个事务中可修改。
		conn.watch((String)args[0]);
		Transaction tx = conn.multi();
		try{
			Object o = methodProxy.invokeSuper(obj, args);
			conn.unwatch();
			return o;			
		}finally{
			tx.exec();
		}
	}
	
}
