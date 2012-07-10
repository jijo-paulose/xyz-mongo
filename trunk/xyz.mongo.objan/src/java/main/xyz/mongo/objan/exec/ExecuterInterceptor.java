package xyz.mongo.objan.exec;

import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExecuterInterceptor implements MethodInterceptor{
	private static final Log LOG=LogFactory.getLog(ExecuterInterceptor.class);

	private IExecuterManager executerManager;
	
	private IParamManager paramManager;

	public Object invoke(MethodInvocation invocation) throws Throwable {
		IExecuter executer=executerManager.get(invocation);
		if(null==executer){
			return invocation.proceed();
		}
		//这个需要待定啦，将method传来的params的值和其他东西传来的东西的值放在这里
		//不用threadLocal传递，为了逻辑清晰，除非必要，才在最后用那招
		Map<String,Object> params=paramManager.params(invocation);
		return executer.exec(invocation,params);
	}
	public void setExecuterManager(IExecuterManager executerManager) {
		this.executerManager = executerManager;
	}
	/**
	 * @return the paramManager
	 */
	public IParamManager getParamManager() {
		return paramManager;
	}
	/**
	 * @param paramManager the paramManager to set
	 */
	public void setParamManager(IParamManager paramManager) {
		this.paramManager = paramManager;
	}
}
