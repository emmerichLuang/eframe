package com.module.gobal.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;

import org.apache.log4j.LogManager;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.base.dao.ReflectMapper;
import com.base.spring.SpringContextHolder;


@Component
public class InitAction implements ApplicationRunner {
	
	/**
	 * 校验DDL正确性
	 * 在此项目中， 需要Table bean 既有Table，也有Component注解
	 */
	void checkAndInitTableBeans() {
		
		//所有定义为table的bean
		Map<String, Object> tableBeans = SpringContextHolder.getApplicationContext().getBeansWithAnnotation(Table.class);
		
		//初始化数据库配置 
		List<Class<?>> clazzList = new ArrayList<Class<?>>();
		for (String key : tableBeans.keySet()) {
			Object bean = tableBeans.get(key);

			clazzList.add(bean.getClass());
		}

		ReflectMapper.initFieldMapByClazz(clazzList);
		System.err.println("Init table beans:"+tableBeans);
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.err.println("InitAction.run!");
		
		//tables注解的bean，是否合法
		checkAndInitTableBeans();
		
		
		// 系统关闭的钩子
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				//关闭transfermanager
//				AmazonS3Instance.getInstance().closeTransferManager(true);
				
				//日志关闭
				LogManager.shutdown();
			}
		});
	}
}
