package xyz.mongo.ds.servlet.utils;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.context.Context;

import xyz.mongo.ds.servlet.view.IResult;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public final class TransUtil {
   private TransUtil(){
	   throw new RuntimeException("can't invoke");
   }
   
   public static DBObject obj2Json(Object obj,String rootName){
	   //DBObject to=new BasicDBObject();
	   if(obj instanceof DBObject){
		   return (DBObject)obj;
	   }else{//这里list
		   if(obj instanceof List){
				BasicDBList to=new BasicDBList();
				List<DBObject> from=(List<DBObject>)obj;
				to.addAll(from);
				return to;
			}else{
				DBObject to=new BasicDBObject();
				to.put(rootName, to);
				return to;
			}
	   }
   }
   
   public static void obj2Request(HttpServletRequest request,Object obj,String rootName){
	   //DBObject to=new BasicDBObject();
	   if(obj instanceof DBObject){
		   DBObject dbo=(DBObject)obj;
		   for(String key:dbo.keySet()){
			   request.setAttribute(key, dbo.get(key));
			   System.out.println("key");
		   }
	   }else{//这里list无用
		  request.setAttribute(rootName, obj);
	   }
   }
   
   public static void obj2Context(HttpServletRequest request,Context context,Object obj,String rootName){
	   if(obj instanceof DBObject){
		   DBObject dbo=(DBObject)obj;
		   for(String key:dbo.keySet()){
			   context.put(key, dbo.get(key));
		   }
	   }else{
		   context.put(rootName, obj);
	   }
	   //session
	   HttpSession session=request.getSession();
	   Enumeration enums=session.getAttributeNames();
	   if(enums.hasMoreElements()){
		   context.put("xyzSession", session);
	   }
	   //app
	   ServletContext app=request.getSession().getServletContext();
	   enums=app.getAttributeNames();
	   if(enums.hasMoreElements()){
		   context.put("xyzApp", app);
	   }
   }
   
   public static void obj2Map(HttpServletRequest request,Map map,Object obj,String rootName){
	   if(obj instanceof DBObject){
		   DBObject dbo=(DBObject)obj;
		   for(String key:dbo.keySet()){
			   map.put(key, dbo.get(key));
		   }
	   }else{
		   map.put(rootName, obj);
	   }
	   //session
	   HttpSession session=request.getSession();
	   Enumeration enums=session.getAttributeNames();
	   if(enums.hasMoreElements()){
		   map.put("xyzSession", session);
	   }
	   //app
	   ServletContext app=request.getSession().getServletContext();
	   enums=app.getAttributeNames();
	   if(enums.hasMoreElements()){
		   map.put("xyzApp", app);
	   }
   }
   
   public static DBObject request2DBObject(HttpServletRequest request){
	   DBObject to=new BasicDBObject();
	   //attribute
	   Enumeration enums=request.getAttributeNames();
	   while(enums.hasMoreElements()){
		   String key=(String)enums.nextElement();
		   if(-1==key.indexOf(".")){
				to.put(key, request.getAttribute(key));
			}else{
				DBObject will=to;
				String[] strs=key.split("\\.");
				for(int i=0,len=strs.length;i<len;i++){
					if(len-1==i){
						will.put(strs[i],request.getAttribute(key));
					}else{
						if(will.containsField(strs[i])){
							will=(DBObject)will.get(strs[i]);
						}else{
							DBObject willTo=new BasicDBObject();
							will.put(strs[i], willTo);
							will=willTo;
						}
					}
				}
			}
	   }
	 //parameters
	   enums=request.getParameterNames();
	   while(enums.hasMoreElements()){
		   String key=(String)enums.nextElement();
		   if(-1==key.indexOf(".")){
				to.put(key, request.getParameter(key));
			}else{
				DBObject will=to;
				String[] strs=key.split("\\.");
				for(int i=0,len=strs.length;i<len;i++){
					if(len-1==i){
						will.put(strs[i],request.getParameter(key));
					}else{
						if(will.containsField(strs[i])){
							will=(DBObject)will.get(strs[i]);
						}else{
							DBObject willTo=new BasicDBObject();
							will.put(strs[i], willTo);
							will=willTo;
						}
					}
				}
			}
	   }
	   //session
	   HttpSession session=request.getSession();
	   enums=session.getAttributeNames();
	   if(enums.hasMoreElements()){
		   DBObject toSession=new BasicDBObject();
		   while(enums.hasMoreElements()){
			   String key=(String)enums.nextElement();
			   if(-1==key.indexOf(".")){
					toSession.put(key, session.getAttribute(key));
				}else{
					DBObject will=toSession;
					String[] strs=key.split("\\.");
					for(int i=0,len=strs.length;i<len;i++){
						if(len-1==i){
							will.put(strs[i],session.getAttribute(key));
						}else{
							if(will.containsField(strs[i])){
								will=(DBObject)will.get(strs[i]);
							}else{
								DBObject willTo=new BasicDBObject();
								will.put(strs[i], willTo);
								will=willTo;
							}
						}
					}
				}
		   }
		   to.put("xyzSession", toSession);
	   }
	   //application
	   ServletContext app=request.getSession().getServletContext();
	   enums=app.getAttributeNames();
	   if(enums.hasMoreElements()){
		   DBObject toApp=new BasicDBObject();
		   while(enums.hasMoreElements()){
			   String key=(String)enums.nextElement();
			   if(-1==key.indexOf(".")){
				   toApp.put(key, app.getAttribute(key));
				}else{
					DBObject will=toApp;
					String[] strs=key.split("\\.");
					for(int i=0,len=strs.length;i<len;i++){
						if(len-1==i){
							will.put(strs[i],app.getAttribute(key));
						}else{
							if(will.containsField(strs[i])){
								will=(DBObject)will.get(strs[i]);
							}else{
								DBObject willTo=new BasicDBObject();
								will.put(strs[i], willTo);
								will=willTo;
							}
						}
					}
				}
		   }
		   to.put("xyzApp", toApp);
	   }
	   
	   return to;
   }
}
