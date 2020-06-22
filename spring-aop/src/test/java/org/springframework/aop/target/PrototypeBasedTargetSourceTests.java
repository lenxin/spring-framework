package org.springframework.aop.target;

import org.junit.Test;

import org.springframework.aop.TargetSource;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.tests.sample.beans.SerializablePerson;
import org.springframework.tests.sample.beans.TestBean;
import org.springframework.util.SerializationTestUtils;

import static org.junit.Assert.*;

/**
 * Unit tests relating to the abstract {@link AbstractPrototypeBasedTargetSource}
 * and not subclasses.
 *
 * @author Rod Johnson
 * @author Chris Beams
 */
public class PrototypeBasedTargetSourceTests {

	@Test
	public void testSerializability() throws Exception {
		MutablePropertyValues tsPvs = new MutablePropertyValues();
		tsPvs.add("targetBeanName", "person");
		RootBeanDefinition tsBd = new RootBeanDefinition(TestTargetSource.class);
		tsBd.setPropertyValues(tsPvs);

		MutablePropertyValues pvs = new MutablePropertyValues();
		RootBeanDefinition bd = new RootBeanDefinition(SerializablePerson.class);
		bd.setPropertyValues(pvs);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);

		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		bf.registerBeanDefinition("ts", tsBd);
		bf.registerBeanDefinition("person", bd);

		TestTargetSource cpts = (TestTargetSource) bf.getBean("ts");
		TargetSource serialized = (TargetSource) SerializationTestUtils.serializeAndDeserialize(cpts);
		assertTrue("Changed to SingletonTargetSource on deserialization",
				serialized instanceof SingletonTargetSource);
		SingletonTargetSource sts = (SingletonTargetSource) serialized;
		assertNotNull(sts.getTarget());
	}


	private static class TestTargetSource extends AbstractPrototypeBasedTargetSource {

		private static final long serialVersionUID = 1L;

		/**
		 * Nonserializable test field to check that subclass
		 * state can't prevent serialization from working
		 */
		private TestBean thisFieldIsNotSerializable = new TestBean();

		@Override
		public Object getTarget() throws Exception {
			return newPrototypeInstance();
		}

		@Override
		public void releaseTarget(Object target) throws Exception {
			// Do nothing
		}
	}

}
