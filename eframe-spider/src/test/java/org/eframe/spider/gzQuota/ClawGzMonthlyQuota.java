package org.eframe.spider.gzQuota;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.eframe.spider.httpclient.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ClawGzMonthlyQuota {

	static String issueNumber = "201606";

	static String resultPath = "F:/result.txt";
	
	/**
	 * 用httpClient +jsoup抓
	 * 
	 * @param args
	 * @param
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		long begin = System.currentTimeMillis();
		List<String> result = new ArrayList<String>();
		result.add("个人指标配置结果查询：");

		String url = "http://apply.gzjt.gov.cn/apply/norm/personQuery.html";
		// step1:得知一共多少页。
		CloseableHttpClient client = HttpUtil.getHttpClient(false);

		String firstContent = HttpUtil.get(client, url);
		// System.err.println(content);

		Document doc = Jsoup.parse(firstContent);
		Elements elements = doc.select("span.f_orange");
		Element e = elements.get(0);
		// e.ownText();
		// e.text();
		// 一共这么多页数据
		Integer pageCount = Integer.parseInt(e.ownText());

		for (Integer i = 1; i <= pageCount; i++) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("pageNo", i.toString());
			params.put("issueNumber", issueNumber);

			// System.err.println("第"+i+"页数据~");
			String pageContent = HttpUtil.post(client, url, params);
			Document pageDoc = Jsoup.parse(pageContent);

			Elements ele = pageDoc.select("td[style*=border-bottom]");
			for (int j = 0; j < ele.size(); j += 2) {
				// System.err.println("编号："+ele.get(j).ownText() +
				// "  人："+ele.get(j+1).ownText());
				result.add("编号：" + ele.get(j).ownText() + "  人："
						+ ele.get(j + 1).ownText());
			}

		}

		FileUtils.writeLines(new File(resultPath), "utf-8", result);
		
		// 输出结果：
		for (String str : result) {
			System.err.println(str);
		}
		System.err.println("耗时：" + (System.currentTimeMillis() - begin));
	}

}
