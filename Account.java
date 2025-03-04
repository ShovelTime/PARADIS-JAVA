// Peter Idestam-Almquist, 2023-02-26.

package paradis.assignment2;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Account {
	// Instance variables.
	private final Bank owningBank;
	private final int ID;
	private volatile int balance;
	
	// Constructor.
	Account(int id, int balance, Bank bank) {
		ID = id;
		this.balance = balance;
		this.owningBank = bank;
	}
	
	// Instance methods.

	Bank getBank() { return owningBank; }

	int getId() {
		return ID;
	}
	
	int getBalance() {
		return balance;
	}
	
	void setBalance(int balance) {
		this.balance = balance;
	}
}
