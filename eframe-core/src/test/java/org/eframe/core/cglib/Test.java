package org.eframe.core.cglib;

/**
 * 代理模式demo之权限判定。
 * @author liangrl
 * @date   2016年9月20日
 *
 */
public class Test {

	public static void main(String[] args) {
		
		//DemoDao proxiedDao =  PriorityFactory.getInstance().getDao(new DemoDao());
		DemoDao proxiedDao =  PriorityFactory.getProxyDao(new ExecutorProxy("EE"));

		proxiedDao.query();
		
		proxiedDao.create();
		proxiedDao.update();
		proxiedDao.delete();
	}
}
