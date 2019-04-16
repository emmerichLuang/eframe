package lottery;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.Application;

import db.DBBaseTest;

/**
 * https://zh.wikipedia.org/wiki/%E7%BA%A6%E7%91%9F%E5%A4%AB%E6%96%AF%E9%97%AE%E9%A2%98
 * 
 * 给定人数、起点、方向和要跳过的数字，选择初始圆圈中的位置以避免被处决
 * @Date 2019年4月16日
 * @author E.E.
 *
 */
@SpringBootTest(classes = Application.class)
public class JosephProblem extends DBBaseTest{

	int empCount = 100;
	int beginIdx = 1;
	boolean asc = true;//顺序
	int skipNum = 3; //逢n跳过
	
	
	
	@Test
	public void execute(){
		
	}
}
