package worldofzuul.domain;

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
    public boolean removeMoney(long amount, boolean overrule) {
        boolean success = (removeMoney(amount));
        if (!success && overrule) {
            this.balance -= amount;
        }
        return success;
    }

    public long getBalance() {
        return this.balance;
    }
}
