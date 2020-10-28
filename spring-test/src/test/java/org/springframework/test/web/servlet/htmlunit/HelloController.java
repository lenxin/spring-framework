package org.springframework.test.web.servlet.htmlunit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**

 * @since 4.2
 */
@RestController
public class HelloController {

	@RequestMapping("/a")
	public String header(HttpServletRequest request) {
		return "hello";
	}

}
