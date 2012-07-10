package xyz.mongo.ds.servlet.view.impl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xyz.mongo.ds.servlet.utils.TransUtil;
import xyz.mongo.ds.servlet.view.IResult;

public class DispatcherResult implements IResult{
    /**
     * 由于没有设定ContentType,需要再jsp页面设置,这样可以实现html,json,jsonp等
     */
	@Override
	public void process(HttpServletRequest request, HttpServletResponse resp,
			String path, Object objs)throws IOException,ServletException {
		//int index=path.lastIndexOf(".");
		//path=path.substring(0,index+1)+"jsp";
		System.out.println("path 4 jsp:"+path);
		//那个Object怎么放入request里面呢
		resp.setContentType(IResult.CON_TYPE_HTML);
		resp.setCharacterEncoding(IResult.CON_ENCODING_UTF_8);
		TransUtil.obj2Request(request, objs, IResult.ROOT_NAME);
		//PageContext pageContext=request.
		try {
			RequestDispatcher dispatcher = request.getRequestDispatcher(path);
            
			System.err.println("dispatcher :"+dispatcher);
			System.err.println("now not send redirect no req put new :"+path);
			if(null==dispatcher){
				throw new RuntimeException("error path!");
			}
			dispatcher.forward(request, resp);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*System.out.println("parth--"+path);
		//request.getRequestDispatcher("/page/userAddResult.jsp").forward(request, resp);
		request.getRequestDispatcher(path).forward(request, resp);*/
		
	}

}
