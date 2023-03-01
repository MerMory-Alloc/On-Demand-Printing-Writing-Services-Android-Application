package mbh.prjt.ba7thver001;

import java.io.Serializable;

public class Service implements Serializable {
    private Integer serviceId;
    private Integer serviceRequesterId;
    private String serviceRequesterUsername;
    private Integer serviceProviderId;
    private String serviceProviderUsername;
    private String paymentId;
    private Integer pageNumber;
    private String papersSize;
    private Boolean papersColored;
    private String endServiceTime;
    private String startServiceTime;
    private String serviceType;
    private String serviceSubject;
    private String serviceDetails;
    private String serviceState;
    private Double finalPrice;
    private Double estimatedPrice;

        public Service(){}

    public Service(Integer serviceId, Integer serviceRequesterId,String serviceRequesterUsername, Integer serviceProviderId,String serviceProviderUsername,String paymentId, Integer pageNumber, String papersSize, Boolean papersColored, String endServiceTime, String startServiceTime, String serviceType, String serviceSubject, String serviceDetails, String serviceState, Double finalPrice, Double estimatedPrice) {
        this.serviceId = serviceId;
        this.serviceRequesterId = serviceRequesterId;
        this.serviceRequesterUsername=serviceRequesterUsername;
        this.serviceProviderId = serviceProviderId;
        this.serviceProviderUsername=serviceProviderUsername;
        this.paymentId=paymentId;
        this.pageNumber = pageNumber;
        this.papersSize = papersSize;
        this.papersColored = papersColored;
        this.endServiceTime = endServiceTime;
        this.startServiceTime = startServiceTime;
        this.serviceType = serviceType;
        this.serviceSubject = serviceSubject;
        this.serviceDetails = serviceDetails;
        this.serviceState = serviceState;
        this.finalPrice = finalPrice;
        this.estimatedPrice = estimatedPrice;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getServiceRequesterId() {
        return serviceRequesterId;
    }

    public void setServiceRequesterId(Integer serviceRequesterId) {
        this.serviceRequesterId = serviceRequesterId;
    }

    public Integer getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(Integer serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPapersSize() {
        return papersSize;
    }

    public void setPapersSize(String papersSize) {
        this.papersSize = papersSize;
    }

    public Boolean getPapersColored() {
        return papersColored;
    }

    public void setPapersColored(Boolean papersColored) {
        this.papersColored = papersColored;
    }

    public String getEndServiceTime() {
        return endServiceTime;
    }

    public void setEndServiceTime(String endServiceTime) {
        this.endServiceTime = endServiceTime;
    }

    public String getStartServiceTime() {
        return startServiceTime;
    }

    public void setStartServiceTime(String startServiceTime) {
        this.startServiceTime = startServiceTime;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceSubject() {
        return serviceSubject;
    }

    public void setServiceSubject(String serviceSubject) {
        this.serviceSubject = serviceSubject;
    }

    public String getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(String serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public String getServiceState() {
        return serviceState;
    }

    public void setServiceState(String serviceState) {
        this.serviceState = serviceState;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Double getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(Double estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public String getServiceRequesterUsername() {
        return serviceRequesterUsername;
    }

    public void setServiceRequesterUsername(String serviceRequesterUsername) {
        this.serviceRequesterUsername = serviceRequesterUsername;
    }

    public String getServiceProviderUsername() {
        return serviceProviderUsername;
    }

    public void setServiceProviderUsername(String serviceProviderUsername) {
        this.serviceProviderUsername = serviceProviderUsername;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
