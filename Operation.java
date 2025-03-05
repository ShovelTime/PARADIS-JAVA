// Peter Idestam-Almquist, 2023-02-26.

package paradis.assignment2;

class Operation implements Runnable {
	private final int ACCOUNT_ID;
	private final int AMOUNT;
	private final Account account;
	private boolean handleLock;

	Operation(Bank bank, int accountId, int amount) {
		ACCOUNT_ID = accountId;
		AMOUNT = amount;
		account = bank.getAccount(ACCOUNT_ID);
		handleLock = true;
	}

	Account getAccount() {
		return account;
	}

	public void run() {

		//Acquire the lock on the account, if it is already owned by the current thread, which may
		account.acquireWriteLock();
		try {
			int balance = account.getBalance();
			balance = balance + AMOUNT;
			account.setBalance(balance);
		}
		finally {
			account.releaseWriteLock();
		}

	}

	//Allows an outside transaction to handle the lock on behalf of the operation.
	public void setHandleLock(boolean lock)
	{
		handleLock = lock;
	}
}	
