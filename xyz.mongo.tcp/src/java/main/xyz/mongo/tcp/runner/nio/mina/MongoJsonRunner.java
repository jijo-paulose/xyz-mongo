package xyz.mongo.tcp.runner.nio.mina;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.compression.CompressionFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import xyz.mongo.IBizHandler;

public class MongoJsonRunner {
    private static final Log LOG=LogFactory.getLog(MongoJsonRunner.class);
	
	public static void main(String[] args){
		int PORT = 8888;
		boolean USE_LOG=false;
		boolean USE_COM=false;
		String handlerClass=System.getProperty("handlerClass","xyz.mongo.tcp.service.json.JsonBizHandler");
		IBizHandler bizHandler=null;
		try {
			Class class4Handler=Class.forName(handlerClass);
			bizHandler=(IBizHandler)class4Handler.newInstance();
		} catch (Throwable e) {
			LOG.error("error get handler class",e);
			throw new RuntimeException("error get handler class",e);
		}
		String port=System.getProperty("port");
		if(null!=port){
			PORT = Integer.parseInt(port);
		}
		USE_LOG = "true".equalsIgnoreCase(System.getProperty("uselog"));
		USE_COM = "true".equalsIgnoreCase(System.getProperty("usecom"));
        System.out.println(System.getProperties());
		NioSocketAcceptor acceptor = new NioSocketAcceptor();
		if(USE_LOG){
			acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		}
		if(USE_COM){
			 CompressionFilter cf=new CompressionFilter();
			 acceptor.getFilterChain().addLast("compression",cf);
		}
		
		TextLineCodecFactory tcf = new TextLineCodecFactory(Charset.forName("UTF-8"));
		tcf.setDecoderMaxLineLength(Integer.MAX_VALUE);
		tcf.setEncoderMaxLineLength(Integer.MAX_VALUE);
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(tcf));
		
		JsonBizProtocolHandler pHandler = new JsonBizProtocolHandler();
		pHandler.setHandler(bizHandler);
		acceptor.setHandler(pHandler);
		
		boolean bindOk=false;//
		for(int i=0;i<1;i++){
			try {
				acceptor.bind(new InetSocketAddress(PORT));
				bindOk=true;
				break;
			} catch (IOException e) {
				System.out.println("Error bind "+(i+1)+" times");
				e.printStackTrace();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		if(!bindOk){
			System.exit(-1);
		}
		
		LOG.info("Listening on port " + PORT);
		System.out.println("Listening on port " + PORT);
	}
}
