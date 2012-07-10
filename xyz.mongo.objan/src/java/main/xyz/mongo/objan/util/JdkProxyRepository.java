package xyz.mongo.objan.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zmc
 */
public class JdkProxyRepository {
	/**
     *
     */
	public static final Class[] PROXY_CTOR_ARGUMENT_TYPES = new Class[] { InvocationHandler.class };

	/**
     *
     */
	private static final JdkProxyRepository S_INSTANCE = new JdkProxyRepository();

	/**
	 * 
	 * @return
	 */
	public static JdkProxyRepository getInstance() {
		return S_INSTANCE;
	}

	/**
	 * 
	 * @param handler
	 * @param _targetInterface
	 * @return
	 */
	public static Object createDefaultProxy(InvocationHandler handler,
			Class targetInterface) throws Exception{
		return getInstance().createProxy(handler, targetInterface);
	}

	/**
     *
     */
	private Map proxyClassMap = new HashMap();

	/**
	 * 
	 * @param targetInterface
	 * @return
	 */
	public Class resolveClass(Class targetInterface) {
		Class proxyClass = (Class) proxyClassMap.get(targetInterface);
		if (proxyClass == null) {
			proxyClass = Proxy.getProxyClass(targetInterface.getClassLoader(),
					new Class[] { targetInterface });
			proxyClassMap.put(targetInterface, proxyClass);
		}
		return proxyClass;
	}

	/**
	 * 
	 * @param targetInterface
	 * @return
	 */
	public Constructor resolveConstructor(Class targetInterface)
			throws Exception {
		Class proxyClass = resolveClass(targetInterface);
		return proxyClass.getConstructor(PROXY_CTOR_ARGUMENT_TYPES);
	}

	/**
	 * 
	 * @param _handler
	 * @param _targetInterface
	 * @return
	 */
	public Object createProxy(InvocationHandler handler, Class targetInterface)
			throws Exception {
		if (handler == null) {
			throw new NullPointerException("createProxy( null, Class )");
		}
		Constructor ctor = resolveConstructor(targetInterface);

		return ctor.newInstance(new Object[] { handler });

	}

}
