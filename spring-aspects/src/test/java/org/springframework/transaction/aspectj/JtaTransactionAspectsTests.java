package org.springframework.transaction.aspectj;

import java.io.IOException;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.tests.transaction.CallCountingTransactionManager;

import static org.junit.Assert.*;

/**

 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JtaTransactionAspectsTests.Config.class)
public class JtaTransactionAspectsTests {

	@Autowired
	private CallCountingTransactionManager txManager;

	@Before
	public void setUp() {
		this.txManager.clear();
	}

	@Test
	public void commitOnAnnotatedPublicMethod() throws Throwable {
		assertEquals(0, this.txManager.begun);
		new JtaAnnotationPublicAnnotatedMember().echo(null);
		assertEquals(1, this.txManager.commits);
	}

	@Test
	public void matchingRollbackOnApplied() throws Throwable {
		assertEquals(0, this.txManager.begun);
		InterruptedException test = new InterruptedException();
		try {
			new JtaAnnotationPublicAnnotatedMember().echo(test);
			fail("Should have thrown an exception");
		}
		catch (Throwable throwable) {
			assertEquals("wrong exception", test, throwable);
		}
		assertEquals(1, this.txManager.rollbacks);
		assertEquals(0, this.txManager.commits);
	}

	@Test
	public void nonMatchingRollbackOnApplied() throws Throwable {
		assertEquals(0, this.txManager.begun);
		IOException test = new IOException();
		try {
			new JtaAnnotationPublicAnnotatedMember().echo(test);
			fail("Should have thrown an exception");
		}
		catch (Throwable throwable) {
			assertEquals("wrong exception", test, throwable);
		}
		assertEquals(1, this.txManager.commits);
		assertEquals(0, this.txManager.rollbacks);
	}

	@Test
	public void commitOnAnnotatedProtectedMethod() {
		assertEquals(0, this.txManager.begun);
		new JtaAnnotationProtectedAnnotatedMember().doInTransaction();
		assertEquals(1, this.txManager.commits);
	}

	@Test
	public void nonAnnotatedMethodCallingProtectedMethod() {
		assertEquals(0, this.txManager.begun);
		new JtaAnnotationProtectedAnnotatedMember().doSomething();
		assertEquals(1, this.txManager.commits);
	}

	@Test
	public void commitOnAnnotatedPrivateMethod() {
		assertEquals(0, this.txManager.begun);
		new JtaAnnotationPrivateAnnotatedMember().doInTransaction();
		assertEquals(1, this.txManager.commits);
	}

	@Test
	public void nonAnnotatedMethodCallingPrivateMethod() {
		assertEquals(0, this.txManager.begun);
		new JtaAnnotationPrivateAnnotatedMember().doSomething();
		assertEquals(1, this.txManager.commits);
	}

	@Test
	public void notTransactional() {
		assertEquals(0, this.txManager.begun);
		new TransactionAspectTests.NotTransactional().noop();
		assertEquals(0, this.txManager.begun);
	}


	public static class JtaAnnotationPublicAnnotatedMember {

		@Transactional(rollbackOn = InterruptedException.class)
		public void echo(Throwable t) throws Throwable {
			if (t != null) {
				throw t;
			}
		}

	}


	protected static class JtaAnnotationProtectedAnnotatedMember {

		public void doSomething() {
			doInTransaction();
		}

		@Transactional
		protected void doInTransaction() {
		}
	}


	protected static class JtaAnnotationPrivateAnnotatedMember {

		public void doSomething() {
			doInTransaction();
		}

		@Transactional
		private void doInTransaction() {
		}
	}


	@Configuration
	protected static class Config {

		@Bean
		public CallCountingTransactionManager transactionManager() {
			return new CallCountingTransactionManager();
		}

		@Bean
		public JtaAnnotationTransactionAspect transactionAspect() {
			JtaAnnotationTransactionAspect aspect = JtaAnnotationTransactionAspect.aspectOf();
			aspect.setTransactionManager(transactionManager());
			return aspect;
		}
	}

}
