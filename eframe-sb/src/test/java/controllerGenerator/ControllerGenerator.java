package controllerGenerator;

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
import com.module.employee.entity.AdminRoleSet;

import db.DBBaseTest;

@SpringBootTest(classes = Application.class)
public class ControllerGenerator extends DBBaseTest{
	private static String moduleName = "employee";		//某个module包名，驼峰
	private static Class clazz = AdminRoleSet.class;

	private static String className = clazz.getSimpleName();	
	private static String baseFolder = "E:/workspace/eframe/eframe-sb/src/main/java/com/module/";	//xx/controller/
	
	private static String basePackage = "com.module";	//.xx.controller	
	
	
	//需要首字母大写
	private static String daoName =className+"Dao";	
	private static String serviceName =className+"Service";	
	private static String controllerName =className+"Controller";	
	static{
		daoName = (new StringBuilder()).append(Character.toUpperCase(daoName.charAt(0))).append(daoName.substring(1)).toString();		
		serviceName = (new StringBuilder()).append(Character.toUpperCase(serviceName.charAt(0))).append(serviceName.substring(1)).toString();		
		controllerName = (new StringBuilder()).append(Character.toUpperCase(controllerName.charAt(0))).append(controllerName.substring(1)).toString();		
	}
	
	
	@Test
    public void generateDaoFileCase() throws Exception{

		
		String content = this.generateFileStr();
		
		File serviceFolder = new File(baseFolder+moduleName+"/controller/");
		if(!serviceFolder.exists()){
			serviceFolder.mkdirs();//比mkdir 更好，创建虚拟目录
		}
		
		File f = new File(baseFolder+moduleName+"/controller/"+controllerName+".java");
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

		Template t = ve.getTemplate("controllerTemplate.vm");
		VelocityContext ctx = new VelocityContext();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDateStr = sdf.format(new Date());
		
		//首字母大写
		ctx.put("daoName", daoName);
		ctx.put("serviceName", serviceName);
		ctx.put("controllerName", controllerName);
		ctx.put("className", className);
		ctx.put("date", currentDateStr);
		ctx.put("moduleName", moduleName);
		
		//类名， 首字母小写
		String subModuleName = (new StringBuilder()).append(Character.toLowerCase(className.charAt(0))).append(className.substring(1)).toString();
		ctx.put("subModuleName", subModuleName);
		
		String packageName = basePackage+"."+moduleName+".controller";
		ctx.put("packageName", packageName);
		ctx.put("basePackage", basePackage);
		
		StringWriter sw = new StringWriter();

		t.merge(ctx, sw);

		return sw.toString();
	}
}
