package hk.edu.polyu.comp.comp2021.assignment2.bankaccount;

/** A bank account object. */
public class BankAccount { 

    private int balance;

	/** Instantiate an account with 'initialBalance'. */
    public BankAccount(int initialBalance){
        if(initialBalance < 0)
            throw new IllegalArgumentException();

        balance = initialBalance;
    }

	/** Balance of the account. The balance should never be negative. */
    public int getBalance(){
        return balance;
    }

	/** Deposit 'amount' into this account. 'amount' should always be positive. */
    public void deposit(int amount){
        if(amount <= 0)
            throw new IllegalArgumentException();

        synchronized (this){
            balance += amount;

            this.notifyAll();
        }
    }

	/** Withdraw 'amount' from this account. 'amount' should always be positive. */
    public void withdraw(int amount){
        if(amount <= 0)
            throw new IllegalArgumentException();

        synchronized (this) {
            try {
                while (amount > getBalance()) {
                    this.wait();
                }

                balance -= amount;
            }
            catch (InterruptedException e){}
        }
    }
}
