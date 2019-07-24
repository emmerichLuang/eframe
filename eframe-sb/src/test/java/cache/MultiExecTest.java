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

	public static Integer COUNT = 100;
	public static Integer THREAD_COUNT = 100000;
	
	public static String key = "lockKey";
	
	public static void main(String[] args) {
		noLock();
	}

	static void noLock(){
		for(int i=0; i<THREAD_COUNT; i++){
			new Runnable(){
				@Override
				public void run() {
					if(MultiExecTest.COUNT<1){
						return;
					}else{
						MultiExecTest.COUNT--;		
					}
					
					System.err.println("COUNT:"+MultiExecTest.COUNT);
				}
			}.run();
		}
	}
	
	static void hasLock(){
		for(int i=0; i<THREAD_COUNT; i++){
			new Runnable(){
				@Override
				public void run() {
					
					Response<String> resp =null;
					
					Jedis conn = TestCacheUtils.getInstance().getClient();
					conn.watch(key);
					Transaction tx = conn.multi();
					try{
						resp = tx.set(key, "doing");
						
						if(MultiExecTest.COUNT<1){
							System.err.println("已经无数据了");
							return;
						}else{
							MultiExecTest.COUNT--;							
						}
					}finally{
						tx.exec();
						System.err.println("COUNT:"+COUNT);
						conn.unwatch();
						TestCacheUtils.getInstance().returnClient(conn);
					}
				}
			}.run();
		}
	}
}
