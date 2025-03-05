// Peter Idestam-Almquist, 2023-02-26.

package paradis.assignment2;

import java.util.*;
import java.util.concurrent.FutureTask;

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
		//HashMap<Integer, ArrayList<FutureTask<Void>>> tasks = new HashMap<>();
		//HashMap<Bank, HashSet<Integer>> targetedBanks = new HashMap<>();
		HashMap<Bank, HashMap<Integer, ArrayList<FutureTask<Void>>>> tasks = new HashMap<>();
		//ArrayList<FutureTask<Void>> tasks = new ArrayList<>(operations.size());

		for(Operation operation : operations)
		{
			Bank bank = operation.getBank();
			HashMap<Integer, ArrayList<FutureTask<Void>>> bankMap = tasks.getOrDefault(bank, new HashMap<>());

			Integer accountID = operation.getAccountId();
			FutureTask<Void> task = new FutureTask<>(operation, null);
			ArrayList<FutureTask<Void>> queue = bankMap.getOrDefault(accountID, new ArrayList<>());
			queue.add(task);

			bankMap.putIfAbsent(accountID, queue);
			tasks.putIfAbsent(bank, bankMap);

		}

		for(Map.Entry<Bank, HashMap<Integer, ArrayList<FutureTask<Void>>>> maps : tasks.entrySet())
		{
			maps.getKey().requestTransaction(maps.getValue(), this);
		}





		// Queue the operations for execution.

	}

}	
