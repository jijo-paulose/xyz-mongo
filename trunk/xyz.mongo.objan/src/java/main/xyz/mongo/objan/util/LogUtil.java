package xyz.mongo.objan.util;

import org.apache.commons.logging.Log;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public final class LogUtil {
    private LogUtil(){
    	
    }
    
	public static void log(final Log LOG,Query query, Update update) {
		if (LOG.isDebugEnabled()) {
			if (null != query) {
				LOG.debug("query object is:" + query.getQueryObject());
				LOG.debug("order object is:" + query.getSortObject());
				LOG.debug("fields is:" + query.getFieldsObject());
				LOG.debug("skip is:" + query.getSkip());
				LOG.debug("limit is:" + query.getLimit());
			}
			if (null != update) {
				LOG.debug("update obj is:" + update.getUpdateObject());
			}
		}
	}
}
