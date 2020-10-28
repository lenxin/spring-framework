package org.springframework.test.context.junit4.orm.repository;

import org.springframework.test.context.junit4.orm.domain.Person;

/**
 * Person Repository API.
 *

 * @since 3.0
 */
public interface PersonRepository {

	Person findByName(String name);

	Person save(Person person);

}
