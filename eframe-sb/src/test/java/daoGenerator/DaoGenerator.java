package daoGenerator;

import java.io.File;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.aspectj.util.FileUtil;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.Application;

import db.DBBaseTest;

@SpringBootTest(classes = Application.class)
public class DaoGenerator extends DBBaseTest{

	private static String moduleName = "employee";		//某个module包名，驼峰
	private static String className = "AdminRoleSet";	//需要首字母大写
	
	private static String basePackage = "com.module";	//.xx.dao
	private static String baseFolder = "E:/workspace/eframe/eframe-sb/src/main/java/com/module/";	//xx/dao/
	
	@Test
    public void generateDaoFileCase() throws Exception{

		String content = this.generateFileStr();
		
		File daoFolder = new File(baseFolder+moduleName+"/dao/");
		if(!daoFolder.exists()){
			daoFolder.mkdirs();//比mkdir 更好，创建虚拟目录
		}
		
		File f = new File(baseFolder+moduleName+"/dao/"+className+"Dao.java");
		if(f.exists()){
			System.err.println("重建文件！");
		}
		FileUtil.writeAsString(f, content);
	}
	
	private String generateFileStr() throws Exception {
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class",
				ClasspathResourceLoader.class.getName());

		ve.init();

		Template t = ve.getTemplate("daoTemplate.vm");
		VelocityContext ctx = new VelocityContext();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDateStr = sdf.format(new Date());
		
		//首字母大写
		String daoName =className+"Dao";
		daoName = (new StringBuilder()).append(Character.toUpperCase(daoName.charAt(0))).append(daoName.substring(1)).toString();
		ctx.put("daoName", daoName);
		ctx.put("className", className);
		ctx.put("date", currentDateStr);
		ctx.put("moduleName", moduleName);
		
		String packageName = basePackage+"."+moduleName+".dao";
		ctx.put("packageName", packageName);
		ctx.put("basePackage", basePackage);
		
		StringWriter sw = new StringWriter();

		t.merge(ctx, sw);

		System.out.println("\n\n\n\n\n\n");
		
		return sw.toString();
	}
}
