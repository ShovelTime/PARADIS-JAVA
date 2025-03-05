// Peter Idestam-Almquist, 2023-02-26.

package paradis.assignment2;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Bank {
	// Instance variables.
	private final List<Account> accounts = new ArrayList<Account>();
	private final HashMap<Integer, Transaction> busyAccounts = new HashMap<>();
	//private final ConcurrentHashMap<Transaction, HashSet<Integer>> runningTransactions = new ConcurrentHashMap<>();
	private final ConcurrentLinkedQueue<WaitingTransaction> awaitingTransactions = new ConcurrentLinkedQueue<>();
	
	// Instance methods.

	int newAccount(int balance) {
		int accountId = accounts.size();
		Account account = new Account(accountId, balance, this);
		accounts.add(account);
		//Create lock for account.
		locks.add(new ReentrantReadWriteLock(true));
		return accountId;
	}
	
	Account getAccount(int accountId) {
		Account account;
		account = accounts.get(accountId);
		return account;
	}

	boolean requestTransaction(HashMap<Integer, ArrayList<FutureTask<Void>>> requestedAccount, Transaction caller)
	{
		synchronized(busyAccounts)
		{
			if(areAccountsAvailable(requestedAccount.keySet()))
			{

				return true;
			}
		}

		awaitingTransactions.add(new WaitingTransaction(caller, requestedAccount)); //Unable to lock all of the requested accounts, queue it for later.
		return false;
	}

	void runTransaction(Transaction caller, HashMap<Integer, ArrayList<FutureTask<Void>>> tasks)
	{
		synchronized (busyAccounts)
		{
			for(Integer account : tasks.keySet())
			{
				busyAccounts.put(account, caller);
			}
		}
	}

	void tryAssignFromQueue()
	{
		if(awaitingTransactions.isEmpty()) return;
	}

	private boolean areAccountsAvailable(Set<Integer> targetedAccounts)
	{
		for(Integer desiredAccount: targetedAccounts)
		{
			if (busyAccounts.containsKey(desiredAccount)) return false;
		}
		return true;
	}

}

record WaitingTransaction(Transaction source, HashMap<Integer, ArrayList<FutureTask<Void>>> tasks)
{

}