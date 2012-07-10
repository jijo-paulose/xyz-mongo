package xyz.mongo.ds.servlet.view.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xyz.mongo.ds.servlet.utils.TransUtil;
import xyz.mongo.ds.servlet.view.IResult;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerResult implements IResult{
	private static final Log LOG=LogFactory.getLog(FreeMarkerResult.class);
	
	private Configuration config;
	
	public void setFilePath(String filePath){
		config=new Configuration();
		try {
			TemplateLoader templatePathLoader=new FileTemplateLoader(new File(filePath));
			config.setTemplateLoader(templatePathLoader);
			config.setDefaultEncoding(IResult.CON_ENCODING_UTF_8);
		} catch (Throwable e) {
			LOG.error("error while conf freemarker", e);
		}
	}
	/**
	 * 一般内容从直接地方取,为了获取session和application,放了xyzSession和xyzApp
	 */
	@Override
	public void process(HttpServletRequest request, HttpServletResponse resp,
			String path, Object objs)throws IOException,ServletException {
		//那个Object怎么放入request里面呢
		resp.setCharacterEncoding(IResult.CON_ENCODING_UTF_8);
		Template template=config.getTemplate(path, IResult.CON_ENCODING_UTF_8);
		Map<String, Object> params=new HashMap();
		TransUtil.obj2Map(request,params, objs, IResult.ROOT_NAME);
		Writer writer = new OutputStreamWriter(resp.getOutputStream(),IResult.CON_ENCODING_UTF_8);
		try {
			template.process(params, writer);
			writer.flush();
		} catch (TemplateException e) {
			LOG.error("error while process freemarker", e);
		}
	}

}
