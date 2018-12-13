package com.base.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.base.ex.EntityNoMappingException;
import com.base.util.JacksonUtil;


/**
 * 网上找的
 * 
 * @author will_awoke http://blog.csdn.net/will_awoke/article/details/27683703
 * @version 2014-5-30
 * @see ReflectMapper
 * @since
 */
public class ReflectMapper {

	private static final Logger logger = LoggerFactory.getLogger(ReflectMapper.class);
	
	/**
	 * 做过的内容就不用重复做了
	 * 然而， 这些类的名字必须不一样。
	 * 不同目录下相同名字的类会出现异常的。
	 * 
	 * 外层map的key className
	 * 内层map， key是 entity的字段名； value是数据库字段名（注解）
	 */
	private static Map<String, HashMap<String, String>> ALL = new HashMap<String, HashMap<String, String>>();
	
	/***
	 * 某个表的主键
	 * key是entity名， value是数据库的字段名
	 */ 
	private static Map<String, String> PRIMARY_KEY = new HashMap<String, String>();
	
	private static Map<String, String> TABLE_NAME = new HashMap<String, String>();
	
	/**
	 * 尝试懒加载dto类.不一定成功
	 * dto必须继承entity父类
	 * @param clazz
	 */
	private static void tryLazyLoadDTOClazz(Class<?> clazz){
		String name = clazz.getName();	
		
		Class<?> superClazz = clazz.getSuperclass();
		if(!superClazz.isAnnotationPresent(Table.class)){
			logger.info(name+" 的父类没有Table注解。");
			return;
		}
		Table tableAnn = superClazz.getAnnotation(Table.class);
		TABLE_NAME.put(name, tableAnn.name());
		
		//column的映射
		HashMap<String, String> fieldHasColumnAnnoMap = new HashMap<String, String>();
		Field[] superClassfields = superClazz.getDeclaredFields();
		for (Field field : superClassfields) {	//一个field有多个annotation
			Annotation[] annotations = field.getAnnotations();
			for (Annotation an : annotations) {
				if (an instanceof Column) {
					Column column = (Column) an;
					fieldHasColumnAnnoMap.put(field.getName(), column.name());
					
					if(field.isAnnotationPresent(Id.class)){	//某个表的主键
						PRIMARY_KEY.put(name, column.name());
					}
				}
			}
		}
		
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {	//一个field有多个annotation
			Annotation[] annotations = field.getAnnotations();
			for (Annotation an : annotations) {
				if (an instanceof Column) {
					Column column = (Column) an;
					fieldHasColumnAnnoMap.put(field.getName(), column.name());
				}
			}
		}
		
		ALL.put(name, fieldHasColumnAnnoMap);
	}
	
	/**
	 * 参数是 Table注解了的ddl
	 * 初始化所有的ddl字段对应数据表字段
	 * @param clazzList
	 */
	public static void initFieldMapByClazz(List<Class<?>> clazzList){
		for(Class<?> clazz: clazzList){
			String name = clazz.getName();	
			
			//数据表的名字
			Table tableAnn = clazz.getAnnotation(Table.class);
			if(StringUtils.isEmpty(tableAnn.name())){
				throw new EntityNoMappingException(clazz.getName() +" has not table name");
			}
			TABLE_NAME.put(name, tableAnn.name());
			//CACHE_PREFIX.put(name, tableAnn.cachePrefix());
			
			//column的映射
			HashMap<String, String> fieldHasColumnAnnoMap = new HashMap<String, String>();
			Field[] fields = clazz.getDeclaredFields();
			Annotation[] annotations = null;
			for (Field field : fields) {	//一个field有多个annotation
				annotations = field.getAnnotations();
				for (Annotation an : annotations) {
					if (an instanceof Column) {
						Column column = (Column) an;
						fieldHasColumnAnnoMap.put(field.getName(), column.name());
						
						if(field.isAnnotationPresent(Id.class)){	//某个表的主键
							PRIMARY_KEY.put(name, column.name());
						}
					}
				}
			}
			
			if(StringUtils.isEmpty(PRIMARY_KEY.get(name))){
				throw new EntityNoMappingException(clazz.getName() +" no primary key");
			}
			
			ALL.put(name, fieldHasColumnAnnoMap);
		}
	}
	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static Map<String, String> getFieldMapByClazz(Class<?> clazz){
		String name = clazz.getName();	

		if(ALL.containsKey(name)){
			return ALL.get(name);
		}

		tryLazyLoadDTOClazz(clazz);
		if(ALL.containsKey(name)){
			return ALL.get(name);
		}else{
			throw new EntityNoMappingException(clazz.getName() +" has not map annotations.such as column, table");				
		}
	}
	
	/**
	 * pk
	 * @param ddlName
	 * @return
	 */
	public static String getPKName(String ddlName){
		return PRIMARY_KEY.get(ddlName);
	}
	
	/**
	 * tableName
	 * @param ddlName
	 * @return
	 */
	public static String getTableName(String ddlName){
		return TABLE_NAME.get(ddlName);
	}
	
	/**
	 * 缓存前缀
	 * @param ddlName
	 * @return
	 */
	/*public static String getCachePrefix(String ddlName){
		return CACHE_PREFIX.get(ddlName);
	}*/
	
	
	/**
	 * 将jdbcTemplate查询的map结果集 反射生成对应的bean
	 * 
	 * @param clazz
	 *            意向反射的实体.clazz
	 * @param jdbcMapResult
	 *            查询结果集 key is UpperCase
	 * @return
	 * @throws Exception 
	 * @see
	 */
	@SuppressWarnings("unchecked")
	public static <T> T mapEntity(Class<T> clazz, Map<String, Object> jdbcMapResult) throws Exception {
		// 获得
		Map<String, String> fieldHasColumnAnnoMap = getFieldMapByClazz(clazz);
		
		// 存放field name 和 对应的来自map的该field的属性值，用于后续reflect成ModelBean
		Map<String, Object> conCurrent = new LinkedHashMap<String, Object>();
		for (Map.Entry<String, String> en : fieldHasColumnAnnoMap.entrySet()) {
			// 将column大写。因为jdbcMapResult key is UpperCase
			String key = en.getValue();

			// 获得map的该field的属性值
			Object value = jdbcMapResult.get(key);

			// 确保value有效性，防止JSON reflect时异常
			if (value != null) {
				conCurrent.put(en.getKey(), jdbcMapResult.get(key));
			}
		}
		String temp = JacksonUtil.encode(conCurrent);
		return (T) JacksonUtil.decode(temp, clazz);
	}
}