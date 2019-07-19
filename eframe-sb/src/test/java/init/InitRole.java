package init;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.Application;
import com.alibaba.druid.pool.DruidDataSource;

import db.DBBaseTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class InitRole extends DBBaseTest{
	
	//生成全部功能用户组
	@Test
    public void mainCase() throws Exception{
		DruidDataSource datasource = super.getDataSource();
		try{
			
			
			
		}finally{
			if(datasource!=null){
				datasource.close();
			}
		}
	}
}
