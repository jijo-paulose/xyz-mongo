package xyz.mongo.objan.exec.impl.json;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.util.StringUtils;

import xyz.mongo.util.IBeanUtil;
import xyz.mongo.util.IJsonStringUtil;
import xyz.mongo.util.impl.FreemarkerJsonStringUtil;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class TopJsonProcesser implements IJsonProcesser {
	private static final Log LOG = LogFactory.getLog(TopJsonProcesser.class);
	protected IJsonStringUtil jsonStringUtil = FreemarkerJsonStringUtil
			.getInstance();
	protected IBeanUtil beanUtil;

	@Override
	public Object process(DBObject valueObj, Map<String, Object> params,
			final MongoTemplate template) throws Exception {
		final Class entityClass = (Class) params.get("entityClass");
		String realValue = null;
		if (valueObj.containsField("dv")) {
			BasicDBList dvs = (BasicDBList) valueObj.get("dv");
			StringBuilder sb = new StringBuilder("{");
			boolean first = true;
			for (Object obj : dvs) {
				DBObject dv = (DBObject) obj;
				String from = (String) dv.get("value");
				if (LOG.isDebugEnabled()) {
					LOG.debug("from--" + from);
				}
				String to = null;
				try {
					to = jsonStringUtil.merge(from, params);
				} catch (Throwable e) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("error while merge dv string " + from
								+ " and params: " + params, e);
					}
				}
				// 避免用户写错vo，返回字符长度为0的字符串啊
				if (!StringUtils.hasText(to)) {
					continue;
				}
				if (!first) {
					sb.append(",");
				} else {
					first = false;
				}
				if (LOG.isDebugEnabled()) {
					LOG.debug("to--" + to);
				}
				sb.append(to);
			}
			sb.append("}");
			realValue = sb.toString();
		} else {
			String value = (String) valueObj.get("value");
			realValue = jsonStringUtil.merge(value, params);
		}
		String fields = (String) valueObj.get("fields");
		String realFields = jsonStringUtil.merge(fields, params);
		final BasicQuery query = new BasicQuery(realValue, realFields);
		String sorts = fields = (String) valueObj.get("sorts");
		if (StringUtils.hasText(sorts)) {
			DBObject sortObject = (DBObject) JSON.parse(sorts);
			query.setSortObject(sortObject);
		}
		int limit = (Integer) params.get("limit");
		query.limit(limit);
		// 同那个东西一样，属性拷贝的工作现在先不做吧
		List result=template.find(query, entityClass);
		if(valueObj.containsField("vo")){
			Class clasz=Class.forName((String)valueObj.get("vo"));
			beanUtil.listCopy(result, clasz);
		}
		return result;
		//return template.find(query, entityClass);
	}

	public void setJsonStringUtil(IJsonStringUtil jsonStringUtil) {
		this.jsonStringUtil = jsonStringUtil;
	}

	public void setBeanUtil(IBeanUtil beanUtil) {
		this.beanUtil = beanUtil;
	}

}
