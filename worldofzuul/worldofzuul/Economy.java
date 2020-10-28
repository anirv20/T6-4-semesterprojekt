package worldofzuul;

public class Economy {
    private double balance;

    public Economy() {
        this.balance = 100000;
    }

    public void addMoney(double amount) {
        this.balance += amount;
    }

    public boolean removeMoney(double amount) {
        if (this.balance-amount >= 0) {
            this.balance -= amount;
            return true;
        } else {
            return false;
        }
    }

    public double getBalance() {
        return this.balance;
    }
}
