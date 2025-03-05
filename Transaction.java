// Peter Idestam-Almquist, 2023-02-26.

package paradis.assignment2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Transaction implements Runnable {
	private List<Operation> operations = new ArrayList<Operation>();
	private boolean closed = false;
	
	void add(Operation operation) {
		if (closed) return;
		operations.add(operation);
	}
	
	void close() {
		closed = true;
	}
	
	public void run() {
		if (!closed) return;
		//Construct Tasks
		HashSet<Integer> targetedAccounts = new HashSet<>();
		ArrayList<FutureTask<Void>> tasks = new ArrayList<>(operations.size());

		for(Operation operation : operations)
		{
			Integer accountID = operation.getAccountId();
			FutureTask<Void> task = new FutureTask<>(operation, null);
			targetedAccounts.add(accountID);
			tasks.add(task);
		}



		// Queue the operations for execution.

	}

}	
