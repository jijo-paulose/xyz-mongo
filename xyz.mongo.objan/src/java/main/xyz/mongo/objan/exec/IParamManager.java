package xyz.mongo.objan.exec;

import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;

public interface IParamManager {
      Map<String,Object> params(MethodInvocation invocation)throws Exception;
}
