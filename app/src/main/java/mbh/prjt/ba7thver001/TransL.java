package mbh.prjt.ba7thver001;

import java.io.Serializable;

public class TransL implements Serializable {
    private String user;
    private String amount;
    private String date;

    public TransL() {
    }

    public TransL(String user, String amount, String date) {
        this.user = user;
        this.amount = amount;
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
