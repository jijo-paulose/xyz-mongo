package xyz.mongo.tcp.service.json;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import xyz.mongo.IBizHandler;
import xyz.mongo.XyzConstants;
import xyz.mongo.ds.IMongoDataSource;
import xyz.mongo.ds.IMongoDataSourceManager;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class JsonDsBizHandler implements IBizHandler {
	private static final Log LOG = LogFactory.getLog(JsonDsBizHandler.class);

	private IMongoDataSource mds;

	public JsonDsBizHandler() {
		String classpath = System.getProperty("springConfig",
				"jsonDsHandler.xml");
		ApplicationContext context = new ClassPathXmlApplicationContext(
				classpath);
		IMongoDataSourceManager mongoDataSourceManager = (IMongoDataSourceManager) context
				.getBean("mongoDataSourceManager");
		mds = mongoDataSourceManager.getMongoDataSource();
	}

	@Override
	public Object exec(Object... args) {
		try {
			String message = (String) args[0];
			DBObject dbobj = (DBObject) JSON.parse(message);
			checkDbObj(dbobj);
			String coll = (String) dbobj.get("col");
			String method = (String) dbobj.get("method");
			DBObject params = (DBObject) dbobj.get("params");
			Object res=mds.col(coll, method, params);
			DBObject obj=new BasicDBObject();
			obj.put("ver", XyzConstants.VERSION);
			obj.put("res", res);
			return obj;
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

	private void checkDbObj(DBObject dbobj) {
		if (!dbobj.containsField("col")) {
			throw new IllegalArgumentException("args must has a col param");
		}
		if (!dbobj.containsField("method")) {
			throw new IllegalArgumentException("args must has a method param");
		}
		if (!dbobj.containsField("params")) {
			throw new IllegalArgumentException("args must has a params param");
		}
	}

}
