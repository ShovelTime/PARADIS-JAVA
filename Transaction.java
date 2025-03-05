// Peter Idestam-Almquist, 2023-02-26.

package paradis.assignment2;

import java.util.*;

class Transaction implements Runnable {
	private final List<Operation> operations = new ArrayList<Operation>();
	private final Set<Account> targetedAccounts = new HashSet<>(); // Keep track of the accounts we will want to acquire a lock onto once we are ready to execute.
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
		// If we wanted to implement rollback here, what we could do is create a hashset would contain a list of cloned accounts with their original value,
		// that could be restored if an exception occurs, this would however cause an additional performance loss due to clone semantics.

		// Wait until all the locks are acquire before running, as per the requirements of Transactions
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

	private void acquireLocks() {
		for(Account account : targetedAccounts) {
			account.acquireWriteLock();
		}
	}

	private void releaseLocks() {
		for(Account account : targetedAccounts) {
			account.releaseWriteLock();
		}
	}
}	
