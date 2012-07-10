package xyz.mongo.objan.exec.impl;

import java.lang.reflect.Method;

import xyz.mongo.objan.exec.impl.an.TestMethodInvocation;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
/**
 * 这个就不用配置spring的拦截器啦，直接用这个代理一步到位啦
 * 不过我这里实际上还是调用了做的那个实现，不过没有做而已
 * @author zmc
 */
public class NoUseInterMethodInterceptor implements MethodInterceptor{
    private org.aopalliance.intercept.MethodInterceptor interceptor;
    
	@Override
	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		//首先，查看是不是没有实现的方法,这个现在还没有实现
		if(false==true){
			return proxy.invokeSuper(obj, args);
		}
		TestMethodInvocation invocation=new TestMethodInvocation();
		invocation.setMethod(method);
		invocation.setArguments(args);
		invocation.setThis(obj);
		return interceptor.invoke(invocation);
	}

	public void setInterceptor(
			org.aopalliance.intercept.MethodInterceptor interceptor) {
		this.interceptor = interceptor;
	}

}
