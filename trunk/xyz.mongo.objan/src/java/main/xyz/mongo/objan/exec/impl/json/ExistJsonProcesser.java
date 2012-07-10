package xyz.mongo.objan.exec.impl.json;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.DBObject;

public class ExistJsonProcesser extends CountJsonProcesser {
	private static final Log LOG = LogFactory.getLog(ExistJsonProcesser.class);

	@Override
	public Object process(DBObject valueObj, Map<String, Object> params,
			MongoTemplate template) throws Exception {
		return (Long)super.process(valueObj, params, template)>0;
	}
}
