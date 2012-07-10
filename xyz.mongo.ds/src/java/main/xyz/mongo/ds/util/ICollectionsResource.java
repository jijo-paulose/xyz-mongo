package xyz.mongo.ds.util;

import com.mongodb.DBObject;

public interface ICollectionsResource {
	/**
	 * DBObject的结构可以扩展,比用java对象要好
	 * 优点是不用定义java类,就可以自在的扩展啊,哈哈
	 * 现在是:还有就是多个之间join和传递的关系啦
	 * {comment:注释，这个编程简单了，用户的文档可就麻烦了，所以，必须滴,
     *  argsCheck:[多个字符串，输入参数校验],
     *  method:方法名字,
     *  type:类型，比如page，list，one等,
     *  value:这是一个json字符串，条件，累死sql的where子句,
     *  fields:这是查找时返回值的
     *  update:这是更新操作的语句等
     *  errmsg:例外消息，可以传参，以后一点点完善，呵呵
     * }
     * 这个不好使，最好将细节封装进来
	 * @return
	 */
	//Map<String,DBObject> getQuerys();
	
	DBObject methodQuery(String colName,String methodName);
}
