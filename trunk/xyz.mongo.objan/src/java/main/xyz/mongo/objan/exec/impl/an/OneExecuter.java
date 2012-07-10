package xyz.mongo.objan.exec.impl.an;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.util.StringUtils;

import xyz.mongo.objan.exec.IAnUtil;
import xyz.mongo.objan.exec.IExecuter;
import xyz.mongo.objan.util.LogUtil;
import xyz.mongo.util.IBeanUtil;
import xyz.mongo.util.IJsonStringUtil;
import xyz.mongo.util.impl.FreemarkerJsonStringUtil;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class OneExecuter implements IExecuter {
	private static final Log LOG = LogFactory.getLog(OneExecuter.class);

	protected MongoTemplate template;

	protected IJsonStringUtil jsonStringUtil = FreemarkerJsonStringUtil
			.getInstance();

	protected IAnUtil anUtil;
	protected IBeanUtil beanUtil;

	@Override
	public Object exec(MethodInvocation invocation, Map<String, Object> params) {
		Method method = invocation.getMethod();
		One one = (One) anUtil.getAn(method, One.class);
		if (null == one) {
			throw new IllegalStateException("One can't be null 4 method:"
					+ method.getName());
		}
		Class entityClass = (Class) params.get("entityClass");
		String value = one.value();
		String realValue = jsonStringUtil.merge(value, params);
		String fields = one.fields();
		String realFields = jsonStringUtil.merge(fields, params);
		BasicQuery query = new BasicQuery(realValue, realFields);
		String sorts = one.sorts();
		if (StringUtils.hasText(sorts)) {
			DBObject sortObject = (DBObject) JSON.parse(sorts);
			query.setSortObject(sortObject);
		}
		if (null != query.getSortObject()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("now use list to get one");
			}
			query.limit(1);
			LogUtil.log(LOG, query, null);
			List list = template.find(query, entityClass);
			if (null == list || list.isEmpty()) {
				return null;
			}
			if(null!=one.vo()){
				return beanUtil.copy(list.get(0), one.vo());
			}else{
				return list.get(0);
			}
		} else {
			if(null!=one.vo()){
				return beanUtil.copy(template.findOne(query, entityClass), one.vo());
			}
			return template.findOne(query, entityClass);
		}
		// 现在先不考虑,那些子对象VO的属性拷贝问题，现在都在一个地方来做的
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

	public void setBeanUtil(IBeanUtil beanUtil) {
		this.beanUtil = beanUtil;
	}

}
