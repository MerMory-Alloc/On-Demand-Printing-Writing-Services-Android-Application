package mbh.prjt.ba7thver001;

public class Transaction {
    private String transactionId;
    private String userId;
    private Double amount;
    private String cardToken;
    private String status;
    private Long createdDate;

    public Transaction() {
    }

    public Transaction(String transactionId, String userId, Double amount, String cardToken, String status, Long createdDate) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.amount = amount;
        this.cardToken = cardToken;
        this.status = status;
        this.createdDate = createdDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }
}
