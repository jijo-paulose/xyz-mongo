package xyz.mongo.ds.servlet.cont;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xyz.mongo.ds.IMongoDataSource;
import xyz.mongo.ds.servlet.utils.IServiceLocator;
import xyz.mongo.ds.servlet.utils.TransUtil;
import xyz.mongo.ds.servlet.view.IResult;
import xyz.mongo.ds.servlet.view.IResultManager;
import xyz.mongo.ds.servlet.view.impl.FreeMarkerResult;
import xyz.mongo.ds.servlet.view.impl.VelocityResult;

import com.mongodb.DBObject;

public class MongoDsServlet extends HttpServlet {
	private static final Log LOG=LogFactory.getLog(MongoDsServlet.class);
	private static final String SERVICE_LOCATOR_CLASS = "service_locator_class";
	private static final String FREEMARKER_PATH="freemarker_path";
	private static final String VELOCITY_PATH="velocity_path";
	private static final String QEQUSET_ENCODING="request_charset_encoding";
	
	private IMongoDataSource mds;
	private IResultManager resultManager;
	
	private String requestCharsetEncoding="UTF-8";

	public void init(ServletConfig config) throws ServletException {
		try {
			String serviceLocatorClass = config.getInitParameter(SERVICE_LOCATOR_CLASS);
			Class clasz = Class.forName(serviceLocatorClass);
			IServiceLocator serviceLocator=(IServiceLocator)clasz.newInstance();
			mds=serviceLocator.getMongoDataSource();
			resultManager=serviceLocator.getReulstManager();
			
			String vp=config.getInitParameter(VELOCITY_PATH);
			if(null==vp){
				vp="/";
			}
			String realVp=config.getServletContext().getRealPath(vp);
			VelocityResult vr=(VelocityResult)serviceLocator.get("velocityResult");
			vr.setFilePath(realVp);
			String fp=config.getInitParameter(FREEMARKER_PATH);
			if(null==fp){
				fp="/";
			}
			String realFp=config.getServletContext().getRealPath(fp);
			FreeMarkerResult fr=(FreeMarkerResult)serviceLocator.get("freemarkResult");
			fr.setFilePath(realFp);
			String rce=config.getInitParameter(QEQUSET_ENCODING);
			if(null!=rce){
				requestCharsetEncoding=rce;
			}
		} catch (Throwable t) {
			LOG.error("Error while init MongoDsServlet", t);
		}
	}

	public void service(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			request.setCharacterEncoding(requestCharsetEncoding);
			DBObject params = TransUtil.request2DBObject(request);
			//System.err.println("---------------------params-------------------"+params);
			//PathInfo--/null //!后/可写可不写，一回事
			//ServletPath--/table!method!jsp!/hee/ggg/ddd.ds
			//http://localhost:9080/dbo/table!method!jsp!hee/ggg/ddd.ds?a=b
			String servletPath=request.getServletPath();
			String[] paths=servletPath.split("!");
			String coll = paths[0].substring(1);
			String method = paths[1];
			Object objs = mds.col(coll, method, params);
			String type = paths[2];
			if(type.startsWith("/")){
				type=type.substring(1);
			}
			String path = null;//json和jsonp是可以不要这个东西的，当然jsonp可以把他作为js的function名字吧
			if(paths.length==4){
				path=paths[3];
				int index=path.lastIndexOf(".");
				path=path.substring(0,index+1)+type;
			}
			IResult reslut = resultManager.get(type, path);
			//path="/page/userAddResult.jsp";
			reslut.process(request, resp, path, objs);
		} catch (Throwable e) {
			LOG.error("Error while service MongoDsServlet", e);
		}
	}
}
