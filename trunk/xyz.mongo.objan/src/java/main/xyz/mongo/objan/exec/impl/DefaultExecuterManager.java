package xyz.mongo.objan.exec.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xyz.mongo.objan.exec.IAnUtil;
import xyz.mongo.objan.exec.IExecuter;
import xyz.mongo.objan.exec.IExecuterManager;

public class DefaultExecuterManager implements IExecuterManager{
    private static final Log LOG=LogFactory.getLog(DefaultExecuterManager.class);
    
    private Map<String,IExecuter> executers=new HashMap<String,IExecuter>();
    
    private IExecuter defExecuter;
    
    private boolean canNoMet=true;
    
    private IAnUtil anUtil;
    
	@Override
	public IExecuter get(MethodInvocation invocation) {
		Method method=invocation.getMethod();
		Annotation an=anUtil.getAn(method);
		if(null==an){
			if(canNoMet){
				return defExecuter;
			}else{
				throw new IllegalStateException("can't find Met annotation 4 method:"+method.getName());
			}
		}
		String type=anUtil.getAnType(an);
		return executers.get(type);
	}
	
	public void addExecuter(String name,IExecuter executer){
		executers.put(name, executer);
	}

	/**
	 * @return the executers
	 */
	public Map<String, IExecuter> getExecuters() {
		return executers;
	}

	/**
	 * @param executers the executers to set
	 */
	public void setExecuters(Map<String, IExecuter> executers) {
		this.executers = executers;
	}

	/**
	 * @param defExecuter the defExecuter to set
	 */
	public void setDefExecuter(IExecuter defExecuter) {
		this.defExecuter = defExecuter;
	}

	/**
	 * @param anUtil the anUtil to set
	 */
	public void setAnUtil(IAnUtil anUtil) {
		this.anUtil = anUtil;
	}

	/**
	 * @param canNoMet the canNoMet to set
	 */
	public void setCanNoMet(boolean canNoMet) {
		this.canNoMet = canNoMet;
	}

}
