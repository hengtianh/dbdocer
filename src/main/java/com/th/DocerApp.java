package com.th;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DocerApp {
	public static String path = "C:/Users/Administrator/Desktop/nc63系统资料/租赁核心业务系统NC6_files/";
	
	public static String firstFile = "租赁核心3.htm";
	
	public static int num = 14;

	public static void main2(String[] args) throws Exception {
		// TODO 数据库文档处理启动类
		String file = "";
		for (int i = 0; i < num; i++) {
			if (i == 0) {
				file = path + firstFile;
			} else {
				String[] apart = firstFile.split("\\.");
				file = path + apart[0] + "_" + i + "." + apart[1];
			}
			System.out.println(file);
			Document dom = DocReader.readHtml(file, "GB2312");
			Element elem = DocReader.parseBody(dom);
			// tr/td/p
			DocReader.parseTextOfElements(DocReader.getElementsByPath(elem, "tr/td/p/a"), new TableInfoSqlCreater());
		}
//		testPressDoc();
	}
	
	public static String concatFileName(String file, int i) {
		String finalFile = "";
		if (i == 0) {
			finalFile = path + file;
		} else {
			String[] apart = file.split("\\.");
			finalFile = path + apart[0] + "_" + i + "." + apart[1];
		}
		return finalFile;
	}
	
	public static void main(String ... args) {
		String firstFile = "租赁核心4.htm";
		parseTableItem(firstFile);
	}
	
	public static void parseTableItem(String file) {
		String firstFile = file;
		List<Document> domList = new ArrayList<Document>();
		for (int i = 0; i < num; i++) {
			String fileName = concatFileName(firstFile, i);
			// 探测同一个表是否还有其他描述文件
			try {
				domList.add(DocReader.readHtml(fileName, "GB2312"));
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		String tableName = "";
		for (int i = 0; i < domList.size(); i++) {
			Element elem = DocReader.parseBody(domList.get(i));
			// tr/td/p
			List<List<Elements>> lists = DocReader.getElementsByPath(elem, "tr/td/p");
			if (i == 0) {
				tableName = parseHead(domList.get(i));
				// 移除标题及表头的描述内容，remove后下标重排，所以逆向移除
				lists.remove(3);
				lists.remove(2);
				lists.remove(1);
				lists.remove(0);
			} else {
				// 第一页以后的内容只用去除表头内容
				lists.remove(0);
			}			
			DocReader.parseTextOfElements(lists, new TableItemSqlCreater(tableName));
		}
		
	}
	
	public static String parseHead(Document dom) {
		Element elem = DocReader.parseBody(dom);
		List<List<Elements>> list = DocReader.getElementsByPath(elem, "tr/td/p");
		String value = DocReader.getTableNameOfItem(list.get(1));
		System.out.println(value);
		return value;
	}
	
	public static void testPressDoc() {
		DocReader.pressDoc();
	}

}
