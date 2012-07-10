package xyz.mongo.objan.exec.impl.an;

import java.lang.reflect.Method;
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

public class DistinctExecuter implements IExecuter {
	private static final Log LOG = LogFactory.getLog(DistinctExecuter.class);

	protected MongoTemplate template;

	protected IJsonStringUtil jsonStringUtil = FreemarkerJsonStringUtil
			.getInstance();

	private IAnUtil anUtil;

	@Override
	public Object exec(MethodInvocation invocation, Map<String, Object> params) {
		Method method = invocation.getMethod();
		Distinct distinct = (Distinct) anUtil.getAn(method, Distinct.class);
		if (null == distinct) {
			throw new IllegalStateException("Distinct can't be null 4 method:"
					+ method.getName());
		}
		Class entityClass = (Class) params.get("entityClass");
		String value = distinct.value();
		String realValue= jsonStringUtil.merge(value, params);
		DBObject query=(DBObject)JSON.parse(realValue);
		String key=distinct.key();
		return template.getCollection(template.getCollectionName(entityClass)).distinct(key, query);
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

}
