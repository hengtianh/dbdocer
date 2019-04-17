package com.th;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 读取文件生成dom
 * @author Administrator
 *
 */
public class DocReader {
	private static String testFile = "D:/eclipse-workspace-02/dbdocer/src/main/resources/doc3.html";
	private static final Charset defaultCharset = Charset.forName("utf-8");
	/**
	 * 读取html文档
	 */
	public static Document readHtml(String filePath, String charset) throws Exception {
		File html = new File(filePath);
		if (!html.exists()) {
			throw new RuntimeException("html文件" + filePath + "不存在，请检查文件路径");
		}
		if (charset == null || charset == "") {
			charset = defaultCharset.name();
		}
		try {
			return Jsoup.parse(html, charset, "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("指定的html文件" + filePath + "解析错误");
		}
	}
	
	public static void pressDoc() {
		/*
		 * 去掉空格，换行
		 */
		Reader in = null;
		Writer out = null;
		try {
			in = new FileReader(new File(testFile));
			out = new FileWriter("C:\\Users\\Administrator\\Desktop\\test.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuilder sbuilder = new StringBuilder();
		BufferedReader reader = new BufferedReader(in);
		BufferedWriter writer = new BufferedWriter(out);
		String aLine = null;
		int count = 0;
		while(true) {
			try {
				aLine = reader.readLine();
				count++;
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (aLine == null) {
				break;
			}
			sbuilder.append(aLine);
			if (count == 10) {
				try {
					writer.write(sbuilder.toString());
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				count = 0;
			}
		}
		try {
			writer.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 获得html文件的body元素
	 * @param dom
	 * @return
	 */
	public static Element parseBody(Document dom) {
		Element body = dom.body();
		return body;
	}
	
	/**
	 * 查找指定path的元素
	 * @param elem
	 * @param path /td/a
	 * @return
	 */
	public static List<List<Elements>> getElementsByPath(Element elem, String path) {
		if (path == null || path == "") {
			return null;
		}
		List<List<Elements>> elemsList = new ArrayList<List<Elements>>();
		getRootElements(elem, path, elemsList);
		return elemsList;
	}
	
	/**
	 * 读取字段描述文件对应的表名
	 * @param list
	 * @return
	 */
	public static String getTableNameOfItem(List<Elements> list) {
		for (int i = 0; i < list.size(); i++) {
			if (i == 1) {
				Element ele = list.get(i).get(0);
				return getText(ele);
			}
		}
		return "";
	}

	/**
	 * 解析叶子节点的text内容，生成insert 语句 保存到文件
	 * @param list
	 * @param sqlCreater
	 */
	public static void parseTextOfElements(List<List<Elements>> list, ISqlCreater sqlCreater) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sql = new StringBuilder();
		int flag = 0;
		int counter = 0;
		ThreadFactory namedThreadFactory = Executors.defaultThreadFactory();
		for (List<Elements> elemsList : list) {
			flag = 0;
			for (Elements elements : elemsList) {
				if (flag != 0 && flag < elemsList.size()) {
					sb.append(",");
				}
				flag++;
				for (Element ele : elements) {
					sb.append("'")
					  .append(getText(ele))
					  .append("'");
				}
			}
//			sb.append("''");
			System.out.println(sb.toString());
			if (!",,''".equals(sb.toString()) && !"".equals(sb.toString()) && sb.toString().length() > 8) {
				sql.append(sqlCreater.createTableDefSqlStatement(sb.toString())).append("\r\n");
			}
			sb = new StringBuilder("");
			counter++;
			if (counter >= 20) {
				counter = 0;
				sqlCreater.setFileContent(sql.toString());
				namedThreadFactory.newThread(sqlCreater).start();
				sql = new StringBuilder();
			}
		}
		sqlCreater.setFileContent(sql.toString());
		namedThreadFactory.newThread(sqlCreater).start();
	}
	
	public static void getRootElements(Element elem, String path, List<List<Elements>> list) {
		String curNode = path.substring(0, path.indexOf("/"));
		Elements elems = getElements(elem, curNode);
		String lastPath = path.substring(path.indexOf(curNode) + curNode.length() + 1);
		for (Element ele : elems) {
			List<Elements> elemList = new ArrayList<Elements>();
			getElements(ele, lastPath, elemList);
			list.add(elemList);
		}
	}
	
	/**
	 * 获得指定路径的所有叶子节点
	 * @param elem
	 * @param path
	 * @param elemList
	 */
	public static void getElements(Element elem, String path, List<Elements> elemList) {
		if (path == null || path == "") {
			return;
		}
		int index = path.indexOf("/");
		if (index == -1) {
			if (elem.children().size() > 1) {
				return;
			}
			elemList.add(elem.getElementsByTag(path));
			return;
		}
		String curNode = path.substring(0, path.indexOf("/"));
		String lastPath = path.substring(path.indexOf(curNode) + curNode.length() + 1);
		for (Element ele : getElements(elem, curNode)) {
			getElements(ele, lastPath, elemList);
		}
	}
	
	/**
	 * 获得所有指定节点
	 * @param elem
	 * @param tagName
	 * @return
	 */
	public static Elements getElements(Element elem, String tagName) {
		return elem.getElementsByTag(tagName);
	}
	
	public static Elements parseElements1(Element elem, String tagName) {
		// tr
		Elements trs = elem.getElementsByTag(tagName);
		Iterator<Element> it = trs.iterator();
		// td
		Elements tds = it.next().children();
		Iterator<Element> tdi = tds.iterator();
		Element td = tdi.next();
		
		Elements ps = td.getElementsByTag("p");
		Iterator<Element> pst = ps.iterator();
		Element p = pst.next();
		String value =  p.text();
		System.out.print(value);
		return null;
	}
	/**
	 * 获取标签内的text内容
	 * @param elem
	 * @return
	 */
	public static String getText(Element elem) {
		return elem.text();
	}
	
}
