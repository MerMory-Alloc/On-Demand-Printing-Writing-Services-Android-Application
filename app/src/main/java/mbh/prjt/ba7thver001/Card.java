package mbh.prjt.ba7thver001;

import java.io.Serializable;

public class Card implements Serializable {

    private Integer cardId;
    private String cardNumber;
    private String expiredDate;
    private String cvvCode;

    public Card() {
    }

    public Card(Integer cardId, String cardNumber, String expiredDate, String cvvCode) {
        this.cardId=cardId;
        this.cardNumber = cardNumber;
        this.expiredDate = expiredDate;
        this.cvvCode = cvvCode;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getCvvCode() {
        return cvvCode;
    }

    public void setCvvCode(String cvvCode) {
        this.cvvCode = cvvCode;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }
}
