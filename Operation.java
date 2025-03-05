// Peter Idestam-Almquist, 2023-02-26.

package paradis.assignment2;

class Operation implements Runnable {
	private final int ACCOUNT_ID;
	private final int AMOUNT;
	private final Account account;
	private final Bank bank;
	
	Operation(Bank bank, int accountId, int amount) {
		ACCOUNT_ID = accountId;
		AMOUNT = amount;
		account = bank.getAccount(ACCOUNT_ID);
		this.bank = bank;
	}

	Bank getBank() { return bank; }
	
	int getAccountId() {
		return ACCOUNT_ID;
	}
	
	public void run() {
		account.addBalance(AMOUNT);
	}
}	
