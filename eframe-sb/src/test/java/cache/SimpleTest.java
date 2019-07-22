package cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.Application;
import com.base.service.RedisService;

/**
 * RedisTemplate 需要2.8+版本的redis，比较不方便
 * @Date 2019年7月22日
 * @author E.E.
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SimpleTest {
	
	@Autowired
	private RedisService redisService;
	
	@Test
	public void templateTest() throws Exception{
		String key = "testKey";
		redisService.set(key, key);
		
		String val = (String) redisService.get(key);
		System.out.println(val);
	}
	
	/*@Test
	public void getsetCase() throws Exception{
		String key = "testKey";
		CacheUtils.getInstance().setKeyValue(key, key.getBytes(), null);
		
		String val = CacheUtils.getInstance().getByKey(key);
		System.out.println(val);
	}*/
	
	
}
