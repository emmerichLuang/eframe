package cache;

import com.base.util.GUID;

import redis.clients.jedis.Jedis;

public class SetTest {

	static String key = "testSet";
	
	static Integer size = 100;
	
	public static void main(String[] args) {
		Jedis conn = TestCacheUtils.getInstance().getClient();
		try{
			//init(conn);
			
			pop(conn);
		}finally{
			TestCacheUtils.getInstance().returnClient(conn);
		}
	}
	
	static void init(Jedis conn){
		for(Integer i=0;i<size;i++){
			conn.sadd(key, GUID.nextUUID());			
		}
	}
	

	static void pop(Jedis conn){
		System.out.println("data:"+conn.spop(key));
		System.out.println("remian:"+conn.scard(key));
	}
}
