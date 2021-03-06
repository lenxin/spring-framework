package org.springframework.transaction.config;

import org.springframework.tests.transaction.CallCountingTransactionManager;

/**
 * @author Juergen Hoeller
 */
@NoSynch
@SuppressWarnings("serial")
public class NoSynchTransactionManager extends CallCountingTransactionManager {

	public NoSynchTransactionManager() {
		setTransactionSynchronization(CallCountingTransactionManager.SYNCHRONIZATION_NEVER);
	}

}
