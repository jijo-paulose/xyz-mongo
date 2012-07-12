package xyz.mongo;

public interface IBizHandler {
	 /**
	  * 添加对于业务逻辑处理
	  * @param args
	  * @return
	  */
      Object exec(Object...args);
}
