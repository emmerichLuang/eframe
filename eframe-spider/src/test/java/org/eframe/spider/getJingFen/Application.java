package org.eframe.spider.getJingFen;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.eframe.core.constant.Encoding;
import org.eframe.util.FileUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 抓精分写手成神记 全文， 并且下载到本地。
 * F:\精分\具体章节名称一个txt。
 * @Date 2018年9月30日
 * @author E.E.
 *
 */
public class Application {
	
	static String baseUrl = "https://www.zhuaji.org/read/23773/";
	
	static File allChapter = new File("E:/workspace/eframe/eframe-spider/src/test/java/org/eframe/spider/getJingFen/all2.txt");
	
	static String downLoadFolder  = "F:/精分html3/";
	
	static Map<String,String> numMapper = new HashMap<String,String>();
	static{
		numMapper.put("〇", "0");
		numMapper.put("一", "1");
		numMapper.put("二", "2");
		numMapper.put("三", "3");
		numMapper.put("四", "4");
		numMapper.put("五", "5");
		numMapper.put("六", "6");
		numMapper.put("七", "7");
		numMapper.put("八", "8");
		numMapper.put("九", "9");
	}
	
	/**
	 * 所有的
	 * @return
	 * @throws Exception
	 */
	static List<String> getAll() throws Exception{
		List<String> all = FileUtil.readLine(allChapter, Encoding.UTF_8);
		return all;
	}
	
	/**
	 * 继续下载
	 * @param all
	 * @return
	 * @throws Exception
	 */
	static List<String> todoList(List<String> all) throws Exception{
		
		if(StringUtils.isEmpty(beginString) || CollectionUtils.isEmpty(all)){
			return all;
		}
		
		List<String> todoList = new ArrayList<String>();
		boolean flag = false;	
		for(String temp:all){
			if(StringUtils.equals(beginString, temp)){
				System.err.println(String.format("从 [%s] 开始。 ", beginString));
				flag=true;
			}
			
			if(flag==true){
				todoList.add(temp);
			}
		}
		
		return todoList;
	}
	
	/**
	 * 转为数字， 补0。一千多章，0000
	 * @param sourceName
	 * @return
	 */
	static String newName(String sourceName){

		if(StringUtils.equals("最终章 那之后，浦新宙", sourceName)||
				StringUtils.equals("后记与彩蛋", sourceName)){
			return sourceName;
		}
		
		//错字 begin
		if(sourceName.contains("第吃")){
			sourceName = sourceName.replaceFirst("吃", "");
		}
		if(sourceName.contains("第21227章")){
			sourceName = sourceName.replaceFirst("第21227章", "第1227章");
		}
		if(sourceName.contains("第91195章")){
			sourceName = sourceName.replaceFirst("第91195章", "第1195章");
		}
		if(sourceName.contains("第11160章")){
			sourceName = sourceName.replaceFirst("第11160章", "第1160章");
		}
		if(sourceName.contains("第11641章")){
			sourceName = sourceName.replaceFirst("第11641章", "第1641章");
		}
		if(sourceName.contains("第11898章")){
			sourceName = sourceName.replaceFirst("第11898章", "第1898章");
		}
		//错字 end		

		int idx = sourceName.indexOf("章");
		String prefix = sourceName.substring(1, idx);
		
		//String posFix = sourceName.substring(idx+1, sourceName.length());
		
		for(String key : numMapper.keySet()){
			if(prefix.contains(key)){
				prefix = prefix.replaceAll(key, numMapper.get(key));
			}
		}
		
		
		//第十x章
		if(prefix.startsWith("十")){
			prefix = prefix.replaceFirst("十", "1");
		}
		
		//第x十章
		if(prefix.endsWith("十")){
			prefix = prefix.replaceFirst("十", "0");
		}
		
		//第x十x章
		if(prefix.contains("十")){
			prefix = prefix.replaceFirst("十", "");
		}
		
		int chapterNum = Integer.parseInt(prefix);
		
		if(chapterNum>2000){
			throw new RuntimeException("sourceName:"+sourceName +" 错字");
		}
		
		//String result = String.format("%04d", chapterNum)+posFix;
		String result = String.format("%04d", chapterNum);
		return "chapter_"+result;
	}

	static void downChapter(VelocityEngine ve, String chapterUrl) throws Exception{
		Document doc = Jsoup.connect(baseUrl+chapterUrl).get();
		
		Elements content = doc.select("div#content");
		Elements title = doc.select("h1");
		
		//标题 换行 然后全文内容
		String finalContent = content.html();
		
		String newName = newName(title.text());
		
		Template t = ve.getTemplate("template.xhtml");
		VelocityContext ctx = new VelocityContext();
		ctx.put("title", title.text());
		ctx.put("content", content.html());

		StringWriter sw = new StringWriter();
		t.merge(ctx, sw);

		finalContent = sw.toString();
		
		FileUtils.writeStringToFile(new File(downLoadFolder+newName+".xhtml"), finalContent, "utf-8");
		System.err.println("下载完成： "+chapterUrl+"  保存名字："+newName);
	}
	
	static String beginString = "16456332.html";
	
	public static void main(String[] args) throws Exception {
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class",
				ClasspathResourceLoader.class.getName());		
		
		//downChapter("18983369.html");
		
		Long begin = System.currentTimeMillis();
		List<String> all = getAll();
		all = todoList(all);
		if(beginString!=null){
			System.err.println(String.format("耗时:%s, 从%s开始,剩余%s。 ",(System.currentTimeMillis()-begin), beginString, all.size()));			
		}else{
			System.err.println(String.format("耗时:%s, 剩余%s。 ",(System.currentTimeMillis()-begin), all.size()));
		}

		//顺序下载
		for(String temp:all){
			downChapter(ve, temp);
		}
		
	}
	
	

}
