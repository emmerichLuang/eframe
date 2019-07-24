package cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

/**
 * 集合
 * @Date 2019年7月24日
 * @author E.E.
 *
 */
public class ZTest {

	static String key = "testZSet6";
	
	static Integer size = 100;
	
	public static void main(String[] args) {
		Jedis conn = TestCacheUtils.getInstance().getClient();
		try{
			//init(conn);
			System.out.println("add 之前：");
			listAll(conn,0.0, 100.0);
			for(int i=0;i<size;i++){
				addData(conn);
			}
			
			System.out.println("add 之后：");
			listAll(conn,0.0, 100.0);
		}finally{
			TestCacheUtils.getInstance().returnClient(conn);
		}
	}

	private static void init(Jedis conn){
		//Map<String,Double> m = new HashMap<String,Double>();
		for(Integer i=0;i<size;i++){
			//m.put(i.toString(), 0.0);
			conn.zadd(key, 0.0, i.toString());
		}
		//conn.zadd(key, m);
		//conn.zadd(key, score, member)
	}
	
	/**
	 * 如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。
	 * 分数值可以是整数值或双精度浮点数。
	 * @param conn
	 */
	private static void addData(Jedis conn){
		//lazy load 的方式， 增加数据
		Random rand = new Random();
		
		Integer id = rand.nextInt(size);
		
		Double val = Math.abs(rand.nextDouble())*size;
		conn.zincrby(key, val, id.toString());
		//conn.zadd(key, 1.0, id.toString());	//会覆盖原来的值
	}
	
	private static void listAll(Jedis conn, Double min, Double max){
		
		// 全部数据 -inf and +inf
		
		//逆序
		//Set<Tuple> allData = conn.zrevrangeByScoreWithScores(key, 100.0,1.0, 0, size);
		//顺序
		Set<Tuple> allData = conn.zrangeByScoreWithScores(key, "-inf", "+inf");
		
		
		System.out.println("size:"+allData.size());
		for(Tuple t: allData){
			System.out.println("id:"+t.getElement()+"\t score:"+t.getScore());
		}
		
		/*Set<String> allData = conn.zrangeByScore(key, 1.0, 100.0);
		for(String val:allData){
			System.out.println(val);
		}*/
	}
}
