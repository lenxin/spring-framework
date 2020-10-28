package org.springframework.orm.jpa;

import org.junit.Test;

import static org.mockito.BDDMockito.*;

/**


 */
public class EntityManagerFactoryBeanSupportTests extends AbstractEntityManagerFactoryBeanTests {

	@Test
	public void testHookIsCalled() throws Exception {
		DummyEntityManagerFactoryBean demf = new DummyEntityManagerFactoryBean(mockEmf);

		demf.afterPropertiesSet();

		checkInvariants(demf);

		// Should trigger close method expected by EntityManagerFactory mock
		demf.destroy();

		verify(mockEmf).close();
	}

}
