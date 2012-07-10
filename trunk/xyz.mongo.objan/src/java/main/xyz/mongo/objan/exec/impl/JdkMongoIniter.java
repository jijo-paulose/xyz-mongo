package xyz.mongo.objan.exec.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xyz.mongo.objan.exec.IMongoIniter;
import xyz.mongo.objan.exec.MongoAbstract;
import xyz.mongo.objan.exec.impl.an.TestMethodInvocation;
import xyz.mongo.objan.util.JdkProxyRepository;

public class JdkMongoIniter implements IMongoIniter {
	private static final Log LOG = LogFactory.getLog(JdkMongoIniter.class);
	private MethodInterceptor interceptor;

	public void init(MongoAbstract mongo) throws Exception {
		Object target=JdkProxyRepository.createDefaultProxy(new MongoProxy(interceptor,mongo), mongo.getTargetType());
		mongo.setTarget(target);
	}
	
	static class MongoProxy implements InvocationHandler{
		private MethodInterceptor interceptor;
		private MongoAbstract mongo;
		
		public MongoProxy(MethodInterceptor interceptor,MongoAbstract mongo){
			this.interceptor=interceptor;
			this.mongo=mongo;
		}
		
		@Override
		public Object invoke(Object obj, Method method, Object[] args)
				throws Throwable {
			TestMethodInvocation invocation=new TestMethodInvocation();
			invocation.setMethod(method);
			invocation.setArguments(args);
			invocation.setThis(obj);
			return interceptor.invoke(invocation);
		}
		
	}

	public void setInterceptor(MethodInterceptor interceptor) {
		this.interceptor = interceptor;
	}
}
