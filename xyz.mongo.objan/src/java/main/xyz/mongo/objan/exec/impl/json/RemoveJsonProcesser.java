package xyz.mongo.objan.exec.impl.json;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;

import xyz.mongo.objan.util.DvUtil;
import xyz.mongo.util.IJsonStringUtil;
import xyz.mongo.util.impl.FreemarkerJsonStringUtil;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class RemoveJsonProcesser implements IJsonProcesser {
	private static final Log LOG = LogFactory.getLog(RemoveJsonProcesser.class);
	protected IJsonStringUtil jsonStringUtil = FreemarkerJsonStringUtil
			.getInstance();

	@Override
	public Object process(DBObject valueObj, Map<String, Object> params,
			MongoTemplate template) throws Exception {
		Class entityClass = (Class) params.get("entityClass");
		String realValue=null;
		if(valueObj.containsField("dv")){
			BasicDBList list=(BasicDBList)valueObj.get("dv");
			realValue=DvUtil.dv(list.toArray(new String[list.size()]), jsonStringUtil, params);
		}else{
			String value = (String) valueObj.get("value");
			realValue = jsonStringUtil.merge(value, params);
		}
		BasicQuery query = new BasicQuery(realValue);
		template.remove(query, entityClass);
		return "void";
		// 现在先不考虑,那些子对象VO的属性拷贝问题，现在都在一个地方来做的
	}

	public void setJsonStringUtil(IJsonStringUtil jsonStringUtil) {
		this.jsonStringUtil = jsonStringUtil;
	}

}
