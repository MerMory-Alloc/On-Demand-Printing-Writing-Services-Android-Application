package mbh.prjt.ba7thver001;

import java.io.Serializable;
import java.util.ArrayList;

public class Admin implements Serializable {
    private Integer srvcProviderId;
    private String srvcProviderType;
    private String srvcProviderName;
    private String srvcProviderFirstName;
    private String srvcProviderLastName;
    private String srvcProviderBio;
    private String srvcProviderBirthDay;
    private String srvcProviderEmail;
    private String srvcProviderLocation;
    private String srvcProviderCountry;
    private String srvcProviderProvince;
    private String srvcProviderCity;
    private String srvcProviderZipcode;
    private String srvcProviderStreetAddress;
    private String srvcProviderPhone;
    private String srvcProviderProfilImage;
    private ArrayList<String> availablePapersSizes;
    private Double walletBalance;
    private Float srvcProviderRating;
    private Integer servicesDone;
    private Boolean verified;
    private Boolean online;

    public Admin(){}
    public Admin(Integer srvcProviderId, String srvcProviderType, String srvcProviderName, String srvcProviderFirstName, String srvcProviderLastName,String srvcProviderBio, String srvcProviderBirthDay, String srvcProviderEmail, String srvcProviderLocation, String srvcProviderCountry, String srvcProviderProvince, String srvcProviderCity, String srvcProviderZipcode, String srvcProviderStreetAddress, String srvcProviderPhone, String srvcProviderProfilImage, ArrayList<String> availablePapersSizes,Double walletBalance, Float srvcProviderRating,Integer servicesDone, Boolean verified, Boolean online) {
        this.srvcProviderId = srvcProviderId;
        this.srvcProviderType = srvcProviderType;
        this.srvcProviderName = srvcProviderName;
        this.srvcProviderFirstName = srvcProviderFirstName;
        this.srvcProviderLastName = srvcProviderLastName;
        this.srvcProviderBio= srvcProviderBio;
        this.srvcProviderBirthDay = srvcProviderBirthDay;
        this.srvcProviderEmail = srvcProviderEmail;
        this.srvcProviderLocation = srvcProviderLocation;
        this.srvcProviderCountry = srvcProviderCountry;
        this.srvcProviderProvince = srvcProviderProvince;
        this.srvcProviderCity = srvcProviderCity;
        this.srvcProviderZipcode = srvcProviderZipcode;
        this.srvcProviderStreetAddress = srvcProviderStreetAddress;
        this.srvcProviderPhone = srvcProviderPhone;
        this.srvcProviderProfilImage = srvcProviderProfilImage;
        this.availablePapersSizes=availablePapersSizes;
        this.walletBalance=walletBalance;
        this.srvcProviderRating=srvcProviderRating;
        this.servicesDone=servicesDone;
        this.verified = verified;
        this.online = online;
    }

    public Integer getSrvcProviderId() {
        return srvcProviderId;
    }

    public String getSrvcProviderName() {
        return srvcProviderName;
    }

    public String getSrvcProviderLocation() {
        return srvcProviderLocation;
    }

    public String getSrvcProviderProfilImage() {
        return srvcProviderProfilImage;
    }

    public String getSrvcProviderFirstName() {
        return srvcProviderFirstName;
    }

    public void setSrvcProviderFirstName(String srvcProviderFirstName) {
        this.srvcProviderFirstName = srvcProviderFirstName;
    }

    public String getSrvcProviderLastName() {
        return srvcProviderLastName;
    }

    public void setSrvcProviderLastName(String srvcProviderLastName) {
        this.srvcProviderLastName = srvcProviderLastName;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public void setSrvcProviderProfilImage(String srvcProviderProfilImage) {
        this.srvcProviderProfilImage = srvcProviderProfilImage;
    }

    public String getSrvcProviderEmail() {
        return srvcProviderEmail;
    }

    public String getSrvcProviderPhone() {
        return srvcProviderPhone;
    }

    public void setSrvcProviderId(Integer srvcProviderId) {
        this.srvcProviderId = srvcProviderId;
    }

    public void setSrvcProviderName(String srvcProviderName) {
        this.srvcProviderName = srvcProviderName;
    }

    public void setSrvcProviderLocation(String srvcProviderLocation) {
        this.srvcProviderLocation = srvcProviderLocation;
    }

    public void setSrvcProviderEmail(String srvcProviderEmail) {
        this.srvcProviderEmail = srvcProviderEmail;
    }

    public void setSrvcProviderPhone(String srvcProviderPhone) {
        this.srvcProviderPhone = srvcProviderPhone;
    }

    public String getSrvcProviderType() {
        return srvcProviderType;
    }

    public void setSrvcProviderType(String srvcProviderType) {
        this.srvcProviderType = srvcProviderType;
    }

    public String getSrvcProviderBirthDay() {
        return srvcProviderBirthDay;
    }

    public void setSrvcProviderBirthDay(String srvcProviderBirthDay) {
        this.srvcProviderBirthDay = srvcProviderBirthDay;
    }

    public String getSrvcProviderCountry() {
        return srvcProviderCountry;
    }

    public void setSrvcProviderCountry(String srvcProviderCountry) {
        this.srvcProviderCountry = srvcProviderCountry;
    }

    public String getSrvcProviderProvince() {
        return srvcProviderProvince;
    }

    public void setSrvcProviderProvince(String srvcProviderProvince) {
        this.srvcProviderProvince = srvcProviderProvince;
    }

    public String getSrvcProviderCity() {
        return srvcProviderCity;
    }

    public void setSrvcProviderCity(String srvcProviderCity) {
        this.srvcProviderCity = srvcProviderCity;
    }

    public String getSrvcProviderZipcode() {
        return srvcProviderZipcode;
    }

    public void setSrvcProviderZipcode(String srvcProviderZipcode) {
        this.srvcProviderZipcode = srvcProviderZipcode;
    }

    public String getSrvcProviderStreetAddress() {
        return srvcProviderStreetAddress;
    }

    public void setSrvcProviderStreetAddress(String srvcProviderStreetAddress) {
        this.srvcProviderStreetAddress = srvcProviderStreetAddress;
    }

    public Float getSrvcProviderRating() {
        return srvcProviderRating;
    }

    public void setSrvcProviderRating(Float srvcProviderRating) {
        this.srvcProviderRating = srvcProviderRating;
    }

    public ArrayList<String> getAvailablePapersSizes() {
        return availablePapersSizes;
    }

    public void setAvailablePapersSizes(ArrayList<String> availablePapersSizes) {
        this.availablePapersSizes = availablePapersSizes;
    }

    public Integer getServicesDone() {
        return servicesDone;
    }

    public void setServicesDone(Integer servicesDone) {
        this.servicesDone = servicesDone;
    }

    public Double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(Double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getSrvcProviderBio() {
        return srvcProviderBio;
    }

    public void setSrvcProviderBio(String srvcProviderBio) {
        this.srvcProviderBio = srvcProviderBio;
    }
}
