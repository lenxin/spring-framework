package org.springframework.beans.factory.parsing;

import org.springframework.beans.factory.BeanDefinitionStoreException;

/**
 * Exception thrown when a bean definition reader encounters an error
 * during the parsing process.
 *
 * @since 2.0
 */
@SuppressWarnings("serial")
public class BeanDefinitionParsingException extends BeanDefinitionStoreException {
	/**
	 * Create a new BeanDefinitionParsingException.
	 *
	 * @param problem the configuration problem that was detected during the parsing process
	 */
	public BeanDefinitionParsingException(Problem problem) {
		super(problem.getResourceDescription(), problem.toString(), problem.getRootCause());
	}
}