// Peter Idestam-Almquist, 2023-02-26.

package paradis.assignment2;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Bank {
	// Instance variables.
	private final List<Account> accounts = new ArrayList<Account>();
	private final HashMap<Integer, Lock> busyAccounts = new HashMap<>();
	private final ConcurrentHashMap<Transaction, HashSet<Integer>> runningConditions = new ConcurrentHashMap<>();
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

	void requestExecution(HashSet<Integer> requestedAccount)
	{
		synchronized(busyAccounts)
		{
			if(areAccountsAvailable(targetedAccounts))
			{

			}
		}

	}

	private boolean areAccountsAvailable(HashSet<Integer> targetedAccounts)
	{
		for(Integer desiredAccount: targetedAccounts)
		{
			if (busyAccounts.containsKey(desiredAccount)) return false;
		}
		return true;
	}

}