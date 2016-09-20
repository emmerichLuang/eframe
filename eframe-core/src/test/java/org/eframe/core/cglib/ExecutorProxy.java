package org.eframe.core.cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ExecutorProxy implements MethodInterceptor {
	
	public Enhancer enhancer = new Enhancer();
	private String executor;

	public ExecutorProxy(String executor) {
		this.executor = executor;
	}

	public Object intercept(Object object, Method method, Object[] args,
			MethodProxy methodProxy) throws Throwable {

		// 用户进行判断
		if (!PriorityFactory.HAVE_PRIORITY_EXECUTOR.contains(executor)) {
			System.out.println(executor+"你没有"+method.getName()+"这个方法的权限！");
			return null;
		}
		Object result = methodProxy.invokeSuper(object, args);

		return result;
	}
}
