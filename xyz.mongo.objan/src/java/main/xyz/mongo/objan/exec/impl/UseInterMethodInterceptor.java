package xyz.mongo.objan.exec.impl;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class UseInterMethodInterceptor implements MethodInterceptor{

	@Override
	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		//这个对于那些需要做的东西怎么办呢,可能会有点问题吧
		//有必要用method的吗，先不考虑，不过缺点是类型不再，不能注入啦
		return proxy.invokeSuper(obj, args);
	}

}
