package org.springframework.test.context.transaction.ejb.dao;

/**
 * Test DAO for EJB transaction support in the TestContext framework.
 *


 * @since 4.0.1
 */
public interface TestEntityDao {

	int getCount(String name);

	int incrementCount(String name);

}
