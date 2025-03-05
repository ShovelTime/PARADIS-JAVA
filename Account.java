// Peter Idestam-Almquist, 2023-02-26.

package paradis.assignment2;

import java.util.concurrent.locks.ReentrantReadWriteLock;

class Account {
	// Instance variables.
	//Lock that determines access to the ability to modify this account.
	private final ReentrantReadWriteLock accountLock = new ReentrantReadWriteLock(true);
	private final int ID;
	private volatile Integer balance;

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
		accountLock.writeLock().lock();
	}

	void releaseWriteLock() {
		accountLock.writeLock().unlock();
	}

	int getBalance() {
		accountLock.readLock().lock();
		try {
			return balance;
		}
		finally {
			accountLock.readLock().unlock();
		}
	}

	void setBalance(int balance) {
		if(!this.accountLock.writeLock().isHeldByCurrentThread()) throw new IllegalStateException();

		this.balance = balance;
	}
}
