package xyz.mongo.objan.exec.impl.an;

import java.lang.reflect.Method;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import xyz.mongo.objan.exec.IAnUtil;
import xyz.mongo.objan.exec.IExecuter;
import xyz.mongo.objan.util.DvUtil;
import xyz.mongo.util.IJsonStringUtil;
import xyz.mongo.util.impl.FreemarkerJsonStringUtil;

public class CountExecuter implements IExecuter {
	private static final Log LOG = LogFactory.getLog(CountExecuter.class);

	protected MongoTemplate template;
	

	protected IJsonStringUtil jsonStringUtil = FreemarkerJsonStringUtil
			.getInstance();

	protected IAnUtil anUtil;

	@Override
	public Object exec(MethodInvocation invocation, Map<String, Object> params) {
		Method method = invocation.getMethod();
		Count count = (Count) anUtil.getAn(method, Count.class);
		if (null == count) {
			throw new IllegalStateException("Count can't be null 4 method:"
					+ method.getName());
		}
		Class entityClass = (Class) params.get("entityClass");
		String realValue = null;
		DV[] dvs = count.dv();
		if (0 != dvs.length) {
			realValue = DvUtil.dv(dvs, jsonStringUtil, params);
		} else {
			String value = count.value();
			realValue = jsonStringUtil.merge(value, params);
		}
		Query query = new BasicQuery(realValue);
		return template.count(query, entityClass);
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
