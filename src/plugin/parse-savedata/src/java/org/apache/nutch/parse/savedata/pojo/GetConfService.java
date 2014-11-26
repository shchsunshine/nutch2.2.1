package org.apache.nutch.parse.savedata.pojo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.nutch.parse.savedata.dao.SDUtils;
import org.apache.nutch.parse.savedata.pojo.GetConfService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetConfService {
	
	private static Map<String, String> url_conf = new ConcurrentHashMap<String, String>();
	/**
	 * 隔多久从数据库更新数据
	 */
	private static long update_time  = 2 * 60 * 60 * 1000;
	/**
	 * 最后更新时间
	 */
	private static Long end_update_time = System.currentTimeMillis();
	
	private static GetConfService service ;
	
	public static GetConfService getInstance() {
		
		if(service == null) {
			service = new GetConfService();
		}	
		return service;		
	}
	
	private GetConfService() {
		sdUtils = new SDUtils();
	}
	
	private SDUtils sdUtils ;
	
	/**
	 * 插入一条nutch爬取数据
	 * @param sd
	 */
	@SuppressWarnings("rawtypes")
	public String getConf(String url) {

		if(update_time + end_update_time < System.currentTimeMillis()) {
			url_conf.clear();
			end_update_time = System.currentTimeMillis();
		}
		Iterator it = url_conf.entrySet().iterator();
		while(it.hasNext()){
			Entry entry = (Entry)it.next();
			String a1=(String) entry.getKey() ;
			Pattern p = Pattern.compile(a1); 
			Matcher matcher = p.matcher(url); 
			if (matcher.find()){		    	
				  return url_conf.get(a1);
			  }	   
		  }
		PreparedStatement sta=null,sta1=null;
		String conf =null;
		try { 
		 String sql="select filter  from  info_reactor_filter";
		 sta = sdUtils.getConn().prepareStatement(sql);
		   ResultSet re=sta.executeQuery();
		   while(re.next())   
			{
			   String reg=re.getString(1);
		    	 Pattern p = Pattern.compile(reg.substring(2, reg.length())); 
				 Matcher matcher = p.matcher(url); 
				  if (matcher.find()){	
					  String sql1="select conf from info_reactor_filter where filter=? ";	
					  sta1 = sdUtils.getConn().prepareStatement(sql1);
					  sta1.setString(1, reg);
					  ResultSet re1=sta1.executeQuery();
					    while(re1.next())
					    {
					    	conf=re1.getString(1);
					    	url_conf.put(reg.substring(2, reg.length()), conf);			    	
					    	break;
					    }	    
					    return conf ;
				  }
			}
	  } catch(Exception ex) {
		  ex.printStackTrace(System.out);   
		  return null;
	  }
		finally {	
			try {
				
				if(sta1 != null && !sta1.isClosed())
				{
					sta1.close();}
				if(sta != null && !sta.isClosed())
				{
					sta.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}			
		return null;	
	} 
	public Document getDoc(String url) throws IOException
	{
		String conf=getConf(url);
		if(conf!=null){
			InputStream is = new ByteArrayInputStream(conf.getBytes());
			Document doc=Jsoup.parse(is,"utf-8","");
			return doc;		
		}
		return null;
	}
	
	public Map<String,String> getPubtime(String url) throws IOException
	{
		 Document doc=getDoc(url);
		 Map<String, String> map=new HashMap<String, String>();
		 Elements pubtimess = doc.select("url>pubtime");
		  for(Element pubtimes:pubtimess)
		  {
			  String pubtime1=pubtimes.attr("regex");
			  if(pubtime1!=null)
			  {		 
			 	String pubtime2=pubtimes.attr("xpath");
			  if(pubtime2!=null)
			   {
				 map.put(pubtime1, pubtime2); 
			   }
			  }
		  }	
		  return map;
	}
	public Map<String,String> getBrowsenum(String url) throws IOException
	{
		 Document doc=getDoc(url);
		 Map<String, String> map=new HashMap<String, String>();
		 Elements browsenumss = doc.select("url>browsenum");
		  for(Element browsenums:browsenumss)
		  {
			  String browsenum1=browsenums.attr("regex");
			  if(browsenum1!=null)
			  {		 
			 	String browsenum2=browsenums.attr("xpath");
			  if(browsenum2!=null)
			   {
				 map.put(browsenum1, browsenum2); 
			   }
			  }
		  }	
		  return map;
	}
	public Map<String,String> getCommentnum(String url) throws IOException
	{
		Document doc=getDoc(url);
		 Map<String, String> map=new HashMap<String, String>();
		 Elements commentnumss = doc.select("url>commentnum");
		  for(Element commentnums:commentnumss)
		  {
			  String commentnum1=commentnums.attr("regex");
			  if(commentnum1!=null)
			  {		 
			 	String commentnum2=commentnums.attr("xpath");
			  if(commentnum2!=null)
			   {
				 map.put(commentnum1, commentnum2); 
			   }
			  }
		  }	
		  return map;
	}
	public Map<String,String> getInfotype(String url) throws IOException
	{
		Document doc=getDoc(url);
		 Map<String, String> map=new HashMap<String, String>();
		 Elements infotypess = doc.select("url>infotype");
		  for(Element infotypes:infotypess)
		  {
			  String infotype1=infotypes.attr("regex");
			  if(infotype1!=null)
			  {		 
			 	String infotype2=infotypes.attr("xpath");
			  if(infotype2!=null)
			   {
				 map.put(infotype1, infotype2); 
			   }
			  }
		  }	
		  return map;
	}
	public String getSite(String url) throws IOException
	{
		Document doc=getDoc(url);
		 String site = doc.select("url").val();	
		 return site;
	}
	
 public static void main(String[] args) throws IOException {
	GetConfService gs= new GetConfService();
	System.out.println(gs.getDoc("http://www.ledth.com/"));
}
	
	
}
