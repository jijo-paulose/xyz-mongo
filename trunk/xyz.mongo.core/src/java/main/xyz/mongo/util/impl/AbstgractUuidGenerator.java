package xyz.mongo.util.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xyz.mongo.util.IUuidGenerator;

public abstract class AbstgractUuidGenerator implements IUuidGenerator {

	protected   Log LOG = LogFactory.getLog(getClass());

	protected boolean genLocal = true;


	public abstract String next();

	public void nextAndSet(Object obj) {
		if (genLocal) {
			try {
				//现在先不做这个
				PropertyUtils.setProperty(obj, "id", next());
			} catch (Throwable t) {
				if (LOG.isWarnEnabled()) {
					LOG.warn("Error when next and set 4:" + obj, t);
				}
			}
		}
	}

	public void setGenLocal(boolean genLocal) {
		this.genLocal = genLocal;
	}


}
