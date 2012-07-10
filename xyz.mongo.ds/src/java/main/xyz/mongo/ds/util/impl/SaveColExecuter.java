package xyz.mongo.ds.util.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xyz.mongo.ds.util.ICollectionExecuter;
import xyz.mongo.ds.util.IWriteConcernManager;
import xyz.mongo.ds.util.IWriterCheck;
import xyz.mongo.util.IJsonStringUtil;
import xyz.mongo.util.IUuidGenerator;
import xyz.mongo.util.impl.FreemarkerJsonStringUtil;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
/**
 * json格式：
 * {
 * 	  type:save,
 *    value:
 *    uuid:
 * }
 * @author zmc
 *
 */
public class SaveColExecuter implements ICollectionExecuter{
    private static final Log LOG=LogFactory.getLog(SaveColExecuter.class);
    
    protected IJsonStringUtil jsonStringUtil=FreemarkerJsonStringUtil.getInstance();
    private IWriterCheck writerCheck;
    private IWriteConcernManager writeConcernManager;
    
	public Object process(DBObject methodQuery, DBCollection coll,DBObject transParams)
			throws Exception {
		String value=jsonStringUtil.merge((String)methodQuery.get("value"), transParams);
		if(LOG.isDebugEnabled()){
			LOG.debug("Now save for {table:"+coll.getName()+" ,value:"+value+"}");
		}
		DBObject valueDbo = (DBObject) JSON.parse(value);

		if(!methodQuery.containsField("_id")){
			throw new IllegalArgumentException("save collection must have a _id field!");
		}
		WriteResult result=null;
        if(writeConcernManager.isUse()){
        	result=coll.save(valueDbo, writeConcernManager.getWriteConcern());
        }else{
        	result=coll.save(valueDbo);
        }
	    writerCheck.check(result);
	    return "void";
	}


	public void setWriterCheck(IWriterCheck writerCheck) {
		this.writerCheck = writerCheck;
	}


	public void setWriteConcernManager(IWriteConcernManager writeConcernManager) {
		this.writeConcernManager = writeConcernManager;
	}

}
