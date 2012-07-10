package xyz.mongo.objan.exec;

import org.aopalliance.intercept.MethodInvocation;

public interface IExecuterManager {
	IExecuter get(MethodInvocation invocation);
}
