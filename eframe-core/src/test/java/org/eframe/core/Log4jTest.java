package org.eframe.core;

import org.apache.log4j.Logger;

public class Log4jTest {

	public static void main(String[] args) {
		Logger logger = Logger.getLogger(Log4jTest.class);
        
		logger.info("==开始");
        logger.debug("测试用的");
        
        logger.warn("警告");
        
        logger.error("错误！");
        
        logger.info("==结束了~");
	}

}
