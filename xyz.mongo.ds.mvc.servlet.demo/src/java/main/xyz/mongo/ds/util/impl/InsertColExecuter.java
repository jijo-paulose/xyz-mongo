package xyz.mongo.ds.util.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xyz.mongo.ds.util.ICollectionExecuter;
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
 * 	  type:insert,
 *    value:
 *    fields:
 *    uuid:
 * }
 * @author zmc
 *
 */
public class InsertColExecuter implements ICollectionExecuter{
    private static final Log LOG=LogFactory.getLog(InsertColExecuter.class);
    
    private IUuidGenerator uuidGenerator;
    protected IJsonStringUtil jsonStringUtil=FreemarkerJsonStringUtil.getInstance();
    private IWriterCheck writerCheck;
    
	public Object process(DBObject methodQuery, DBCollection coll,DBObject transParams)
			throws Exception {
		String value=jsonStringUtil.merge((String)methodQuery.get("value"), transParams);
		if(LOG.isDebugEnabled()){
			LOG.debug("Now insert for {table:"+coll.getName()+" ,value:"+value+"}");
		}
		DBObject valueDbo = (DBObject) JSON.parse(value);
		String uuid="void";
		if(methodQuery.containsField("uuid")){
			uuid=uuidGenerator.next();
			valueDbo.put("_id", uuid);
		}
       
	    WriteResult result=coll.insert(valueDbo);
	    writerCheck.check(result);
	    String fields=(String)methodQuery.get("fields");
	    if(null==fields||"".equals(fields.trim())){
	    	return uuid;
	    }
	    DBObject fieldsDbo = (DBObject) JSON.parse(fields);
	    if(fieldsDbo.keySet().isEmpty()){
	    	return valueDbo;
	    }
	    DBObject returnDBObject=new BasicDBObject();
	    for(String key:fieldsDbo.keySet()){
	    	Object obj=valueDbo.get(key);
	    	returnDBObject.put(key, obj);
	    }
	    return returnDBObject;
	    
	}

	public void setUuidGenerator(IUuidGenerator uuidGenerator) {
		this.uuidGenerator = uuidGenerator;
	}

	public void setWriterCheck(IWriterCheck writerCheck) {
		this.writerCheck = writerCheck;
	}

}
