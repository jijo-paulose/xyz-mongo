package xyz.mongo.objan.simple.an;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import xyz.mongo.objan.exec.ExecuterInterceptor;
import xyz.mongo.objan.exec.IExecuter;
import xyz.mongo.objan.exec.IExecuterManager;
import xyz.mongo.objan.exec.IParamManager;
import xyz.mongo.objan.exec.impl.AnParamManager;
import xyz.mongo.objan.exec.impl.DefaultAnUtil;
import xyz.mongo.objan.exec.impl.DefaultExecuterManager;
import xyz.mongo.objan.exec.impl.an.TestMethodInvocation;

public class SimpleAnTest {
	private ExecuterInterceptor executerInterceptor;
	// private MethodInvocation invocation
	private IExecuterManager executerManager;

	private IParamManager paramManager;

	@Before
	public void setUp() throws Exception {
		executerInterceptor = new ExecuterInterceptor();
		initExecuterManager();
		executerInterceptor.setExecuterManager(executerManager);
		initParamManager();
		executerInterceptor.setParamManager(paramManager);
	}

	private void initExecuterManager() throws Exception {
		DefaultExecuterManager dem=new DefaultExecuterManager();
		DefaultAnUtil anUtil=new DefaultAnUtil();
		dem.setAnUtil(anUtil);
		IExecuter test=new TestExecuter();
		dem.addExecuter("test", test);
		executerManager=dem;
	}

	private void initParamManager() throws Exception {
		paramManager=new AnParamManager();
	}

	@Test
	public void test() throws Throwable{
		TestMethodInvocation invocation=new TestMethodInvocation();
		AnObj anobj=new AnObj();
		invocation.setThis(anobj);
		Method method=AnObj.class.getDeclaredMethod("test", First.class);
		invocation.setMethod(method);
		invocation.setArguments(new Object[]{new First()});
		Object res=executerInterceptor.invoke(invocation);
		Assert.assertNotNull(res);
		Assert.assertEquals(First.class, res.getClass());
		First resFirst=(First)res;
		Assert.assertEquals("myId", resFirst.getId());
	}

}
