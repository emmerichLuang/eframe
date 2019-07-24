package cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

/**
 * 测试排他
 * 线程A正在修改内容， 线程b不可修改内容。
 * @Date 2019年7月24日
 * @author E.E.
 *
 */
public class MultiExecTest {

	public static Integer COUNT = 20;
	public static volatile Integer THREAD_COUNT = 200;
	
	public static String key = "lockKey3";
	
	public static void main(String[] args) {
		//noLock();
		_lockTest();
	}

	static void noLock(){
		for(int i=0; i<THREAD_COUNT; i++){
			new Thread(new Runnable(){
				@Override
				public void run() {
					if(MultiExecTest.COUNT<1){
						return;
					}else{
						MultiExecTest.COUNT--;		
					}
					
					System.err.println("COUNT:"+MultiExecTest.COUNT);
				}
			}).start();
		}
	}
	
	/**
	 * 命令在设置成功时返回 1 ， 设置失败时返回 0 。
	 * @param key
	 * @return
	 */
	static boolean setLockSuccess(String key, Jedis conn){
		
		Long val = conn.setnx(key, "doing");
		if(val==0){
			//一次重试
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			val = conn.setnx(key, "doing");
			if(val==1){
				return true;
			}else{
				return false;				
			}
		}else{
			return true;
		}
	}
	
	static void releaseLock(String key, Jedis conn){
		conn.del(key);
	}
	
	static void _lockTest(){
		for(int i=0; i<THREAD_COUNT; i++){
			
			new Thread(
			new Runnable(){
				@Override
				public void run() {
					Jedis conn = TestCacheUtils.getInstance().getClient();
					boolean flag = setLockSuccess(key, conn);
					if(!flag){
						System.err.println("重试过都不行，本次线程结束");
						return;
					}
					try{
						if(MultiExecTest.COUNT<1){
							//System.err.println("已经无数据了(inner)");
							return;
						}
						MultiExecTest.COUNT--;
						System.err.println("MultiExecTest.COUNT:"+MultiExecTest.COUNT);
					}finally{
						releaseLock(key, conn);
					}
				}
			}).start();
		}
	}
}
