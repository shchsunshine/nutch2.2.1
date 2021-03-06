package org.apache.nutch.parse.savedata.pojo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.nutch.parse.savedata.dao.SDUtils;
import org.apache.nutch.parse.savedata.pojo.CrawlData;
import org.apache.nutch.parse.savedata.pojo.SaveDataService;

public class SaveDataService {

	private static SaveDataService service ;
	
	public static SaveDataService getInstance() {
		
		if(service == null) {
			service = new SaveDataService();
		}
		
		return service;
		
	}
	
	private SaveDataService() {
		sdUtils = new SDUtils();
	}
	
	private SDUtils sdUtils ;
	
	/**
	 * 插入一条nutch爬取数据
	 * @param sd
	 */
	public void addData(CrawlData sd) {
		Connection conn =sdUtils.getConn();	
		PreparedStatement  state = null,sta=null,state1 = null,sta1=null;
		try {	
			String sql1="select url from info_reactor_micro p where p.url=?";
			 sta1 = sdUtils.getConn().prepareStatement(sql1);
			    sta1.setString(1, sd.getUrl());
			    ResultSet re1=sta1.executeQuery();
			    while(re1.next())	
			    {
			    	System.out.println(re1.getString(1));
			    	String sql = "update  info_reactor_micro set fetchtime=? ,title=? , maintext=?,snap=?,pubtime=?,site=?,infotype=?,browsenum=?,commentnum=? ,siteid=? where url=?" ;		
			    	state1 = conn.prepareStatement(sql);	
			    	//state.setString(1, sd.getUrl());
			    	state1.setTimestamp(1, new Timestamp(sd.getFetchtime().getTime()));
			    	state1.setString(2, sd.getTitle());
			    	state1.setString(3, sd.getMaintext());
			    	state1.setString(4, sd.getSnap());
			    	state1.setTimestamp(5,new Timestamp( sd.getPubtime().getTime()));
			    	state1.setString(6,sd.getSite());
			    	state1.setString(7,sd.getInfotype());
			    	state1.setInt(8,sd.getBrowsenum());
			    	state1.setInt(9,sd.getCommentnum());
			    	state1.setInt(10, sd.getSiteid());
			    	state1.setString(11, sd.getUrl());
			    //	state.execute();
			    	int row=state1.executeUpdate();
			    	if(row>0)
			    	{		    		
			    	System.out.println("Update Successfully!");
			    	}
			    	 break;
			    }
			sta = conn.prepareStatement(sql1);
		    sta.setString(1, sd.getUrl());
		    ResultSet re=sta.executeQuery();
		    
		    while(!re.next())	
		    {
			String sql = "insert into info_reactor_micro(userid,url ,fetchtime ,title ,maintext,snap,pubtime,site,infotype,browsenum,commentnum,siteid) values(187,?,?,?,?,?,?,?,?,?,?,?)" ;		    			
		    	state = conn.prepareStatement(sql);	
		    	state.setString(1, sd.getUrl());
		    	state.setTimestamp(2, new Timestamp(sd.getFetchtime().getTime()));
		    	state.setString(3, sd.getTitle());
		    	state.setString(4, sd.getMaintext());
		    	state.setString(5, sd.getSnap());
		    	state.setTimestamp(6,new Timestamp( sd.getPubtime().getTime()));
		    	state.setString(7,sd.getSite());
		    	state.setString(8,sd.getInfotype());
		    	state.setInt(9,sd.getBrowsenum());
		    	state.setInt(10,sd.getCommentnum());
		    	state.setInt(11,sd.getSiteid());
		    //	state.execute();
		    	int row=state.executeUpdate();
		    	if(row>0)
		    	{		    		
		    	System.out.println("Insert Successfully!\n");
		    	}
		    	 break;
		    }
		   
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally {	
			try {
				
				if(conn != null && !conn.isClosed())
				{
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	} 
 
	/*public void insertAll(Collection<CrawlData> list) {
		
		String sql = "insert into savedata(url ,fetchtime ,title , maintext,snap,pubtime) values(?,?,?,?,?,?)" ;
		
		PreparedStatement  state = null;
		try {
			Connection conn = sdUtils.getConn();
			conn.setAutoCommit(false);
			
			for(CrawlData sd : list) {
				
				state = conn.prepareStatement(sql);
				 
				
				state.setString(1, sd.getUrl());
				state.setString(2, sd.getFetchtime());
				state.setString(3, sd.getTitle());
				state.setString(4, sd.getMaintext());
				state.setString(5, sd.getSnap());
				state.setString(6, sd.getPubtime());
				
			
				state.execute();
			}
			 
			conn.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			try {
				if(state != null && !state.isClosed())
				{
					state.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}
	 */
	
}

