// Peter Idestam-Almquist, 2023-02-26.

package paradis.assignment2;

import java.util.*;

class Transaction implements Runnable {
	private final List<Operation> operations = new ArrayList<Operation>();
	private final Set<Account> targetedAccounts = new HashSet<>();
	private boolean closed = false;

	void add(Operation operation) {
		if (closed) return;
		operation.setHandleLock(false);
		operations.add(operation);
		targetedAccounts.add(operation.getAccount());
	}

	void close() {
		closed = true;
	}

	public void run() {
		if (!closed) return;

		acquireLocks();
		try {
			// Execute the operations.
			for (Operation operation : operations) {
				operation.run();
			}
		}
		finally {
			releaseLocks();
		}
	}

	private void acquireLocks()
	{
		for(Account account : targetedAccounts) {
			account.acquireWriteLock();
		}
	}

	private void releaseLocks()
	{
		for(Account account : targetedAccounts)
		{
			account.releaseWriteLock();
		}
	}
}	
