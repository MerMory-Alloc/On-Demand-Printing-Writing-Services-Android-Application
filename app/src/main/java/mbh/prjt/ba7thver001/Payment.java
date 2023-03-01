package mbh.prjt.ba7thver001;

import java.io.Serializable;

public class Payment implements Serializable {

    private String paymentId;
    private String providerId;
    private String requesterId;
    private String serviceId;
    private Long creatDay;
    private Long finishedDay;
    private String paymentMethod;
    private String paymentState;
    private Double amount;
    private Boolean aboutRefund;
    private Boolean refundDemanded;
    private Integer refundPercent;
    private String refundGuidlines;
    private String reasonRefundDemanded;

    public Payment() {
    }

    public Payment(String paymentId, String providerId, String requesterId, String serviceId, Long creatDay, Long finishedDay, String paymentMethod,
                   String paymentState, Double amount, Boolean aboutRefund,Boolean refundDemanded, Integer refundPercent,String refundGuidlines,String reasonRefundDemanded) {
        this.paymentId = paymentId;
        this.providerId = providerId;
        this.requesterId = requesterId;
        this.serviceId = serviceId;
        this.creatDay = creatDay;
        this.finishedDay = finishedDay;
        this.paymentMethod = paymentMethod;
        this.paymentState = paymentState;
        this.amount=amount;
        this.aboutRefund = aboutRefund;
        this.refundDemanded=refundDemanded;
        this.refundPercent=refundPercent;
        this.refundGuidlines=refundGuidlines;
        this.reasonRefundDemanded=reasonRefundDemanded;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Long getCreatDay() {
        return creatDay;
    }

    public void setCreatDay(Long creatDay) {
        this.creatDay = creatDay;
    }

    public Long getFinishedDay() {
        return finishedDay;
    }

    public void setFinishedDay(Long finishedDay) {
        this.finishedDay = finishedDay;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(String paymentState) {
        this.paymentState = paymentState;
    }

    public Boolean getAboutRefund() {
        return aboutRefund;
    }

    public void setAboutRefund(Boolean aboutRefund) {
        this.aboutRefund = aboutRefund;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Boolean getRefundDemanded() {
        return refundDemanded;
    }

    public void setRefundDemanded(Boolean refundDemanded) {
        this.refundDemanded = refundDemanded;
    }

    public Integer getRefundPercent() {
        return refundPercent;
    }

    public void setRefundPercent(Integer refundPercent) {
        this.refundPercent = refundPercent;
    }

    public String getRefundGuidlines() {
        return refundGuidlines;
    }

    public void setRefundGuidlines(String refundGuidlines) {
        this.refundGuidlines = refundGuidlines;
    }

    public String getReasonRefundDemanded() {
        return reasonRefundDemanded;
    }

    public void setReasonRefundDemanded(String reasonRefundDemanded) {
        this.reasonRefundDemanded = reasonRefundDemanded;
    }
}
