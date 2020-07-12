package org.springframework.core.serializer;

import java.io.NotSerializableException;
import java.io.Serializable;

import org.junit.Test;

import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializationFailedException;
import org.springframework.core.serializer.support.SerializingConverter;

import static org.junit.Assert.*;

/**
 * @author Gary Russell
 * @author Mark Fisher
 * @since 3.0.5
 */
public class SerializationConverterTests {

	@Test
	public void serializeAndDeserializeString() {
		SerializingConverter toBytes = new SerializingConverter();
		byte[] bytes = toBytes.convert("Testing");
		DeserializingConverter fromBytes = new DeserializingConverter();
		assertEquals("Testing", fromBytes.convert(bytes));
	}

	@Test
	public void nonSerializableObject() {
		SerializingConverter toBytes = new SerializingConverter();
		try {
			toBytes.convert(new Object());
			fail("Expected IllegalArgumentException");
		}
		catch (SerializationFailedException e) {
			assertNotNull(e.getCause());
			assertTrue(e.getCause() instanceof IllegalArgumentException);
		}
	}

	@Test
	public void nonSerializableField() {
		SerializingConverter toBytes = new SerializingConverter();
		try {
			toBytes.convert(new UnSerializable());
			fail("Expected SerializationFailureException");
		}
		catch (SerializationFailedException e) {
			assertNotNull(e.getCause());
			assertTrue(e.getCause() instanceof NotSerializableException);
		}
	}

	@Test(expected = SerializationFailedException.class)
	public void deserializationFailure() {
		DeserializingConverter fromBytes = new DeserializingConverter();
		fromBytes.convert("Junk".getBytes());
	}


	class UnSerializable implements Serializable {

		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unused")
		private Object object;
	}

}
