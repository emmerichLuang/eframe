package lottery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.Application;
import com.base.collection.Node;

import db.DBBaseTest;

@SpringBootTest(classes = Application.class)
public class LotteryTest extends DBBaseTest{

	int size = 1000;
	public List<Integer> empList(){
		List<Integer> list = new ArrayList<Integer>();
		for(int i=1;i<size;i++){
			list.add(i);
		}
		
		Collections.shuffle(list);
		
		Node<Object> head = Node.createLinkedListFromArray(list.toArray());
		
		return list;
	}
	
	@Test
	public void execute(){
		List<Integer> empList = empList();
		
	}
}
