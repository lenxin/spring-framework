package test.mixin;

import org.springframework.aop.support.DefaultIntroductionAdvisor;

/**
 * Advisor for use with a LockMixin. Applies to all classes.
 *

 */
@SuppressWarnings("serial")
public class LockMixinAdvisor extends DefaultIntroductionAdvisor {

	public LockMixinAdvisor() {
		super(new LockMixin(), Lockable.class);
	}

}
