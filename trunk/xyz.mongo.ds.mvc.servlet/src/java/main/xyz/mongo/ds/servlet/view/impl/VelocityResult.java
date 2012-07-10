package xyz.mongo.ds.servlet.view.impl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import xyz.mongo.ds.servlet.utils.TransUtil;
import xyz.mongo.ds.servlet.view.IResult;

public class VelocityResult implements IResult{
	
	private VelocityEngine velocity;
	
	public void setFilePath(String filePath){
		velocity = new VelocityEngine();
		Properties properties = new Properties();
		//request.getRealPath("/"),这个可能要在Servlet init里面去做呢
		properties.setProperty("file.resource.loader.path", filePath);
		properties.setProperty("file.resource.loader.cache", "true");
		properties.setProperty("input.encoding", "UTF-8");
		properties.setProperty("output.encoding", "UTF-8");
		velocity.init(properties);
	}
    /**
     * 一般内容从直接地方取,为了获取session和application,放了xyzSession和xyzApp
     */
	@Override
	public void process(HttpServletRequest request, HttpServletResponse resp,
			String path, Object objs)throws IOException,ServletException {
		resp.setCharacterEncoding(IResult.CON_ENCODING_UTF_8);
		Template template = velocity.getTemplate(path,IResult.CON_ENCODING_UTF_8);
		Writer writer = new OutputStreamWriter(resp.getOutputStream(),IResult.CON_ENCODING_UTF_8);
		Context context=new VelocityContext();
		TransUtil.obj2Context(request, context, objs, IResult.ROOT_NAME);
		template.merge(context, writer);
		writer.flush();
	}


}
