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

public class SonExecuter implements IExecuter {
	private static final Log LOG = LogFactory.getLog(SonExecuter.class);

	protected MongoTemplate template;
	

	protected IJsonStringUtil jsonStringUtil = FreemarkerJsonStringUtil
			.getInstance();

	protected IAnUtil anUtil;
	protected IBeanUtil beanUtil;

	@Override
	public Object exec(MethodInvocation invocation, Map<String, Object> params) {
		Method method = invocation.getMethod();
		Son son = (Son) anUtil.getAn(method, Son.class);
		if (null == son) {
			throw new IllegalStateException("Son can't be null 4 method:"
					+ method.getName());
		}
		Class entityClass = (Class) params.get("entityClass");
			String value = son.value();
			String realValue = jsonStringUtil.merge(value, params);
			String fields=son.son();
			String realFields=jsonStringUtil.merge(fields, params);
			BasicQuery query = new BasicQuery(realValue,"{"+realFields+":1}");
		String sorts=son.sorts();
		if (StringUtils.hasText(sorts)) {
			DBObject sortObject = (DBObject) JSON.parse(sorts);
			query.setSortObject(sortObject);
		}
		//Object res
		Object temp=null;
		if(null!=query.getSortObject()){
			if(LOG.isDebugEnabled()){
				LOG.debug("now use list to get one");
			}
			query.limit(1);
			LogUtil.log(LOG,query,null);
			List list=template.find(query, entityClass);
			if(null==list||list.isEmpty()){
			}else{
				temp= list.get(0);
			}
			
		}else{
			temp=template.findOne(query, entityClass);
		}
		return beanUtil.prop(temp, son.son());
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
