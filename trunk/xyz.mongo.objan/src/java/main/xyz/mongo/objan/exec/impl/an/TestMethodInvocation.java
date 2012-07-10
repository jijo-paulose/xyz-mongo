package xyz.mongo.objan.exec.impl.an;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
/**
 * 开始只是测试用的例子，现在作为cglib和aopalliance的转换而用
 * @author zmc
 *
 */
public class TestMethodInvocation implements MethodInvocation{
    private Object[] arguments;
    private Method method;
    private Object obj;
    
	@Override
	public Object[] getArguments() {
		return arguments;
	}

	@Override
	public AccessibleObject getStaticPart() {
		// TODO Auto-generated method stub
		return null;
	}
    public void setThis(Object obj){
    	this.obj=obj;
    }
	@Override
	public Object getThis() {
		// TODO Auto-generated method stub
		return obj;
	}

	@Override
	public Object proceed() throws Throwable {
		// TODO Auto-generated method stub
		return method.invoke(obj, arguments);
	}

	@Override
	public Method getMethod() {
		// TODO Auto-generated method stub
		return method;
	}

	/**
	 * @param arguments the arguments to set
	 */
	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(Method method) {
		this.method = method;
	}

}
