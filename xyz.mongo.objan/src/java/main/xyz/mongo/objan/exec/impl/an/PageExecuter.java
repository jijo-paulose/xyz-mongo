package xyz.mongo.objan.exec.impl.an;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.util.StringUtils;

import xyz.mongo.IPageRequest;
import xyz.mongo.objan.exec.IAnUtil;
import xyz.mongo.objan.exec.IExecuter;
import xyz.mongo.objan.util.DvUtil;
import xyz.mongo.objan.util.IDocPageCallBack;
import xyz.mongo.objan.util.PageUtil;
import xyz.mongo.util.IBeanUtil;
import xyz.mongo.util.IJsonStringUtil;
import xyz.mongo.util.impl.FreemarkerJsonStringUtil;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public  class PageExecuter implements IExecuter {
	private static final Log LOG = LogFactory.getLog(PageExecuter.class);

	protected MongoTemplate template;
	

	protected IJsonStringUtil jsonStringUtil = FreemarkerJsonStringUtil
			.getInstance();

	protected IAnUtil anUtil;
	protected IBeanUtil beanUtil;

	@Override
	public Object exec(MethodInvocation invocation, Map<String, Object> params)throws Exception {
		//这个地方问题很多，需要好好考虑，考虑
		Method method = invocation.getMethod();
		final Page  page= (Page) anUtil.getAn(method, Page.class);
		if (null == page) {
			throw new IllegalStateException("Lists can't be null 4 method:"
					+ method.getName());
		}
		final Class entityClass = (Class) params.get("entityClass");
		String realValue = null;
		DV[] dvs = page.dv();
		if (0 != dvs.length) {
			realValue = DvUtil.dv(dvs, jsonStringUtil, params);
		} else {
			String value = page.value();
			realValue = jsonStringUtil.merge(value, params);
		}
		String fields=page.fields();
		String realFields=jsonStringUtil.merge(fields, params);
		final BasicQuery query = new BasicQuery(realValue,realFields);
		String sorts=page.sorts();
		if (StringUtils.hasText(sorts)) {
			DBObject sortObject = (DBObject) JSON.parse(sorts);
			query.setSortObject(sortObject);
		}
		//同那个东西一样，属性拷贝的工作现在先不做吧
		final IPageRequest request=(IPageRequest)params.get("page");
		return (Object) PageUtil.getPage(request, template,
				entityClass,  query,
				new IDocPageCallBack() {

					public List process() throws Exception {
						List fromList=template.find(query, entityClass);
						if(null!=page.vo()){
							beanUtil.listCopy(fromList, page.vo());
						}
						return fromList;
					}

				});
	}
	
	

	public void setTemplate(MongoTemplate template) {
		this.template = template;
	}

	/**
	 * @param anUtil
	 *            the anUtil to set
	 */
	public void setAnUtil(IAnUtil anUtil) {
		this.anUtil = anUtil;
	}

	/**
	 * @param jsonStringUtil
	 *            the jsonStringUtil to set
	 */
	public void setJsonStringUtil(IJsonStringUtil jsonStringUtil) {
		this.jsonStringUtil = jsonStringUtil;
	}



	public void setBeanUtil(IBeanUtil beanUtil) {
		this.beanUtil = beanUtil;
	}


}
