package xyz.mongo.objan.exec.impl.an;

import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;

public class ExistExecuter extends CountExecuter {

	@Override
	public Object exec(MethodInvocation invocation, Map<String, Object> params) {
		return (Long)super.exec(invocation, params)>0;
	}



}
