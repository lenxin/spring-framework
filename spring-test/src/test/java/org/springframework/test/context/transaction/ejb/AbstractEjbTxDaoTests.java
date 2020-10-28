package org.springframework.test.context.transaction.ejb;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.ejb.dao.TestEntityDao;

import static org.junit.Assert.*;

/**
 * Abstract base class for all tests involving EJB transaction support in the
 * TestContext framework.
 *


 * @since 4.0.1
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public abstract class AbstractEjbTxDaoTests extends AbstractTransactionalJUnit4SpringContextTests {

	protected static final String TEST_NAME = "test-name";

	@EJB
	protected TestEntityDao dao;

	@PersistenceContext
	protected EntityManager em;


	@Test
	public void test1InitialState() {
		int count = dao.getCount(TEST_NAME);
		assertEquals("New TestEntity should have count=0.", 0, count);
	}

	@Test
	public void test2IncrementCount1() {
		int count = dao.incrementCount(TEST_NAME);
		assertEquals("Expected count=1 after first increment.", 1, count);
	}

	/**
	 * The default implementation of this method assumes that the transaction
	 * for {@link #test2IncrementCount1()} was committed. Therefore, it is
	 * expected that the previous increment has been persisted in the database.
	 */
	@Test
	public void test3IncrementCount2() {
		int count = dao.getCount(TEST_NAME);
		assertEquals("Expected count=1 after test2IncrementCount1().", 1, count);

		count = dao.incrementCount(TEST_NAME);
		assertEquals("Expected count=2 now.", 2, count);
	}

	@After
	public void synchronizePersistenceContext() {
		em.flush();
	}

}
