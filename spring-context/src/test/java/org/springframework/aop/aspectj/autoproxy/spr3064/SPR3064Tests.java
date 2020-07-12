package org.springframework.aop.aspectj.autoproxy.spr3064;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SPR3064Tests {

	private Service service;


	@Test
	public void testServiceIsAdvised() {
		ClassPathXmlApplicationContext ctx =
				new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());

		service = (Service) ctx.getBean("service");

		try {
			this.service.serveMe();
			fail("service operation has not been advised by transaction interceptor");
		} catch (RuntimeException ex) {
			assertEquals("advice invoked", ex.getMessage());
		}
	}

}


@Retention(RetentionPolicy.RUNTIME)
@interface Transaction {
}


@Aspect
class TransactionInterceptor {

	@Around(value = "execution(* *..Service.*(..)) && @annotation(transaction)")
	public Object around(ProceedingJoinPoint pjp, Transaction transaction) throws Throwable {
		throw new RuntimeException("advice invoked");
		//return pjp.proceed();
	}
}


interface Service {

	void serveMe();
}


class ServiceImpl implements Service {

	@Override
	@Transaction
	public void serveMe() {
	}
}
