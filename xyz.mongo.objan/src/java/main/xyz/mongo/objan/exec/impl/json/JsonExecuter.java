package xyz.mongo.objan.exec.impl.json;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import xyz.mongo.objan.exec.IAnUtil;
import xyz.mongo.objan.exec.IExecuter;
import xyz.mongo.util.IJsonStringUtil;
import xyz.mongo.util.impl.FreemarkerJsonStringUtil;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class JsonExecuter implements IExecuter {
	private static final Log LOG = LogFactory.getLog(JsonExecuter.class);

	protected MongoTemplate template;
	
	protected IJsonStringUtil jsonStringUtil = FreemarkerJsonStringUtil
			.getInstance();

	protected IAnUtil anUtil;
	
	private Map<String,IJsonProcesser> jsonProcessers=new HashMap<String,IJsonProcesser>();

	@Override
	public Object exec(MethodInvocation invocation, Map<String, Object> params) throws Exception{
		Method method = invocation.getMethod();
		Json json = (Json) anUtil.getAn(method, Json.class);
		if (null == json) {
			throw new IllegalStateException("Json can't be null 4 method:"
					+ method.getName());
		}
        String value=json.value();
        String realValue=jsonStringUtil.merge(value, params);
        DBObject valueObj=(DBObject)JSON.parse(realValue);
        String type=(String)valueObj.get("type");
        if(!jsonProcessers.containsKey(type)){
        	throw new IllegalStateException("not have a json processer named:"+type);
        }
        IJsonProcesser processer=jsonProcessers.get(type);
        return processer.process(valueObj, params, template);
	}

	public void setTemplate(MongoTemplate template) {
		this.template = template;
	}

	/**
	 * @param anUtil
	 *            the anUtil to set
	 */
	public void setAnUtil(IAnUtil anUtil) {
		this.anUtil = anUtil;
	}

	/**
	 * @param jsonStringUtil
	 *            the jsonStringUtil to set
	 */
	public void setJsonStringUtil(IJsonStringUtil jsonStringUtil) {
		this.jsonStringUtil = jsonStringUtil;
	}

	public void setJsonProcessers(Map<String, IJsonProcesser> jsonProcessers) {
		this.jsonProcessers = jsonProcessers;
	}

}
