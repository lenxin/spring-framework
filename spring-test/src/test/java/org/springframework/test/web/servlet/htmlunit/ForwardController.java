package org.springframework.test.web.servlet.htmlunit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**

 * @since 4.2
 */
@Controller
public class ForwardController {

	@RequestMapping("/forward")
	public String forward() {
		return "forward:/a";
	}

}
