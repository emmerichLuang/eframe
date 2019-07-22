package cache;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.Application;

import db.DBBaseTest;

/**
 * redis排它锁测试
 * @Date 2019年7月19日
 * @author E.E.
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class LockTest extends DBBaseTest{
	
	
}
