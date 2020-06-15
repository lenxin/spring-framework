package org.springframework.cache.aspectj;

import org.springframework.cache.jcache.config.AbstractJCacheAnnotationTests;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * @author Stephane Nicoll
 */
public class JCacheAspectJNamespaceConfigTests extends AbstractJCacheAnnotationTests {

	@Override
	protected ApplicationContext getApplicationContext() {
		return new GenericXmlApplicationContext(
				"/org/springframework/cache/config/annotation-jcache-aspectj.xml");
	}

}
