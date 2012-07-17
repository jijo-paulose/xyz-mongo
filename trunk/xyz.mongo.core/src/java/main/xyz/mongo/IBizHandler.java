package xyz.mongo;

public interface IBizHandler {
	 /**
	  * json
	  * {
	  * ver:0.1,
	  * res:现在的结果
	  * err:{
	  *     msg:消息,
	  *     stack:堆栈
	  * }
	  * }
	  * 添加对于业务逻辑处理
	  * @param args
	  * @return
	  */
      Object exec(Object...args);
}
