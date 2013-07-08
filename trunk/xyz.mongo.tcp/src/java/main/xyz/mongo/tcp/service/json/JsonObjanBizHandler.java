package xyz.mongo.tcp.service.json;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import xyz.mongo.IBizHandler;
import xyz.mongo.IPageRequest;
import xyz.mongo.Read;
import xyz.mongo.XyzConstants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JsonObjanBizHandler implements IBizHandler{
	private static final Log LOG = LogFactory.getLog(JsonObjanBizHandler.class);

	private ApplicationContext context;
	
	private static boolean errorMsg=false;
	private static boolean returnSpendTime=true;

	static{
		errorMsg="true".equalsIgnoreCase(System.getProperty("err_msg"));
		returnSpendTime="true".equalsIgnoreCase(System.getProperty("returnSpendTime"));
	}
	
	//添加缓存，加快速度,2012-6-27 改成 ConcurrentHashMap 来加快速度
	private Map<String,Method> methodMap=new ConcurrentHashMap<String,Method>();//用service_method为key放置method
	private Map<String,Class[]> paramTypeMap=new ConcurrentHashMap<String,Class[]>();
	private Map<String,Class> returnTypeMap=new ConcurrentHashMap<String,Class>();
	private Map<String,List<Read>> readMap=new ConcurrentHashMap<String,List<Read>>();
	
	public JsonObjanBizHandler() {
		String classpath = System.getProperty("springConfig",
				"jsonObjanHandler.xml");
		context = new ClassPathXmlApplicationContext(
				classpath);
	}

	@Override
	public Object exec(Object... args) {
		try {
			String message = (String) args[0];
			JSONObject dbobj=  JSON.parseObject(message);
			String service = (String) dbobj.get("service");
			String method = (String) dbobj.get("method");
			JSONObject params = (JSONObject) dbobj.get("params");
			String key=service+"_"+method;
			//首先判断reads
			//java.util.concurrent.BlockingQueue<E>
			Object serviceBean = context.getBean("service");
			List<Read> reads = null;
			if(readMap.containsKey(key)){
				reads=readMap.get(key);
			}else{
				Method realMethod = getMethod(serviceBean, method);
				reads = getReads(realMethod);
				readMap.put(key, reads);
				paramTypeMap.put(key, realMethod.getParameterTypes());
				returnTypeMap.put(key, realMethod.getReturnType());
			}
			Method proxyMethod=null;
			if(methodMap.containsKey(key)){
				proxyMethod=methodMap.get(key);
			}else{
				proxyMethod=getProxyMethod(serviceBean, method);
				methodMap.put(key, proxyMethod);
			}

			Class[] classes = paramTypeMap.get(key);

			Object[] realParams = new Object[reads.size()];
			for (int i = 0, len = classes.length; i < len; i++) {
				String name = reads.get(i).name();
				Class clasz = classes[i];
				boolean array=false;
				if(null!=reads.get(i).ctype()){
					clasz=reads.get(i).ctype();
					array=true;
				}
				String rparams = params.get(name)+"";
				realParams[i] = getRealObjects(clasz, rparams,array);
			}

			Object res=proxyMethod.invoke(serviceBean, realParams);
			JSONObject obj=new JSONObject();
			obj.put("ver", XyzConstants.VERSION);
			obj.put("res", res);
			return obj;
		} catch (Throwable t) {
			LOG.error("error while json ds biz handler --"+args[0], t);
			JSONObject obj=new JSONObject();
			obj.put("ver", XyzConstants.VERSION);
			StringWriter spw = new StringWriter();
			t.printStackTrace(new PrintWriter(spw));
			JSONObject err=new JSONObject();
			err.put("stack", spw.toString());
			err.put("msg", t.getMessage());
			obj.put("err", err);
            return obj;
		}
	}
		
		private Method getProxyMethod(Object serviceBean, String name) {
			for(Method method:serviceBean.getClass().getDeclaredMethods()){
				String mname=method.getName();
				if(mname.equals(name)){
					return method;
				}
			}
			throw new IllegalStateException("cant' find "+name+" method 4 "+serviceBean);
		}
		
		private Method getMethod(Object serviceBean, String name) {
			String bname=serviceBean+"";
			bname=bname.substring(0,bname.lastIndexOf("@"));
			Class myclass=null;
			try{
				myclass=Class.forName(bname);
			}catch(Throwable t){
				t.printStackTrace();
			}
			for(Method method:myclass.getDeclaredMethods()){
				String mname=method.getName();
				if(mname.equals(name)){
					return method;
				}
			}
			throw new IllegalStateException("cant' find "+name+" method 4 "+serviceBean);
		}

		/**
		 * 读取缓存
		 * @param realMethod
		 * @return
		 */
		private List<Read> getReads(Method realMethod) {
			List<Read> list=new ArrayList<Read>();
			for(Annotation[] annos:realMethod.getParameterAnnotations()){
				for(Annotation an:annos){
					if(an instanceof Read){
						list.add((Read)an);
					}
				}
			}
			return list;
		}
		
		private Object getRealObjects(Class clazz, String text,boolean array) {
			if(null==text){
				return null;
			}
			 if(array){
			    	return JSON.parseArray(text, clazz);
			 }
		    if(clazz ==String.class){
		    	return text;
		    }
		    if(clazz==IPageRequest.class){
		    	return JSON.parseObject(text, DefaultPageRequest.class);
		    }
		    Object to=JSON.parseObject(text, clazz);
		    return to;
		}

}
