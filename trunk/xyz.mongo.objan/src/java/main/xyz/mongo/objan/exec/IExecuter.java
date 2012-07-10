package xyz.mongo.objan.exec;

import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;

public interface IExecuter {
    Object exec(MethodInvocation invocation,Map<String,Object> params)throws Exception;
}
