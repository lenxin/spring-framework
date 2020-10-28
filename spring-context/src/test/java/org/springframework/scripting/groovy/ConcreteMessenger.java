package org.springframework.scripting.groovy;

import org.springframework.scripting.ConfigurableMessenger;

/**

 *
 */
public class ConcreteMessenger implements ConfigurableMessenger {

	private String message;

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

}
