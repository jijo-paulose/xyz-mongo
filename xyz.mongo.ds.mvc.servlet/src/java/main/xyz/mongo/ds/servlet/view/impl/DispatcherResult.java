package xyz.mongo.ds.servlet.view.impl;

import java.io.IOException;

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
		//那个Object怎么放入request里面呢
		//resp.setContentType(IResult.CON_TYPE_HTML);
		resp.setCharacterEncoding(IResult.CON_ENCODING_UTF_8);
		TransUtil.obj2Request(request, objs, IResult.ROOT_NAME);
		request.getRequestDispatcher(path).forward(request, resp);
	}

}
