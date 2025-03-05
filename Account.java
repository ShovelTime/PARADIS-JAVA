// Peter Idestam-Almquist, 2023-02-26.

package paradis.assignment2;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Account {
	// Instance variables.
	private final int ID;
	private volatile AtomicInteger balance;
	
	// Constructor.
	Account(int id, int balance, Bank bank) {
		ID = id;
		this.balance = new AtomicInteger(balance);
	}
	
	// Instance methods.


	int getId() {
		return ID;
	}
	
	int getBalance() {
		return balance.get();
	}

	void addBalance(int amount) {
		balance.addAndGet()
	}
}
