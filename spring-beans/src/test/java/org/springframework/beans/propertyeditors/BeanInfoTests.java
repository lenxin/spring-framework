package org.springframework.beans.propertyeditors;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import org.junit.Test;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import static org.junit.Assert.*;

/**

 * @since 06.03.2006
 */
public class BeanInfoTests {

	@Test
	public void testComplexObject() {
		ValueBean bean = new ValueBean();
		BeanWrapper bw = new BeanWrapperImpl(bean);
		Integer value = new Integer(1);

		bw.setPropertyValue("value", value);
		assertEquals("value not set correctly", bean.getValue(), value);

		value = new Integer(2);
		bw.setPropertyValue("value", value.toString());
		assertEquals("value not converted", bean.getValue(), value);

		bw.setPropertyValue("value", null);
		assertNull("value not null", bean.getValue());

		bw.setPropertyValue("value", "");
		assertNull("value not converted to null", bean.getValue());
	}


	public static class ValueBean {

		private Integer value;

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}
	}


	public static class ValueBeanBeanInfo extends SimpleBeanInfo {

		@Override
		public PropertyDescriptor[] getPropertyDescriptors() {
			try {
				PropertyDescriptor pd = new PropertyDescriptor("value", ValueBean.class);
				pd.setPropertyEditorClass(MyNumberEditor.class);
				return new PropertyDescriptor[] {pd};
			}
			catch (IntrospectionException ex) {
				throw new FatalBeanException("Couldn't create PropertyDescriptor", ex);
			}
		}
	}


	public static class MyNumberEditor extends CustomNumberEditor {

		private Object target;

		public MyNumberEditor() throws IllegalArgumentException {
			super(Integer.class, true);
		}

		public MyNumberEditor(Object target) throws IllegalArgumentException {
			super(Integer.class, true);
			this.target = target;
		}

		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			Assert.isTrue(this.target instanceof ValueBean, "Target must be available");
			super.setAsText(text);
		}

	}

}
