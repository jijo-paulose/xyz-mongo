package xyz.mongo.tcp.runner.nio.mina;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import xyz.mongo.IBizHandler;
import xyz.mongo.tcp.service.json.JsonBizHandler;

public class JsonBizProtocolHandler extends IoHandlerAdapter{
	private static final Log LOG=LogFactory.getLog(JsonBizProtocolHandler.class);
	private static final String PERF_LOG_KEY="mina_perf";
	private static boolean PERF_LOG=false;
	static{
		PERF_LOG="true".equalsIgnoreCase(System.getProperty(PERF_LOG_KEY));
	}
	
	private IBizHandler handler=new JsonBizHandler();
	
    public void exceptionCaught(IoSession session, Throwable cause) {
    	if(LOG.isErrorEnabled()){
    		LOG.error("error while handle json biz:"+cause.getMessage(), cause);
    	}
        session.close(true);
    }

    public void messageReceived(IoSession session, Object message) {
    	long begin=0l;
    	if(PERF_LOG){
    		begin=System.currentTimeMillis();
    	}
        Object res=handler.exec(message);
        session.write((String)res);
        if(PERF_LOG){
    		LOG.warn("Mina use time:"+(System.currentTimeMillis()-begin)+" ms for---("+message+")");
    	}
    }

	public void setHandler(IBizHandler handler) {
		this.handler = handler;
	}
}
