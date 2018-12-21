package ddlGenerate;

import java.io.File;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.aspectj.util.FileUtil;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.Application;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import db.DBBaseTest;


@SpringBootTest(classes = Application.class)
public class DDLGenerator extends DBBaseTest{

private static String tableName = "user_admin_roleset";
	
	private static String ddlName = "AdminRoleSet";
	
	private static String ddlFolder = "E:/workspace/eframe/eframe-sb/src/main/java/com/module/employee/entity/";
	private static String packageName = "com.module.employee.entity";
	//生成文件
	@Test
    public void generateDDLFileCase() throws Exception{
		DruidDataSource dataSource = super.getDataSource();
		DruidPooledConnection conn = dataSource.getConnection();

		String sql = "select * from "+tableName+" limit 0,2";
		ResultSet rs = conn.createStatement().executeQuery(sql);
		
		List<VelocityColumn> fieldList = DDLOperHelper.getDDL(rs);
	
		String fileContent = generateFileStr(fieldList,packageName);
		//System.err.println(fileContent);

		File f = new File(ddlFolder+ddlName+".java");
		FileUtil.writeAsString(f, fileContent);
	}
	/**
	 * 生成简单的ddlText。 getter & setter自己生成。
	 * @throws Exception
	 */
	//@Test
    public void generateDDLTextCase() throws Exception{
		DruidDataSource dataSource = super.getDataSource();
		
		DruidPooledConnection conn = dataSource.getConnection();

		String sql = "select * from "+tableName+" limit 0,2";
		ResultSet rs = conn.createStatement().executeQuery(sql);
		
		List<VelocityColumn> fieldList = DDLOperHelper.getDDL(rs);
	
		String fileContent = generateFileStr(fieldList,null);
		System.err.println(fileContent);
	}
    
	private String generateFileStr(List<VelocityColumn> columnList, String packageName) throws Exception {
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class",
				ClasspathResourceLoader.class.getName());

		ve.init();

		Template t = ve.getTemplate("tbTemplate.vm");
		VelocityContext ctx = new VelocityContext();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDateStr = sdf.format(new Date());
		
		ctx.put("TBName", tableName);
		ctx.put("DDLName", ddlName);
		ctx.put("date", currentDateStr);

		ctx.put("columnList", columnList);
		ctx.put("packageName", packageName);
		
		StringWriter sw = new StringWriter();

		t.merge(ctx, sw);

		System.out.println("\n\n\n\n\n\n");
		
		return sw.toString();
	}
}
