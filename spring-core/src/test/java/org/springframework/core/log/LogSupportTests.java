package org.springframework.core.log;

import org.junit.Test;

import static org.junit.Assert.*;

/**

 * @since 5.2
 */
public class LogSupportTests {

	@Test
	public void testLogMessageWithSupplier() {
		LogMessage msg = LogMessage.of(() -> new StringBuilder("a").append(" b"));
		assertEquals("a b", msg.toString());
		assertSame(msg.toString(), msg.toString());
	}

	@Test
	public void testLogMessageWithFormat1() {
		LogMessage msg = LogMessage.format("a %s", "b");
		assertEquals("a b", msg.toString());
		assertSame(msg.toString(), msg.toString());
	}

	@Test
	public void testLogMessageWithFormat2() {
		LogMessage msg = LogMessage.format("a %s %s", "b", "c");
		assertEquals("a b c", msg.toString());
		assertSame(msg.toString(), msg.toString());
	}

	@Test
	public void testLogMessageWithFormat3() {
		LogMessage msg = LogMessage.format("a %s %s %s", "b", "c", "d");
		assertEquals("a b c d", msg.toString());
		assertSame(msg.toString(), msg.toString());
	}

	@Test
	public void testLogMessageWithFormat4() {
		LogMessage msg = LogMessage.format("a %s %s %s %s", "b", "c", "d", "e");
		assertEquals("a b c d e", msg.toString());
		assertSame(msg.toString(), msg.toString());
	}

	@Test
	public void testLogMessageWithFormatX() {
		LogMessage msg = LogMessage.format("a %s %s %s %s %s", "b", "c", "d", "e", "f");
		assertEquals("a b c d e f", msg.toString());
		assertSame(msg.toString(), msg.toString());
	}

}
