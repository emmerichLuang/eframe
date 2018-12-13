package org.eframe.spider.contentType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @Date 2018年11月14日
 * @author E.E.
 *
 */
public class Application {

	static String url = "http://tool.oschina.net/commons/";
	
	/**
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Document doc = Jsoup.connect(url).get();
		
		//separateColor
		Elements posfixs = doc.select("td.separateColor");
		
		String sql = "INSERT INTO content_type_mapper (pos_fix,content_type,type) VALUES ('%s','%s','%s');";
		for(Element el :posfixs){
			Element elNext = el.nextElementSibling();
			//System.err.println(el.html()+"','"+elNext.html());
			System.err.println(String.format(sql, el.html(), elNext.html(),1));
		}
		
	}

}
