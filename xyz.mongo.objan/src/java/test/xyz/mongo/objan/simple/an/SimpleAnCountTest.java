package xyz.mongo.objan.simple.an;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import xyz.mongo.objan.exec.ExecuterInterceptor;
import xyz.mongo.objan.exec.IExecuterManager;
import xyz.mongo.objan.exec.IParamManager;
import xyz.mongo.objan.exec.impl.AnParamManager;
import xyz.mongo.objan.exec.impl.DefaultAnUtil;
import xyz.mongo.objan.exec.impl.DefaultExecuterManager;
import xyz.mongo.objan.exec.impl.an.Count;
import xyz.mongo.objan.exec.impl.an.CountExecuter;
import xyz.mongo.objan.exec.impl.an.TestMethodInvocation;

import com.mongodb.Mongo;

public class SimpleAnCountTest {
	private ExecuterInterceptor executerInterceptor;

	private IExecuterManager executerManager;

	private IParamManager paramManager;
	
	private MongoTemplate template;

	@Before
	public void setUp() throws Exception {
		executerInterceptor = new ExecuterInterceptor();
		template=new MongoTemplate(new Mongo(),"jmwpt");
		initExecuterManager();
		executerInterceptor.setExecuterManager(executerManager);
		initParamManager();
		executerInterceptor.setParamManager(paramManager);
		if(template.collectionExists(First.class)){
			template.dropCollection(First.class);
		}
	}
	
	@After
	public void tearDown() throws Exception {
		if(template.collectionExists(First.class)){
			template.dropCollection(First.class);
		}
	}

	private void initExecuterManager() throws Exception {
		DefaultExecuterManager dem=new DefaultExecuterManager();
		DefaultAnUtil anUtil=new DefaultAnUtil();
		Map<String,String> anNameType=new HashMap<String,String>();
		anNameType.put("count", Count.class.getName());
		anUtil.setAnNames(anNameType);
		dem.setAnUtil(anUtil);
		CountExecuter count=new CountExecuter();
		count.setTemplate(template);
		count.setAnUtil(anUtil);
		dem.addExecuter("count", count);
		executerManager=dem;
	}

	private void initParamManager() throws Exception {
		paramManager=new AnParamManager();
	}

	@Test
	public void test() throws Throwable{
		TestMethodInvocation invocation=new TestMethodInvocation();
		CountObj anobj=new CountObj();
		invocation.setThis(anobj);
		Method method=CountObj.class.getDeclaredMethod("count", First.class);
		invocation.setMethod(method);
		invocation.setArguments(new Object[]{new First()});
		Object res=executerInterceptor.invoke(invocation);
		Assert.assertNotNull(res);
		Assert.assertEquals(Long.class, res.getClass());
		long resFirst=(Long)res;
		Assert.assertEquals(0, resFirst);
		//然后,插入5个
		for(int i=1;i<=5;i++){
			First first=new First();
			first.setName("name"+i);
			template.insert(first);
			long myres=(Long)executerInterceptor.invoke(invocation);
			Assert.assertEquals(i, myres);
		}
	}

}
