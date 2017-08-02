package yk;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SavePic {
	
	public static void main(String[] args) throws Exception{
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://download.csdn.net/book_list-20");
		httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");
		
		CloseableHttpResponse response=httpClient.execute(httpGet);
		HttpEntity entity=response.getEntity();
		String content = EntityUtils.toString(entity, "utf-8");
		response.close();
		httpClient.close();
		
		Document document = Jsoup.parse(content);
		
		Elements elements = document.select("img[src]");
		for(Element element : elements){
			Thread.sleep(1000);
			String url = element.attr("src");		// 获取某图片地址url，有一些url是相对地址，需要进行一些处理才能获取图片
			try {									
				Get(url);
			} catch (Exception e) {
				try {
					Get("http://download.csdn.net"+url);
				} catch (Exception e1) {
					System.out.println("仍有异常！"+url);
				}
			}
		}
	}
	
	public static void Get(String url) throws Exception{
		CloseableHttpClient httpClient=HttpClients.createDefault(); 
		HttpGet httpGet=new HttpGet(url); 
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
		CloseableHttpResponse response=httpClient.execute(httpGet); 
		HttpEntity entity=response.getEntity(); 
		if(entity!=null){
			System.out.println("ContentType:"+entity.getContentType().getValue()+"--"+url);
			InputStream inputStream=entity.getContent();
			int lastIndexOf = url.lastIndexOf(".");
			String end = url.substring(lastIndexOf);	// .jpg .png .gif等
			FileUtils.copyToFile(inputStream, new File("F://"+System.currentTimeMillis()+end));
		}
		response.close(); 
		httpClient.close(); 
	}
}
