package xyz.mongo.tcp.service.json;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import xyz.mongo.IBizHandler;
import xyz.mongo.XyzConstants;

public class JsonAllInOneBizHandler implements IBizHandler{
	private static final Log LOG = LogFactory.getLog(JsonAllInOneBizHandler.class);
	
    private IBizHandler dsHandler;
    private IBizHandler objanHandler;
    
	@Override
	public Object exec(Object... args) {
		try {
			String message = (String) args[0];
			DBObject dbobj = (DBObject) JSON.parse(message);
			if(dbobj.containsField("col")){
				return dsHandler.exec(args);
			}
			return objanHandler.exec(args);
		} catch (Throwable t) {
			LOG.error("error while json ds biz handler --"+args[0], t);
			DBObject obj=new BasicDBObject();
			obj.put("ver", XyzConstants.VERSION);
			StringWriter spw = new StringWriter();
			t.printStackTrace(new PrintWriter(spw));
			DBObject err=new BasicDBObject();
			err.put("stack", spw.toString());
			err.put("msg", t.getMessage());
			obj.put("err", err);
            return obj;
		}
	}

	public void setDsHandler(IBizHandler dsHandler) {
		this.dsHandler = dsHandler;
	}

	public void setObjanHandler(IBizHandler objanHandler) {
		this.objanHandler = objanHandler;
	}

}
