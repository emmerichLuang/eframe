package com.base.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * 线程池工具类
 * @author Arthas
 * @create 2018/7/17
 */
public class ThreadPoolUtils {

	//private static final Logger logger = LoggerFactory.getLogger(ThreadPoolUtils.class);
	/**
	 * 线程池维护线程的最少数量
	 */
	private static final int SIZE_CORE_POOL = 10;
	/**
	 * 线程池维护线程的最大数量
	 */
	private static final int SIZE_MAX_POOL = 20;

	/**
	 * 禁止手动初始化
	 */
	private ThreadPoolUtils(){}

	/**
	 * 通过枚举创建单例对象
	 */
	private enum Singleton {
		/**
		 * 线程池单例
		 */
		SINGLETON;
		private ThreadPoolExecutor threadPool;
		Singleton() {
		    // 为线程池命名
            ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                    .setNameFormat("线程池工具类-pool-%d").build();
            // 创建线程池
			threadPool = new ThreadPoolExecutor(
					SIZE_CORE_POOL,
					SIZE_MAX_POOL,
					0L,
					TimeUnit.MILLISECONDS,
					new LinkedBlockingQueue<Runnable>(),
                    namedThreadFactory);
			
			
		}

		/**
		 * 返回单例对象
		 */
		public ThreadPoolExecutor getThreadPool() {
			return threadPool;
		}
	}

	/**
	 * 向池中添加任务
	 * @param task
	 */
	public static void addExecuteTask(Runnable task) {
		if (task != null) {
			Singleton.SINGLETON.getThreadPool().execute(task);
		}
	}

}
