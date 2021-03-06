package org.springframework.test.context.junit4.annotation;

import org.junit.Test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.DelegatingSmartContextLoader;
import org.springframework.tests.sample.beans.Employee;

import static org.junit.Assert.*;

/**
 * Integration tests that verify support for configuration classes in
 * the Spring TestContext Framework in conjunction with the
 * {@link DelegatingSmartContextLoader}.
 *
 * @author Sam Brannen
 * @since 3.1
 */
@ContextConfiguration
public class DefaultLoaderBeanOverridingDefaultConfigClassesInheritedTests extends
		DefaultLoaderDefaultConfigClassesBaseTests {

	@Configuration
	static class Config {

		@Bean
		public Employee employee() {
			Employee employee = new Employee();
			employee.setName("Yoda");
			employee.setAge(900);
			employee.setCompany("The Force");
			return employee;
		}
	}


	@Test
	@Override
	public void verifyEmployeeSetFromBaseContextConfig() {
		assertNotNull("The employee should have been autowired.", this.employee);
		assertEquals("The employee bean should have been overridden.", "Yoda", this.employee.getName());
	}

}
