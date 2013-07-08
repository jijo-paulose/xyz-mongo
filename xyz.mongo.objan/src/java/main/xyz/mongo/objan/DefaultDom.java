package xyz.mongo.objan;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class DefaultDom implements IDom{
    private MongoTemplate template;
    
	@Override
	public void doUpdate(IUpdateAble updateAble, Class collectionClass) {
    	Query query = new Query();
		Update update = new Update();
		updateAble.queryUpdate(query, update);
		template.updateMulti(query, update, collectionClass); 
	}

	@Override
	public <ID> Object findById(ID id, Class<? extends Object> domainClass,
			Class<? extends Object> collectionClass) throws Exception {
		throw new RuntimeException("不支持的操作");
	}

	public void setTemplate(MongoTemplate template) {
		this.template = template;
	}

}
