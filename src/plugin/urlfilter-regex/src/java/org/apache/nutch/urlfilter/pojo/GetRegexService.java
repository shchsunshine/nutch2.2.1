package org.apache.nutch.urlfilter.pojo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.nutch.urlfilter.dao.RGUtils;


public class GetRegexService {
	private static Map<String, String> url_regex = new ConcurrentHashMap<String, String>();
	/**
	 * 隔多久从数据库更新数据
	 */
	private static long update_time  = 2 * 60 * 60 * 1000;
	/**
	 * 最后更新时间
	 */
	private static Long end_update_time = System.currentTimeMillis();
	
	private static GetRegexService service ;
	
	public static GetRegexService getInstance() {
		
		if(service == null) {
			service = new GetRegexService();
		}	
		return service;		
	}
	private GetRegexService() {
		rgUtils = new RGUtils();
	}
	private RGUtils rgUtils ;
    public  String getRegex(){
    	Connection conn =rgUtils.getConn();	
    	if(update_time + end_update_time < System.currentTimeMillis()) {
    		url_regex.clear();
			end_update_time = System.currentTimeMillis();
		}
    	if(url_regex.containsKey("regex"))
    	{
    		return url_regex.get("regex");
    	}
		  PreparedStatement sta =null;
		  StringBuffer sb = new StringBuffer();
		  sb.append("-^(file|ftp|mailto):");
		  sb.append("\r\n");	
		  sb.append("-\\.(gif|GIF|jpg|JPG|png|PNG|ico|ICO|css|CSS|sit|SIT|eps|EPS|wmf|WMF|zip|ZIP|ppt|PPT|mpg|MPG|xls|XLS|gz|GZ|rpm|RPM|tgz|TGZ|mov|MOV|exe|EXE|jpeg|JPEG|bmp|BMP|js|JS)$");
		  sb.append("\r\n");	
		  sb.append("-.*(/[^/]+)/[^/]+\1/[^/]+\1/");
		  sb.append("\r\n");	
		  try { 
	 	   String sql="select filter  from  info_reactor_filter";
	 	   sta =conn.prepareStatement(sql); 
	 	   ResultSet re=sta.executeQuery();
	 	   while(re.next())   
	 		{
	 		   String text =re.getString(1); 
	 		   sb.append(text);
			   sb.append("\r\n");	
	 		} 
	 	   //re.close();
	   } catch(Exception ex) {
	    ex.printStackTrace(System.out); 
	    return sb.toString();
	   }
	   finally {	
		try {			
			if(conn != null && !conn.isClosed()){
					conn.close();					
					}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();}
		}	
		  url_regex.put("regex", sb.toString());
		  return sb.toString();   
	  }
    public static void main(String[] args) {
    	GetRegexService ss= new GetRegexService();
    	System.out.println(ss.getRegex());
	}
}

