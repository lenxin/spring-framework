package org.springframework.beans.factory.parsing;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.tests.sample.beans.TestBean;

import static org.junit.Assert.*;
import static org.springframework.tests.TestResourceUtils.*;

/**
 * @author Rob Harrop
 * @author Chris Beams
 * @since 2.0
 */
public class CustomProblemReporterTests {

	private CollatingProblemReporter problemReporter;

	private DefaultListableBeanFactory beanFactory;

	private XmlBeanDefinitionReader reader;


	@Before
	public void setup() {
		this.problemReporter = new CollatingProblemReporter();
		this.beanFactory = new DefaultListableBeanFactory();
		this.reader = new XmlBeanDefinitionReader(this.beanFactory);
		this.reader.setProblemReporter(this.problemReporter);
	}


	@Test
	public void testErrorsAreCollated() {
		this.reader.loadBeanDefinitions(qualifiedResource(CustomProblemReporterTests.class, "context.xml"));
		assertEquals("Incorrect number of errors collated", 4, this.problemReporter.getErrors().length);

		TestBean bean = (TestBean) this.beanFactory.getBean("validBean");
		assertNotNull(bean);
	}


	private static class CollatingProblemReporter implements ProblemReporter {

		private final List<Problem> errors = new ArrayList<>();

		private final List<Problem> warnings = new ArrayList<>();

		@Override
		public void fatal(Problem problem) {
			throw new BeanDefinitionParsingException(problem);
		}

		@Override
		public void error(Problem problem) {
			this.errors.add(problem);
		}

		public Problem[] getErrors() {
			return this.errors.toArray(new Problem[this.errors.size()]);
		}

		@Override
		public void warning(Problem problem) {
			this.warnings.add(problem);
		}
	}

}
