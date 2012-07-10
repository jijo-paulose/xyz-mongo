package xyz.mongo.ds.servlet.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IResult {
	public static final String ROOT_NAME="root";
	public static final String CON_TYPE_HTML="text/html";
	public static final String CON_TYPE_JS="text/javascript";//jsonp
	public static final String CON_TYPE_JSON="application/json";
	public static final String CON_ENCODING_UTF_8="UTF-8";
	
   void process(HttpServletRequest request,HttpServletResponse resp,String path,Object objs)throws IOException,ServletException ;
}
