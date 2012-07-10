package xyz.mongo.objan;


/**
 * MongoDb Document & Object Mapping interface<br>
 * 利用反射和MongoTemplate简化实体对象的构造<p>
 * @author zmc
 * @since  0.1 version
 */
public interface IDom {
	void doUpdate(IUpdateAble updateAble,Class collectionClass);
	 /**
	  * 根据ID得到一个Collection的部分内容，并将它包含在一个Domain实例之中
	  * @param <ID>
	  * @param id id
	  * @param domainClass 域对象类名
	  * @param collectionClass Collection类名
	  * @return 返回域对象
	  * @throws Exception
	  */
   <ID> Object findById(ID id,Class<? extends Object> domainClass,Class<? extends Object> collectionClass)throws Exception;
}
