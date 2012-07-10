package xyz.mongo.objan.exec.impl;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xyz.mongo.objan.exec.IMongoIniter;
import xyz.mongo.objan.exec.MongoAbstract;

public class CglibMongoIniter implements IMongoIniter {
	private static final Log LOG = LogFactory.getLog(CglibMongoIniter.class);
	
	private MethodInterceptor proxy;//注入，可以是使用JavaSisst一样的拦截器，也可以不使用拦截器的

	public void setProxy(MethodInterceptor proxy) {
		this.proxy = proxy;
	}

	public void init(MongoAbstract mongo) throws Exception {
		Enhancer e = new Enhancer();
		e.setSuperclass(mongo.getTargetType());
		e.setCallback(proxy);
		Object obj = e.create();
		mongo.setTarget(obj);
		//mongo.setTargetType(obj.getClass());
	}
}
