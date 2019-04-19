package lottery;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.Application;

import db.DBBaseTest;

/**
 * 约瑟夫问题有关
 * @Date 2019年3月5日
 * @author E.E.
 *
 */
@SpringBootTest(classes = Application.class)
public class LotteryTest extends DBBaseTest{

	int totalCount = 9999;
	
	//随机获取10个
	int pickCount = 10;
	
	public List<Integer> empList(){
		List<Integer> list = new ArrayList<Integer>();
		for(int i=1;i<totalCount;i++){
			list.add(i);
		}
		
		//Collections.shuffle(list);//乱序
		
		//Node<Object> head = Node.createLinkedListFromArray(list.toArray());
		
		return list;
	}
	
	@Test
	public void execute(){
		List<Integer> empList = empList();
		
		for(int i=1; i<pickCount; ++i){	//要和上面mockdata的下标对齐
			Random rand = new Random(); 
			//随机获取某个下标，移除掉这个下标的元素
			int idx = rand.nextInt(empList.size());
			System.out.println("抽取出："+empList.remove(idx));
		}
		
	}
}
