// Peter Idestam-Almquist, 2023-02-26.

package paradis.assignment2;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Account {
	// Instance variables.
	//Lock that determines access to the ability to modify this account.
	//If proper ordering of execution is preferred, then setting the fairness value to true is ideal, though with overall much worse performance of about 50%.
	private final ReentrantLock accountLock = new ReentrantLock(false);
	private final int ID;
	private volatile int balance; //Volatile to ensure proper propagation of updates.

	// Constructor.
	Account(int id, int balance)
	{
		ID = id;
		this.balance = balance;
	}

	// Instance methods.

	int getId() {
		return ID;
	}

	void acquireWriteLock() {
		accountLock.lock();
	}

	void releaseWriteLock() {
		accountLock.unlock();
	}

	int getBalance() {
		//Volatile guarantees Happens-Before relationships with the writes, and should therefore result in the most recent value being shown.
		return balance;
	}

	void setBalance(int balance) {
		if(!this.accountLock.isHeldByCurrentThread()) throw new IllegalStateException(); // no thread without a lock should be able to write here.
		this.balance = balance;
	}
}
