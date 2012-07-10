package xyz.mongo.util.impl;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import xyz.mongo.util.IJsonStringUtil;

import com.mongodb.DBObject;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerJsonStringUtil implements IJsonStringUtil {
	private static final FreemarkerJsonStringUtil fjsu=new FreemarkerJsonStringUtil();
	public static FreemarkerJsonStringUtil getInstance(){
		fjsu.init();
		return fjsu;
	}
	
	private static Map<String, Object> DEFAULT_BUILD_IN = new HashMap<String, Object>();
	static {
		DEFAULT_BUILD_IN.put("xyzRegexFlag", "cdgimstux");
	}

	private String defaultEncoding = "UTF-8";
	private String numberFormat = "0.######";//保证输出数字不被分割，加点，加逗号等等
	private Map<String, Object> buildin = new HashMap();

	private Map<String, String> fss = new HashMap();
	
	private Configuration conf=new Configuration();
	private JsonStringTemplateLoader jstl=new JsonStringTemplateLoader();
	
	/**
	 * 这个方法必须在上面几个东西的set方法调用完毕之后，没有调用merge的时候调用
	 */
	public void init(){
		conf.setTemplateLoader(jstl);
		conf.setDefaultEncoding(defaultEncoding);
		conf.setNumberFormat(numberFormat);
		for (String key : fss.keySet()) {
			try {
				conf.setSetting(key, fss.get(key));
			} catch (Throwable t) {
				// now do nothing ,but must use log to do something later
			}
		}
	}

	public String merge(String src, DBObject params){
		return merge(src,params.toMap());
	}
	public String merge(String src, Map<String, Object> params) {
		try {
			params.putAll(DEFAULT_BUILD_IN);
			params.put("xyzNow", System.currentTimeMillis());
			params.putAll(buildin);
			Template template = conf.getTemplate(src);
			StringWriter writer = new StringWriter();
			template.process(params, writer);
			return writer.toString();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new IllegalArgumentException("error args while merge string "
					+ src + " and params: " + params);
		}
	}

	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	public void setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
	}

	public void setBuildin(Map<String, Object> buildin) {
		this.buildin = buildin;
	}

	public void setFss(Map<String, String> fss) {
		this.fss = fss;
	}

	public void setJstl(JsonStringTemplateLoader jstl) {
		this.jstl = jstl;
	}

}
