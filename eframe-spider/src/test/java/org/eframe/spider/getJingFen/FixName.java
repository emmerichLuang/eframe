package org.eframe.spider.getJingFen;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FixName {
	
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
	
	
	static String downLoadFolder  = "F:/精分html3/";
	
	static String newName(String sourceName){
		String result = sourceName;
		for(String key : numMapper.keySet()){
			if(result.contains(key)){
				result = result.replaceAll(key, numMapper.get(key));
			}
		}
		return result;
	}
	
	//
	public static void main(String[] args) {
		File folder = new File(downLoadFolder);
		for(File f : folder.listFiles()){
			String name = f.getName();
			
			/*String newName = newName(name);
			if(StringUtils.equalsIgnoreCase(newName, name)){
				continue;
			}
			System.err.println(name+" --> "+newName);
			f.renameTo(new File("F:/精分html/"+newName));*/
			
			
			/*if(!name.contains("十")){
				continue;
			}
			
			if(name.contains("第十")){
				String newName = name.replaceFirst("第十", "第1");
				System.err.println(name+" --> "+newName);
				f.renameTo(new File("F:/精分html/"+newName));
				continue;
			}
			
			if(name.contains("十")){
				String newName = name.replaceFirst("十", "0");
				System.err.println(name+" --> "+newName);
				f.renameTo(new File("F:/精分html/"+newName));
			}*/
			
			//chapter_  --> Section
			if(name.contains("chapter_")){
				String newName = name.replaceFirst("chapter_", "Section");
				f.renameTo(new File(downLoadFolder+newName));
				continue;
			}
		}
	}

}
