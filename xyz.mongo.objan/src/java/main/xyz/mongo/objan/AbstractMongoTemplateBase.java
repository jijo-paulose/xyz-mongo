package xyz.mongo.objan;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import xyz.mongo.Base;

public class AbstractMongoTemplateBase<Entity,ID> extends Base<Entity,ID> implements ApplicationContextAware {
	protected Log LOG=LogFactory.getLog(getClass());
	private ApplicationContext contxet = null;

	protected MongoTemplate template;

	private String templateName = "mongoTemplate";

	protected IDom dom;

	@Override
	public void setApplicationContext(ApplicationContext contxet)
			throws BeansException {
		this.contxet = contxet;
		template = (MongoTemplate) contxet.getBean(templateName);
		if (null == dom) {
			dom = contxet.getBean(IDom.class);
		}
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public void setTemplate(MongoTemplate template) {
		this.template = template;
	}

	public void setDom(IDom dom) {
		this.dom = dom;
	}

	/**
	 * 删除实体，实体ID不能为空
	 * 
	 * @param en
	 * @throws Exception
	 */
	public void doDelete(Entity en) throws Exception {
		template.remove(en);
	}
	
	public void doUpdateProByIds(final List<ID> ids,final String proName,final Object proValue)throws Exception{
		dom.doUpdate(new IUpdateAble() {
			public void queryUpdate(Query query, Update update) {
				query.addCriteria(where("_id").in(ids));
				update.set(proName, proValue);
			}

		}, entityClass);
	}
    public void doUpdateProsByIds(final List<ID> ids,final Map<String,Object>nameAndValues)throws Exception{
    	dom.doUpdate(new IUpdateAble() {
			public void queryUpdate(Query query, Update update) {
				query.addCriteria(where("_id").in(ids));
				for(Iterator<String> it=nameAndValues.keySet().iterator();it.hasNext();){
					String key=it.next();
					Object value=nameAndValues.get(key);
					update.set(key, value);
				}
			}

		}, entityClass);
    }
    public void doUpdateProByIds(ID[] ids,String proName,Object proValue)throws Exception{
    	List<ID> idList=new ArrayList();
    	for(ID id:ids){
    		idList.add(id);
    	}
    	doUpdateProByIds(idList,proName,proValue);
    }
    public void doUpdateProsByIds(ID[] ids,Map<String,Object>nameAndValues)throws Exception{
    	List<ID> idList=new ArrayList();
    	for(ID id:ids){
    		idList.add(id);
    	}
    	doUpdateProsByIds(idList,nameAndValues);
    }
	/**
	 * 根据id更新一个属性，在后台有好多这样的查找之后的操作，不用写dao了
	 * @param id
	 * @param proName
	 * @param proValue
	 * @throws Exception
	 */
	public void doUpdateProById(final ID id,final String proName,final Object proValue)throws Exception{
		dom.doUpdate(new IUpdateAble() {
			public void queryUpdate(Query query, Update update) {
				query.addCriteria(where("_id").is(id));
				update.set(proName, proValue);
			}

		}, entityClass);
	}
	/**
	 * 根据id更新多个属性，这个的应用好像没有上一个多
	 * @param id
	 * @param proName
	 * @param proValue
	 * @throws Exception
	 */
    public void doUpdateProsById(final ID id,final Map<String,Object>nameAndValues)throws Exception{
    	dom.doUpdate(new IUpdateAble() {
			public void queryUpdate(Query query, Update update) {
				query.addCriteria(where("_id").is(id));
				for(Iterator<String> it=nameAndValues.keySet().iterator();it.hasNext();){
					String key=it.next();
					Object value=nameAndValues.get(key);
					update.set(key, value);
				}
			}

		}, entityClass);
    }
    
    public void doDeleteByIds(final List<ID> ids)throws Exception{
    	template.remove(getIdsQuery(ids), entityClass);
    }
    
    private Query getIdsQuery(List<ID> ids) {
		return new Query(getIdsCriteria(ids));
	}

	private Criteria getIdsCriteria(List<ID> ids) {
		return where("_id").in(ids);
	}
    
    public void doDeleteByIds(ID[] ids)throws Exception{
    	List<ID> idList=new ArrayList();
    	for(ID id:ids){
    		idList.add(id);
    	}
    	doDeleteByIds(idList);
    }
    
    private Query getIdQuery(ID id) {
		return new Query(getIdCriteria(id));
	}

	private Criteria getIdCriteria(ID id) {
		return where("_id").is(id);
	}

	/**
	 * 根据id删除实体,logic删除
	 * 
	 * @param id
	 *            删除实体id
	 * @throws Exception
	 */
	public void doDeleteById(ID id) throws Exception {
		 template.remove(getIdQuery(id), entityClass);
	}


	/**
	 * 插入一个实体
	 * 
	 * @param en
	 *            插入实体
	 * @throws Exception
	 */
	public void doInsert(Entity en) throws Exception {
		template.insert(en);
	}

	/**
	 * 插入或者更新实体，可能根据实体id判断做什么操作
	 * 
	 * @param en
	 *            实体
	 * @throws Exception
	 */
	public void doInsertOrUpdate(Entity en) throws Exception {
		template.save(en);
	}

	/**
	 * 更新实体,实体ID不能为空
	 * 
	 * @param en
	 *            更新实体
	 * @throws Exception
	 */
	public void doUpdate(Entity en) throws Exception {
		template.save(en);

	}

	/**
	 * 得到一个类型的全部实体
	 * 
	 * @return 返回一个list
	 * @throws Exception
	 */
	public List<Entity> findAll() throws Exception {
		return template.findAll(entityClass);
	}

	/**
	 * 根据id获取实体
	 * 
	 * @param id
	 *            输入的参数
	 * @return 实体
	 * @throws Exception
	 */
	public Entity findById(ID id) throws Exception {
		return (Entity) template.findById(id, entityClass);
	}
}
