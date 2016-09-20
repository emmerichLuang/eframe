package org.eframe.core.cglib;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;



import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

/**
 * 工厂
 * @author liangrl
 * @date   2016年9月20日
 *
 */
public class PriorityFactory {
	
	private static PriorityFactory instance = new PriorityFactory();
	
	public static List<String> HAVE_PRIORITY_EXECUTOR = new ArrayList<String>();
	static{
		HAVE_PRIORITY_EXECUTOR.add("BOSS");
		HAVE_PRIORITY_EXECUTOR.add("BIG BOSS");
	}
	
	private PriorityFactory(){
		
	}
	
	public static PriorityFactory getInstance(){
		return instance;
	}
	
	public DemoDao getDao(DemoDao dao){
		return dao;
	}
	
	public static DemoDao getProxyDao(ExecutorProxy myProxy){    
	     Enhancer en = new Enhancer();     
	     //进行代理     
	     en.setSuperclass(DemoDao.class);     
	     
	     //en.setCallback(myProxy);     
	     en.setCallbacks(new Callback[]{myProxy,NoOp.INSTANCE});   
	     en.setCallbackFilter(new MyProxyFilter());
	     //生成代理实例     
	     return (DemoDao)en.create();     
	 }
	
	public static class MyProxyFilter implements CallbackFilter {  
		
		 public int accept(Method arg0) {     
		        if(!"query".equalsIgnoreCase(arg0.getName()))     
		            return 0;     
		        return 1;     
		    }  
		}  
}
