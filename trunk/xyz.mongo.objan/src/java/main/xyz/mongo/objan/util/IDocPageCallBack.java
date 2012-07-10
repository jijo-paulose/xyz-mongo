package xyz.mongo.objan.util;

import java.util.List;

/**
 * MongoDb Document 分页回调接口<br>
 * 其实也可以用在不是分页的地方，只要是返回实体列表的地方都可以用<p>
 * @author kara.zhou
 * @since  0.1 version
 */
public interface IDocPageCallBack<Entity>{
	/**
	 * 返回实体列表
	 * @return 实体列表
	 * @throws Exception
	 */
	List<Entity> process()throws Exception;
	
}
