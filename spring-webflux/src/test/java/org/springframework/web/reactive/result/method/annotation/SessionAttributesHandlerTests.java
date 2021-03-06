package org.springframework.web.reactive.result.method.annotation;


import java.util.HashSet;

import org.junit.Test;

import org.springframework.mock.web.test.server.MockWebSession;
import org.springframework.tests.sample.beans.TestBean;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.server.WebSession;

import static java.util.Arrays.*;
import static org.junit.Assert.*;

/**
 * Test fixture with {@link SessionAttributesHandler}.
 * @author Rossen Stoyanchev
 */
public class SessionAttributesHandlerTests {

	private final SessionAttributesHandler sessionAttributesHandler =
			new SessionAttributesHandler(TestController.class);


	@Test
	public void isSessionAttribute() {
		assertTrue(this.sessionAttributesHandler.isHandlerSessionAttribute("attr1", String.class));
		assertTrue(this.sessionAttributesHandler.isHandlerSessionAttribute("attr2", String.class));
		assertTrue(this.sessionAttributesHandler.isHandlerSessionAttribute("simple", TestBean.class));
		assertFalse(this.sessionAttributesHandler.isHandlerSessionAttribute("simple", String.class));
	}

	@Test
	public void retrieveAttributes() {
		WebSession session = new MockWebSession();
		session.getAttributes().put("attr1", "value1");
		session.getAttributes().put("attr2", "value2");
		session.getAttributes().put("attr3", new TestBean());
		session.getAttributes().put("attr4", new TestBean());

		assertEquals("Named attributes (attr1, attr2) should be 'known' right away",
				new HashSet<>(asList("attr1", "attr2")),
				sessionAttributesHandler.retrieveAttributes(session).keySet());

		// Resolve 'attr3' by type
		sessionAttributesHandler.isHandlerSessionAttribute("attr3", TestBean.class);

		assertEquals("Named attributes (attr1, attr2) and resolved attribute (att3) should be 'known'",
				new HashSet<>(asList("attr1", "attr2", "attr3")),
				sessionAttributesHandler.retrieveAttributes(session).keySet());
	}

	@Test
	public void cleanupAttributes() {
		WebSession session = new MockWebSession();
		session.getAttributes().put("attr1", "value1");
		session.getAttributes().put("attr2", "value2");
		session.getAttributes().put("attr3", new TestBean());

		this.sessionAttributesHandler.cleanupAttributes(session);

		assertNull(session.getAttributes().get("attr1"));
		assertNull(session.getAttributes().get("attr2"));
		assertNotNull(session.getAttributes().get("attr3"));

		// Resolve 'attr3' by type
		this.sessionAttributesHandler.isHandlerSessionAttribute("attr3", TestBean.class);
		this.sessionAttributesHandler.cleanupAttributes(session);

		assertNull(session.getAttributes().get("attr3"));
	}

	@Test
	public void storeAttributes() {

		ModelMap model = new ModelMap();
		model.put("attr1", "value1");
		model.put("attr2", "value2");
		model.put("attr3", new TestBean());

		WebSession session = new MockWebSession();
		sessionAttributesHandler.storeAttributes(session, model);

		assertEquals("value1", session.getAttributes().get("attr1"));
		assertEquals("value2", session.getAttributes().get("attr2"));
		assertTrue(session.getAttributes().get("attr3") instanceof TestBean);
	}


	@SessionAttributes(names = { "attr1", "attr2" }, types = { TestBean.class })
	private static class TestController {
	}

}
