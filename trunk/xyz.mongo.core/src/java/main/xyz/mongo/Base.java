package xyz.mongo;

import java.lang.reflect.ParameterizedType;

/**
 */

public abstract class Base <Entity,ID> implements IBase{
	/**
	 * 运行时类型
	 */
     protected Class<Entity> entityClass;
     
     /**
 	 * 运行时类型
 	 */
      protected Class<ID> idClass;
     
     /**
      * 根据实际情况初始化运行时类型
      */
     public Base(){
    	 entityClass =(Class<Entity>) ((ParameterizedType) getClass()
                 .getGenericSuperclass()).getActualTypeArguments()[0];
    	 idClass =(Class<ID>) ((ParameterizedType) getClass()
                 .getGenericSuperclass()).getActualTypeArguments()[1];
     }

	@Override
	public Class getEntityType() {
		return entityClass;
	}

	@Override
	public Class getIdType() {
		return idClass;
	}
}
