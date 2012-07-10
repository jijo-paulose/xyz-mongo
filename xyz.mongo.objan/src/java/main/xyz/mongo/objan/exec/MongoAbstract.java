package xyz.mongo.objan.exec;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

/**
*
* Dao的工具类，利用它，可以将一个MyQuery方法返回对象的IPage的Dao实现做成抽象类，而不必实现：<p>
* 利用它，可以将一个MyGroup方法返回对象的List的Dao实现做成抽象类，而不必实现：<p>
* 这里使用了字节码技术，javassist工具包<p>
* public interface IAbcDao extends IBaseDao<Entity,ID>{    <p>
*@MyQuery(value="{_id:'${test.id}',name:'${test.name}'}",
*			fields="{id:1,name:1}",
*			domainClass=TestDomainEx.class,
*			collectionClass=TestDomainEx.class,
*			collectionName="testDomainEx"
*			)
*	IPage<TestDomain,String> findPageEx(@MyIn(name="test")TestDomainEx test,IPageRequest request);
* }<p>
* 实现就可以写成：<p>
* public abstract class AbcDaoImpl implements IAbcDao{<p>
* }<p>
* 而配置文件可以写成：
* <bean id="abcDao" class="com.myjob.core.dao.mongodb.query.DaoAbstract">
*      <property name="targetClass" value="AbcDaoImpl"/>
*      <property name="targetProperties">
*         <map>
*           &lt!--这里设置AbcDaoImpl的注入属性--&gt
*         </map>
*      </property>
*  </bean>
* @author kara.zhou
* @since  0.1 version
*/
public class MongoAbstract implements FactoryBean, BeanNameAware,
		BeanFactoryAware, DisposableBean, ApplicationContextAware,
		MessageSourceAware, ResourceLoaderAware,
		ApplicationEventPublisherAware, InitializingBean {
	private static final Log LOG = LogFactory.getLog(MongoAbstract.class);
	private static final String DAO_INIT_NAME="daoIniter";

	private Object target;

	private String targetClass;
	private Class targetType;

	private String beanName;

	private BeanFactory beanFactory;

	private ApplicationContext applicationContext;

	private MessageSource messageSource;

	private ResourceLoader resourceLoader;

	private ApplicationEventPublisher applicationEventPublisher;

	private Map<String, Object> targetProperties;
	
	
	

	 /**
	  * 根据Bean的实例，供getBean使用
	  * @return 返回域对象
	  * @throws Exception
	  */
	public Object getObject() throws Exception {
		if(LOG.isDebugEnabled()){
			LOG.debug("----get object--"+target);
		}
		return target;
	}

	public Class getObjectType() {
		if(LOG.isDebugEnabled()){
			LOG.debug("----get object type--"+targetType);
		}
		return targetType;
	}

	public boolean isSingleton() {
		return true;
	}

	// all for BeanNameAware
	public void setBeanName(String beanName) {
		LOG.debug("----beanName--"+beanName);
		this.beanName = beanName;
	}

	// all for BeanFactoryAware
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	// all of DisposableBean
	public void destroy() throws Exception {
		if (target instanceof DisposableBean) {
			((DisposableBean) target).destroy();
		}
	}

	// all of ApplicationContextAware
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.applicationContext = context;
	}

	// all of MessageSourceAware
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	// all of ResourceLoaderAware
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;

	}

	// all of ApplicationEventPublisherAware
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;

	}

	public String getTargetClass() {
		return targetClass;
	}

	/**
	 * 设置实际实现的类的全名
	 * @param targetClass
	 */
	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}

	/**
	 * Set target dao bean's properties. These properties will be copied into
	 * target bean. The classic usage is that target dao bean depends on another
	 * bean, for example a sequence dao. So in your spring context xml, there're
	 * something like following example: <bean id="orderDao"
	 * class="com.myjob.core.spring.dao.DaoAbstract"> <property
	 * name="targetType" value="com.myjob.test.test.impl.OrderDaoImpl" />
	 * <property name="targetProperties"> <map> <entry key="abc"><ref
	 * bean="def"/></entry> </map> </property> </bean> you should specifiy
	 * 
	 * @param targetProperties
	 */
	public void setTargetProperties(Map<String, Object> targetProperties) {
		this.targetProperties = targetProperties;
	}
	
	/**
	 * 在设置property之后初始化target对象，并且传递应该传递的上下文属性
	 * @throws Exception
	 */
	public void afterPropertiesSet()throws Exception {
		if(StringUtils.isEmpty(targetClass)){
			if(LOG.isInfoEnabled()){
				LOG.info("not set targetClass 4:"+beanName );
			}
			return;
		}else{
			LOG.info("can set targetClass 4:"+beanName +",targetClass-"+targetClass);
		}
		initTargetBean();
		if (target instanceof BeanNameAware) {
			((BeanNameAware) target).setBeanName(beanName);
		}

		if (target instanceof BeanFactoryAware) {
			((BeanFactoryAware) target).setBeanFactory(beanFactory);
		}
		if (target instanceof ResourceLoaderAware) {
			((ResourceLoaderAware) target).setResourceLoader(resourceLoader);
		}

		if (target instanceof ApplicationEventPublisherAware) {
			((ApplicationEventPublisherAware) target)
					.setApplicationEventPublisher(applicationEventPublisher);
		}

		if (target instanceof MessageSourceAware) {
			((MessageSourceAware) target).setMessageSource(messageSource);
		}

		if (target instanceof ApplicationContextAware) {
			((ApplicationContextAware) target)
					.setApplicationContext(applicationContext);
		}
		transProperties();
		if (target instanceof InitializingBean) {
			try {
				((InitializingBean) target).afterPropertiesSet();
			} catch (Throwable t) {
				if(LOG.isInfoEnabled()){
					LOG.info("error while after properties set");
				}
			}
		}
	}

	private void transProperties() {
		if (targetProperties != null) {
			PropertyDescriptor[] pds = PropertyUtils
					.getPropertyDescriptors(target.getClass());
			for (int i = 0; i < pds.length; i++) {
				String propName = pds[i].getName();
				Method wmethod = pds[i].getWriteMethod();
				if (wmethod == null
						|| StringUtils.equalsIgnoreCase("class", propName))
					continue;

				Object propValue = targetProperties.get(propName);
				if (propValue == null)
					continue;

				try {
					PropertyUtils.setProperty(target, propName, propValue);
				} catch (Throwable t) {
					if (LOG.isInfoEnabled()) {
						LOG.info("error while set propertis:", t);
					}
				}
			}
		}
	}

	private void initTargetBean() {
		try {
			IMongoIniter daoIniter=(IMongoIniter)applicationContext.getBean(DAO_INIT_NAME);
			daoIniter.init(this);
		} catch (Throwable t) {
			if(LOG.isErrorEnabled()){
				LOG.error("error in initTargetBean",t);
			}
		}
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Class getTargetType() {
		return targetType;
	}

	public void setTargetType(Class targetType) {
		this.targetType = targetType;
	}


}
