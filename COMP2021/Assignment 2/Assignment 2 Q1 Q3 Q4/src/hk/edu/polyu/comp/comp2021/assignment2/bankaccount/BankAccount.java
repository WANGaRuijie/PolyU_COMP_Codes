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
        if (amount <= 0)
            throw new IllegalArgumentException();

        synchronized(this) {
            balance += amount;
            notifyAll();
        }
            // Add missing code here
    }

    /** Withdraw 'amount' from this account. 'amount' should always be positive. */
    public void withdraw(int amount){
        if (amount <= 0)
            throw new IllegalArgumentException();

        synchronized (this) {
            while (balance < amount) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            balance -= amount;
        }

        // Add missing code here

    }
}
