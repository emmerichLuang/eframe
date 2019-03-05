package collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.Application;
import com.base.collection.Node;

import db.DBBaseTest;

/**
 * 链表
 * @Date 2019年3月5日
 * @author E.E.
 *
 */
@SpringBootTest(classes = Application.class)
public class NodeTest extends DBBaseTest{
	int size = 100;
	public Node<Object> empList(){
		List<Integer> list = new ArrayList<Integer>();
		for(int i=1;i<size;i++){
			list.add(i);
		}
		
		Collections.shuffle(list);
		
		Node<Object> head = Node.createLinkedListFromArray(list.toArray());
		
		return head;
	}
	
	@Test
	public void execute(){
		Node<Object> head = empList();

		Node<Object> current = head;
		do{
			System.out.println(current.getValue());
			current = current.getNext();
		}while(current.getNext()!=null);
		
	}
}
