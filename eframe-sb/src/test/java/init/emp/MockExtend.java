package init.emp;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.Application;
import com.alibaba.druid.pool.DruidDataSource;
import com.base.util.GUID;
import com.module.user.entity.UserInfoExtend;
import com.module.user.service.UserExtendService;

import db.DBBaseTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MockExtend extends DBBaseTest {

	@Autowired
	private UserExtendService userExtendService;

	@Test
	public void mainCase() throws Exception {
		DruidDataSource datasource = super.getDataSource();

		try {
			UserInfoExtend exInfo = new UserInfoExtend();
			exInfo.setCreateTime(new Date());
			exInfo.setIsEmailCheck(1);
			exInfo.setIsMobileCheck(1);
			exInfo.setNickName("有道云笔记");
			exInfo.setUserId(GUID.nextUUID());
			
			userExtendService.create(exInfo);
			
		} finally {
			if (datasource != null) {
				datasource.close();
			}
		}
	}
}
