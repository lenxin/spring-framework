package org.springframework.jdbc.core.test;

import java.math.BigDecimal;

/**

 */
public class ConcretePerson extends AbstractPerson {

	private BigDecimal balance;


	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

}
