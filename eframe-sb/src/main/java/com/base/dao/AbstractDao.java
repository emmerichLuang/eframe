package com.base.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.base.util.JacksonUtil;

public abstract class AbstractDao {
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	private Logger sqlLog = Logger.getLogger("sql");
	
	//
	public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
		long begin = System.currentTimeMillis();
		try {
			return jdbcTemplate.batchUpdate(sql, batchArgs);
		} finally {
			long spendTime = System.currentTimeMillis() - begin;
			sqlLog.info(String.format("execute batchUpdate. sql:%s. \n param:%s  \n. time(millsec):%s", sql,
					batchArgs==null?"null":JacksonUtil.catchedEncode(batchArgs), spendTime));
		}

	}
	
	/**
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public int getCount(String sql, Object... args) {
		long begin = System.currentTimeMillis();
		try {
			return jdbcTemplate.queryForObject(sql, Integer.class, args);
		} finally {
			long spendTime = System.currentTimeMillis() - begin;
			sqlLog.info(String.format("execute getCount. sql:%s. param:%s. time(millsec):%s", sql,
					args==null?"null":JacksonUtil.catchedEncode(args), spendTime));
		}

	}

	/**
	 * 一些不需要返回的。比如说create
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public void executeSql(String sql) {
		long begin = System.currentTimeMillis();

		try {
			jdbcTemplate.execute(sql);
		} finally {
			long spendTime = System.currentTimeMillis() - begin;
			sqlLog.info(String.format("execute executeSql. sql:%s. time(millsec):%s", sql, spendTime));
		}
	}

	/**
	 * 更新、删除操作
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public Integer update(String sql, Object... args) {
		long begin = System.currentTimeMillis();
		try{
			return jdbcTemplate.update(sql, args);			
		}finally{
			long spendTime = System.currentTimeMillis() - begin;
			sqlLog.info(String.format("execute update. sql:%s. param:%s. time(millsec):%s", sql,
					args==null?"null":JacksonUtil.catchedEncode(args), spendTime));
		}

	}
	
	//记录insert语句，还有insert语句参数的顺序
	class InsertEntityDTO{
		public String insertSql;
		List<String> params=new ArrayList<String>();
	}
	
	private <T> InsertEntityDTO generateInsertSql(Class<T> clazz, boolean ignoreId){
		InsertEntityDTO result = new InsertEntityDTO();
		
		Map<String, String> fieldMap = ReflectMapper.getFieldMapByClazz(clazz);//key是 entity的字段名； value是数据库字段名（注解）
		
		String tbName = ReflectMapper.getTableName(clazz.getName());//表名
		String pkName = ReflectMapper.getPKName(clazz.getName());	//主键
		
		StringBuilder insertSql = new StringBuilder(String.format("insert into %s (", tbName));
		
		boolean isFirst = true;
		int fieldSize =0;//多少个字段
		for(String entityFieldName:fieldMap.keySet()){
			String tbFieldName = fieldMap.get(entityFieldName);
			//忽略主键
			if(ignoreId && StringUtils.equals(tbFieldName, pkName)){
				continue;
			}
			if(isFirst){
				insertSql.append(tbFieldName);
				isFirst=false;
			}else{
				insertSql.append(","+tbFieldName);
			}
			fieldSize++;
			result.params.add(entityFieldName);
		}
		insertSql.append(") values(");
		for(int i=0;i<fieldSize;i++){
			if(i==0){
				insertSql.append("?");
			}else{
				insertSql.append(",?");
			}
		}
		insertSql.append(")");
		
		result.insertSql=insertSql.toString();
		
		return result;
	}
	
	
	/**
	 * 批量插入
	 * @param clazz
	 * @param entity
	 * @param ignoreId	是否不插入主键
	 * @throws  
	 * @throws Exception 
	 */
	public <T> void insertBatch(Class<T> clazz, List<T> entityList, boolean ignoreId) throws Exception{
		InsertEntityDTO insertEntity = generateInsertSql(clazz, ignoreId);
		
		Map<String, Field> fieldMap = new HashMap<String, Field>();
		
		long begin = System.currentTimeMillis();
		try{
			List<Object[]> batchArgs = new ArrayList<Object[]>();
			for(T entity:entityList){
				List<Object> params = new ArrayList<Object>();
				for(String entityFieldName:insertEntity.params){
					
					Field f = fieldMap.get(entityFieldName);
					if(f==null){
						f = clazz.getDeclaredField(entityFieldName);
						f.setAccessible(true);						
						fieldMap.put(entityFieldName, f);
					}
					params.add(f.get(entity));
				}	
				batchArgs.add(params.toArray());
			}
			jdbcTemplate.batchUpdate(insertEntity.insertSql, batchArgs);
		}finally{
			long spendTime = System.currentTimeMillis() - begin;
			sqlLog.info(String.format("execute insertBatch. sql:%s. time(millsec):%s", insertEntity.insertSql,spendTime));
		}
	}
	
	
	/**
	 * 
	 * @param clazz
	 * @param entity
	 * @param ignoreId	是否不插入主键
	 * @throws  
	 * @throws Exception 
	 */
	public <T> void insert(Class<T> clazz, T entity, boolean ignoreId) throws Exception{
		InsertEntityDTO insertEntity = generateInsertSql(clazz, ignoreId);
		List<Object> params = new ArrayList<Object>();
		
		long begin = System.currentTimeMillis();
		try{
			for(String entityFieldName:insertEntity.params){
				Field f = clazz.getDeclaredField(entityFieldName);
				f.setAccessible(true);
				params.add(f.get(entity));
			}
			jdbcTemplate.update(insertEntity.insertSql, params.toArray());			
		}finally{
			long spendTime = System.currentTimeMillis() - begin;
			sqlLog.info(String.format("execute insert. sql:%s. param:%s. time(millsec):%s", insertEntity.insertSql,
					entity==null?"null":JacksonUtil.catchedEncode(entity), spendTime));
		}
	}

	/**
	 * 
	 * @param clazz
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> getList(Class<T> clazz, String sql) throws Exception {
		long begin = System.currentTimeMillis();
		
		try{
			List<Map<String, Object>> tempList = jdbcTemplate.queryForList(sql);

			List<T> resultList = new ArrayList<T>();
			for (Map<String, Object> m : tempList) {
				T entity = ReflectMapper.mapEntity(clazz, m);
				resultList.add(entity);
			}
			return resultList;			
		}finally{
			long spendTime = System.currentTimeMillis() - begin;
			sqlLog.info(String.format("execute getList. sql:%s. time(millsec):%s", sql, spendTime));
		}
	}
	
	/**
	 * 返回列表
	 * 
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> getList(Class<T> clazz, String sql, Object... args) throws Exception {
		long begin = System.currentTimeMillis();
		
		try{
			List<Map<String, Object>> tempList = jdbcTemplate.queryForList(sql, args);

			List<T> resultList = new ArrayList<T>();
			for (Map<String, Object> m : tempList) {
				T entity = ReflectMapper.mapEntity(clazz, m);
				resultList.add(entity);
			}

			return resultList;			
		}finally{
			long spendTime = System.currentTimeMillis() - begin;
			sqlLog.info(String.format("execute getList. sql:%s. param:%s. time(millsec):%s", sql,
					args==null?"null":JacksonUtil.catchedEncode(args), spendTime));
		}
	}

	/**
	 * 只返回一个
	 * 
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public <T> T get(Class<T> clazz, String sql, Object... args) throws Exception {
		long begin = System.currentTimeMillis();
		
		try{
			Map<String, Object> m = jdbcTemplate.queryForMap(sql, args);
			return ReflectMapper.mapEntity(clazz, m);	
		}catch(EmptyResultDataAccessException emptyEx){
			//jdbcTemplate.queryForMap 如果没有值，会往外抛这个异常
			return null;			
		}finally{
			long spendTime = System.currentTimeMillis() - begin;
			sqlLog.info(String.format("execute get. sql:%s. param:%s. time(millsec):%s", sql,
					args==null?"null":JacksonUtil.catchedEncode(args), spendTime));
		}

	}

	/**
	 * find by id
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public <T> T get(Class<T> clazz, Object id) throws Exception {
		long begin = System.currentTimeMillis();
		String sql=null;
		try{
			sql = String.format("select * from %s where %s=?", ReflectMapper.getTableName(clazz.getName()),
					ReflectMapper.getPKName(clazz.getName()));
			Map<String, Object> m = jdbcTemplate.queryForMap(sql, id);
			T entity = ReflectMapper.mapEntity(clazz, m);
			return entity;
		}catch(EmptyResultDataAccessException emptyEx){
			//jdbcTemplate.queryForMap 如果没有值，会往外抛这个异常
			return null;
		}finally{
			long spendTime = System.currentTimeMillis() - begin;
			sqlLog.info(String.format("execute get(find by id). sql:%s, id:%s. time(millsec):%s", sql, id,spendTime));
		}

	}
	
	/**
	 * delete by id
	 * @param clazz
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public <T> int del(Class<T> clazz, Object id) throws Exception {
		
		String sql = String.format("delete from %s where %s=?", ReflectMapper.getTableName(clazz.getName()),
				ReflectMapper.getPKName(clazz.getName()));
		return jdbcTemplate.update(sql, id);

	}	
	

	/**
	 * 应该没啥用的了
	 * 
	 * @return
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * 应该没啥用的了
	 * 
	 * @param jdbcTemplate
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
