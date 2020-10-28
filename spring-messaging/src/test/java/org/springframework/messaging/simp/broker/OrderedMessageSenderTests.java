package org.springframework.messaging.simp.broker;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link OrderedMessageSender}.

 */
public class OrderedMessageSenderTests {

	private static final Log logger = LogFactory.getLog(OrderedMessageSenderTests.class);


	private OrderedMessageSender sender;

	ExecutorSubscribableChannel channel = new ExecutorSubscribableChannel(this.executor);

	private ThreadPoolTaskExecutor executor;


	@Before
	public void setup() {
		this.executor = new ThreadPoolTaskExecutor();
		this.executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);
		this.executor.setAllowCoreThreadTimeOut(true);
		this.executor.afterPropertiesSet();

		this.channel = new ExecutorSubscribableChannel(this.executor);
		OrderedMessageSender.configureOutboundChannel(this.channel, true);

		this.sender = new OrderedMessageSender(this.channel, logger);

	}

	@After
	public void tearDown() {
		this.executor.shutdown();
	}


	@Test
	public void test() throws InterruptedException {

		int start = 1;
		int end = 1000;

		AtomicInteger index = new AtomicInteger(start);
		AtomicReference<Object> result = new AtomicReference<>();
		CountDownLatch latch = new CountDownLatch(1);

		this.channel.subscribe(message -> {
			int expected = index.getAndIncrement();
			Integer actual = (Integer) message.getHeaders().getOrDefault("seq", -1);
			if (actual != expected) {
				result.set("Expected: " + expected + ", but was: " + actual);
				latch.countDown();
				return;
			}
			if (actual == 100 || actual == 200) {
				try {
					Thread.sleep(200);
				}
				catch (InterruptedException ex) {
					result.set(ex.toString());
					latch.countDown();
				}
			}
			if (actual == end) {
				result.set("Done");
				latch.countDown();
			}
		});

		for (int i = start; i <= end; i++) {
			SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
			accessor.setHeader("seq", i);
			accessor.setLeaveMutable(true);
			this.sender.send(MessageBuilder.createMessage("payload", accessor.getMessageHeaders()));
		}

		latch.await(10, TimeUnit.SECONDS);
		assertEquals("Done", result.get());
	}

}

