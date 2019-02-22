package init;

import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSource;

import db.DBBaseTest;

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
