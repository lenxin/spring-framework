package org.springframework.transaction.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.tests.transaction.CallCountingTransactionManager;

/**
 * @author Juergen Hoeller
 */
@Qualifier("synch")
@SuppressWarnings("serial")
public class SynchTransactionManager extends CallCountingTransactionManager {

}
