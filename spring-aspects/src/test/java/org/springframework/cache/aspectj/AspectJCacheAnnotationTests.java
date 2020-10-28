package org.springframework.cache.aspectj;

import org.junit.Assert;
import org.junit.Test;

import org.springframework.cache.Cache;
import org.springframework.cache.config.AbstractCacheAnnotationTests;
import org.springframework.cache.config.CacheableService;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import static org.junit.Assert.*;

/**

 */
public class AspectJCacheAnnotationTests extends AbstractCacheAnnotationTests {

	@Override
	protected ConfigurableApplicationContext getApplicationContext() {
		return new GenericXmlApplicationContext(
				"/org/springframework/cache/config/annotation-cache-aspectj.xml");
	}

	@Test
	public void testKeyStrategy() throws Exception {
		AnnotationCacheAspect aspect = ctx.getBean(
				"org.springframework.cache.config.internalCacheAspect", AnnotationCacheAspect.class);
		Assert.assertSame(ctx.getBean("keyGenerator"), aspect.getKeyGenerator());
	}

	@Override
	public void testMultiEvict(CacheableService<?> service) {
		Object o1 = new Object();

		Object r1 = service.multiCache(o1);
		Object r2 = service.multiCache(o1);

		Cache primary = cm.getCache("primary");
		Cache secondary = cm.getCache("secondary");

		assertSame(r1, r2);
		assertSame(r1, primary.get(o1).get());
		assertSame(r1, secondary.get(o1).get());

		service.multiEvict(o1);
		assertNull(primary.get(o1));
		assertNull(secondary.get(o1));

		Object r3 = service.multiCache(o1);
		Object r4 = service.multiCache(o1);
		assertNotSame(r1, r3);
		assertSame(r3, r4);

		assertSame(r3, primary.get(o1).get());
		assertSame(r4, secondary.get(o1).get());
	}

}
