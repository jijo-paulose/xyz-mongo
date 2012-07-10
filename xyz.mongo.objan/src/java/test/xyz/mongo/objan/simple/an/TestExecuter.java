package xyz.mongo.objan.simple.an;

import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;

import xyz.mongo.objan.exec.IExecuter;

public class TestExecuter implements IExecuter{

	@Override
	public Object exec(MethodInvocation invocation, Map<String, Object> params) throws Exception{
		try {
			for(String key:params.keySet()){
				System.out.println("key:"+key);
				System.out.println("value:"+params.get(key));
			}
			System.out.println("hhhh");
			return invocation.proceed();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
