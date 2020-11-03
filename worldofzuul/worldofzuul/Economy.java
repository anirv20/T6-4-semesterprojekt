package worldofzuul;

public class Economy {
    private long balance;

    public Economy(long amount) {
        this.balance = amount;
    }

    public void addMoney(long amount) {
        this.balance += amount;
    }

    public boolean removeMoney(long amount) {
        if (this.balance-amount >= 0) {
            this.balance -= amount;
            return true;
        } else {
            return false;
        }
    }

    public long getBalance() {
        return this.balance;
    }
}
