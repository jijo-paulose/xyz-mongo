package xyz.mongo.objan.util;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import xyz.mongo.IPage;
import xyz.mongo.IPageRequest;

/**
 * MongoDb Document 分页工具类<br>
 * 使用模板和回调简化编程<p>
 * @author kara.zhou
 * @since  0.1 version
 */
public final class PageUtil {
	private static final Log LOG=LogFactory.getLog(PageUtil.class);
	
	private PageUtil(){}
	
	/**
	 * 得到分页
	 * @param <Entity>
	 * @param <ID>
	 * @param pageRequest 分页请求
	 * @param template  MongoTemplate
	 * @param entityClass Collection类名
	 * @param queryIn 输入查询
	 * @param callBack 构造一页列表的回调接口
	 * @return 分页
	 * @throws Exception
	 */
	public static <Entity, ID> IPage<Entity, ID> getPage(
			IPageRequest pageRequest, MongoTemplate template,
			Class entityClass,Query queryIn, IDocPageCallBack<Entity> callBack) throws Exception {
		long pageNumber = pageRequest.getNumber();
		long pageSize = pageRequest.getSize();
		int cnt=pageRequest.getCnt();
		long skip=pageSize*(pageNumber-1);
		if(0>=skip){
			skip=0;
		}
		
		DefaultPage<Entity, ID> page = new DefaultPage<Entity, ID>();
		page.setNumber(pageNumber);// number
		page.setSize(pageSize);// size
		if(IPageRequest.CNT_GET==cnt){
			Query query =queryIn;
			long totalCnt= template.count(query, entityClass);
			if (totalCnt == 0) {
				return page;
			}
			page.setTotalCnt(totalCnt);
			long totalPages=totalCnt / pageSize;
			if (0 < (totalCnt % pageSize)) {
				totalPages++;
			}
			page.setTotalPages(totalPages);
		}
		queryIn.skip((int)skip).limit((int)pageSize);
		
		// 使用回调得到具体内容
		List<Entity> result = callBack.process();
		page.setResult(result);
		page.setNowSize(result.size());

		return page;
	}

}
