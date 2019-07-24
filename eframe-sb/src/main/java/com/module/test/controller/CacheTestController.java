package com.module.test.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import com.base.controller.BaseController;
import com.base.dto.ResultPack;
import com.base.util.CacheUtils;
import com.base.util.GUID;
import com.base.util.SidGenerator;

@Controller
@RequestMapping(value={"/test/cache/*"})
public class CacheTestController extends BaseController{

	static String dataKey= "dataKey";
	
	static String testZSet = "testZSet";
	
	//排行榜
	@ResponseBody
	@RequestMapping(value={"/listZSet"})
	public Object listZSet(){
		Jedis conn = CacheUtils.getInstance().getClient();
		Set<String> data = conn.zrange(testZSet, 0, -1);
		return ResultPack.success().setData(data);
	}
	
	
	//step1 创建一堆库存
	@ResponseBody
	@RequestMapping(value={"/createData"})
	public Object createData(){
		Integer size = 1000;
		Jedis conn = CacheUtils.getInstance().getClient();
		for(int i=0;i<size;i++){
			conn.sadd(dataKey, SidGenerator.getId());
		}
		return ResultPack.success();
	}

	//step2 扣库存
	@ResponseBody
	@RequestMapping(value={"/popData"})
	public Object popData(){
		Map<String,Object> result = new HashMap<String,Object>();
		
		Jedis conn = CacheUtils.getInstance().getClient();
		result.put("leftCount", conn.scard(dataKey));
		result.put("thisVal", conn.spop(dataKey));
		
		return ResultPack.success().setData(result);
	}
	
	/**
	 * 缓存排它锁测试
	 */
	@ResponseBody
	@RequestMapping(value={"/testLock"})
	public Object testLock(){
		String temp = this.getRequest().getParameter("key");
		
		Jedis conn = CacheUtils.getInstance().getClient();
		
		//这个key，目前只在这个事务中可修改。
		conn.watch(temp);
		Transaction tx = conn.multi();
		try{
			Long val = conn.incr(temp);
			conn.unwatch();
			return ResultPack.success().setData(val);
		}finally{
			tx.exec();
		}
	}
	
	@ResponseBody
	@RequestMapping(value={"/set"})
	public Object set() throws Exception{
		String temp = GUID.nextUUID();
		System.err.println(temp);
		CacheUtils.getInstance().setKeyValue(temp, temp.getBytes(), null);
		return ResultPack.success();
	}
	
	@ResponseBody
	@RequestMapping(value={"/get"})
	public Object get() throws Exception{
		String key = this.getRequest().getParameter("key");
		String val = CacheUtils.getInstance().getByKey(key);
		
		
		return ResultPack.success().setData(val);
	}
}
