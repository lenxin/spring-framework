

package org.springframework.web.servlet.function;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import org.springframework.mock.web.test.MockHttpServletRequest;

import static org.junit.Assert.*;

/**
 * @author Arjen Poutsma
 */
public class RequestPredicateTests {

	private ServerRequest request;

	@Before
	public void createRequest() {
		this.request = new DefaultServerRequest(new MockHttpServletRequest(),
				Collections.emptyList());
	}

	@Test
	public void and() {
		RequestPredicate predicate1 = request -> true;
		RequestPredicate predicate2 = request -> true;
		RequestPredicate predicate3 = request -> false;

		assertTrue(predicate1.and(predicate2).test(request));
		assertTrue(predicate2.and(predicate1).test(request));
		assertFalse(predicate1.and(predicate3).test(request));
	}

	@Test
	public void negate() {
		RequestPredicate predicate = request -> false;
		RequestPredicate negated = predicate.negate();

		assertTrue(negated.test(request));

		predicate = request -> true;
		negated = predicate.negate();

		assertFalse(negated.test(request));
	}

	@Test
	public void or() {
		RequestPredicate predicate1 = request -> true;
		RequestPredicate predicate2 = request -> false;
		RequestPredicate predicate3 = request -> false;

		assertTrue(predicate1.or(predicate2).test(request));
		assertTrue(predicate2.or(predicate1).test(request));
		assertFalse(predicate2.or(predicate3).test(request));
	}

}
