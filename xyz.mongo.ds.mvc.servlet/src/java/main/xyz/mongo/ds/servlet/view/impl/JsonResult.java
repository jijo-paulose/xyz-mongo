package xyz.mongo.ds.servlet.view.impl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xyz.mongo.ds.servlet.utils.TransUtil;
import xyz.mongo.ds.servlet.view.IResult;

public class JsonResult implements IResult{
    /**
     * json格式,配合javascript解析使用
     */
	@Override
	public void process(HttpServletRequest request, HttpServletResponse resp,
			String path, Object objs)throws IOException,ServletException {
		//那个Object怎么放入request里面呢
		resp.setContentType(IResult.CON_TYPE_JSON);
		resp.setCharacterEncoding(IResult.CON_ENCODING_UTF_8);
		Object json=TransUtil.obj2Json(objs, null==path?IResult.ROOT_NAME:path);
		Writer writer = new OutputStreamWriter(resp.getOutputStream(),IResult.CON_ENCODING_UTF_8);
		writer.write(json.toString());
		writer.flush();
	}

}
