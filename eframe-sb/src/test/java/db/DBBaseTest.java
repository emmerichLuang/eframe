package db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;

import com.alibaba.druid.pool.DruidDataSource;

public abstract class DBBaseTest {

	protected DruidDataSource dataSource = null;

	protected Properties propertie = new Properties();
	
	protected DruidDataSource getDataSource() {
		return dataSource;
	}
	
	static Long begin = null;
	
	@Before
	public void init() throws Exception {
		begin = System.currentTimeMillis();
		System.out.println("开始测试-----------------");
		
		InputStream stream = DBBaseTest.class.getClassLoader().getResourceAsStream("application.properties");
		BufferedReader bf = new BufferedReader(new InputStreamReader(stream));
		propertie.load(bf);
		
		
		String driver = propertie.getProperty("spring.datasource.driver-class-name");
		String url = propertie.getProperty("spring.datasource.url");
		String username = propertie.getProperty("spring.datasource.username");
		String pwd = propertie.getProperty("spring.datasource.password");
		
		dataSource = new DruidDataSource();
		dataSource.setUrl(url);
		dataSource.setDriverClassName(driver);
		dataSource.setUsername(username);
		dataSource.setPassword(pwd);

	}

	@After
	public void after() {
		if(dataSource!=null){
			dataSource.close();
		}
		
		System.out.println("耗时"+(System.currentTimeMillis()-begin)+"(millsec) 测试结束-----------------");
	}
}
