package mbh.prjt.ba7thver001;

public class Rating {

    private Integer serviceId;
    private Integer providerId;
    private Integer requesterId;
    private Float rate;
    private String review;

    public Rating() {
    }

    public Rating(Integer serviceId, Integer providerId, Integer requesterId, Float rate, String review) {
        this.serviceId = serviceId;
        this.providerId = providerId;
        this.requesterId = requesterId;
        this.rate = rate;
        this.review = review;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public Integer getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Integer requesterId) {
        this.requesterId = requesterId;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
