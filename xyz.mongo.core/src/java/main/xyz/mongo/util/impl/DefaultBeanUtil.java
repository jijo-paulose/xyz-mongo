package xyz.mongo.util.impl;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.beans.BeanCopier;
import xyz.mongo.util.IBeanUtil;

public class DefaultBeanUtil implements IBeanUtil{
	public static final DefaultBeanUtil DEFAULT=new DefaultBeanUtil();
    public static DefaultBeanUtil getInstance(){
    	return DEFAULT;
    }
	@Override
	public void copy(Object from, Object to){
		if(null==from)return;
		BeanCopier beanCopier = BeanCopier.create(from.getClass(),to.getClass(), false); 
		beanCopier.copy(from, to, null);
	}

	@Override
	public Object copy(Object from, Class toClass){
		Object to=null;
		try {
			to = toClass.newInstance();
		} catch (Throwable e) {
			throw new RuntimeException("error while copy object",e);
		} 
		copy(from,to);
		return to;
	}

	@Override
	public void listCopy(List list, Class toClass) {
		if(list.isEmpty()){
			return;
		}
		Class fromClass=list.get(0).getClass();
		BeanCopier beanCopier = BeanCopier.create(fromClass,toClass, false); 
		for(int i=0,len=list.size();i<len;i++){
			Object from=list.remove(i);
			Object to=null;
			try {
				to = toClass.newInstance();
			} catch (Throwable e) {
				throw new RuntimeException("error while copy object",e);
			}
			beanCopier.copy(from, to, null);
			list.add(i, to);
		}
	}

	@Override
	public Object prop(Object father, String son) {
		try {
			String getMethodName="get"+son.substring(0,1).toUpperCase()+son.substring(1);
			final Method getMethod = father.getClass().getMethod(getMethodName, new Class[] {});
			getMethod.setAccessible(true);
			return getMethod.invoke(father, new Object[] {});
			/*BeanMap beanMap=BeanMap.create(father);
			return beanMap.get(son);*/
		} catch (Throwable e) {
			throw new RuntimeException("error while prop object",e);
		} 
	}

}
