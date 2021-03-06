package org.springframework.test.web.servlet.samples.spr;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Test for SPR-10277 (multiple method chaining when building MockMvc).
 *
 * @author Wesley Hall
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class MockMvcBuilderMethodChainTests {

	@Autowired
	private WebApplicationContext wac;

	@Test
	public void chainMultiple() {
		MockMvcBuilders
				.webAppContextSetup(wac)
				.addFilter(new CharacterEncodingFilter() )
				.defaultRequest(get("/").contextPath("/mywebapp"))
				.build();
	}

	@Configuration
	@EnableWebMvc
	static class WebConfig implements WebMvcConfigurer {
	}

}
