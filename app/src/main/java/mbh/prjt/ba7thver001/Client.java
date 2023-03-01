package mbh.prjt.ba7thver001;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class Client implements Serializable {
    private Integer userId;
    private String serviceRequesterName;
    private String serviceRequesterFirstName;
    private String serviceRequesterLastName;
    private String serviceRequesterBio;
    private String serviceRequesterBirthDay;
    private String serviceRequesterEmail;
    private String serviceRequesterPhone;
    private String serviceRequesterLocation;
    private String serviceRequesterCountry;
    private String serviceRequesterProvince;
    private String serviceRequesterCity;
    private String serviceRequesterStreetAddress;
    private String serviceRequesterZipCode;
    private String serviceRequesterPhotoProfile;
    private Boolean verified;

    public Client(){}

    public Client(Integer userId, String serviceRequesterName, String serviceRequesterFirstName, String serviceRequesterLastName,String serviceRequesterBio, String serviceRequesterBirthDay, String serviceRequesterEmail, String serviceRequesterPhone, String serviceRequesterLocation, String serviceRequesterCountry, String serviceRequesterProvince, String serviceRequesterCity, String serviceRequesterStreetAddress, String serviceRequesterZipCode, String serviceRequesterPhotoProfile, Boolean verified) {
        this.userId = userId;
        this.serviceRequesterName = serviceRequesterName;
        this.serviceRequesterFirstName = serviceRequesterFirstName;
        this.serviceRequesterLastName = serviceRequesterLastName;
        this.serviceRequesterBio=serviceRequesterBio;
        this.serviceRequesterBirthDay = serviceRequesterBirthDay;
        this.serviceRequesterEmail = serviceRequesterEmail;
        this.serviceRequesterPhone = serviceRequesterPhone;
        this.serviceRequesterLocation = serviceRequesterLocation;
        this.serviceRequesterCountry = serviceRequesterCountry;
        this.serviceRequesterProvince = serviceRequesterProvince;
        this.serviceRequesterCity = serviceRequesterCity;
        this.serviceRequesterStreetAddress = serviceRequesterStreetAddress;
        this.serviceRequesterZipCode = serviceRequesterZipCode;
        this.serviceRequesterPhotoProfile = serviceRequesterPhotoProfile;
        this.verified = verified;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getServiceRequesterName() {
        return serviceRequesterName;
    }

    public void setServiceRequesterName(String serviceRequesterName) {
        this.serviceRequesterName = serviceRequesterName;
    }

    public String getServiceRequesterFirstName() {
        return serviceRequesterFirstName;
    }

    public void setServiceRequesterFirstName(String serviceRequesterFirstName) {
        this.serviceRequesterFirstName = serviceRequesterFirstName;
    }

    public String getServiceRequesterLastName() {
        return serviceRequesterLastName;
    }

    public void setServiceRequesterLastName(String serviceRequesterLastName) {
        this.serviceRequesterLastName = serviceRequesterLastName;
    }

    public String getServiceRequesterBirthDay() {
        return serviceRequesterBirthDay;
    }

    public void setServiceRequesterBirthDay(String serviceRequesterBirthDay) {
        this.serviceRequesterBirthDay = serviceRequesterBirthDay;
    }

    public String getServiceRequesterEmail() {
        return serviceRequesterEmail;
    }

    public void setServiceRequesterEmail(String serviceRequesterEmail) {
        this.serviceRequesterEmail = serviceRequesterEmail;
    }

    public String getServiceRequesterPhone() {
        return serviceRequesterPhone;
    }

    public void setServiceRequesterPhone(String serviceRequesterPhone) {
        this.serviceRequesterPhone = serviceRequesterPhone;
    }

    public String getServiceRequesterLocation() {
        return serviceRequesterLocation;
    }

    public void setServiceRequesterLocation(String serviceRequesterLocation) {
        this.serviceRequesterLocation = serviceRequesterLocation;
    }

    public String getServiceRequesterCountry() {
        return serviceRequesterCountry;
    }

    public void setServiceRequesterCountry(String serviceRequesterCountry) {
        this.serviceRequesterCountry = serviceRequesterCountry;
    }

    public String getServiceRequesterProvince() {
        return serviceRequesterProvince;
    }

    public void setServiceRequesterProvince(String serviceRequesterProvince) {
        this.serviceRequesterProvince = serviceRequesterProvince;
    }

    public String getServiceRequesterCity() {
        return serviceRequesterCity;
    }

    public void setServiceRequesterCity(String serviceRequesterCity) {
        this.serviceRequesterCity = serviceRequesterCity;
    }

    public String getServiceRequesterStreetAddress() {
        return serviceRequesterStreetAddress;
    }

    public void setServiceRequesterStreetAddress(String serviceRequesterStreetAddress) {
        this.serviceRequesterStreetAddress = serviceRequesterStreetAddress;
    }

    public String getServiceRequesterZipCode() {
        return serviceRequesterZipCode;
    }

    public void setServiceRequesterZipCode(String serviceRequesterZipCode) {
        this.serviceRequesterZipCode = serviceRequesterZipCode;
    }

    public String getServiceRequesterPhotoProfile() {
        return serviceRequesterPhotoProfile;
    }

    public void setServiceRequesterPhotoProfile(String serviceRequesterPhotoProfile) {
        this.serviceRequesterPhotoProfile = serviceRequesterPhotoProfile;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getServiceRequesterBio() {
        return serviceRequesterBio;
    }

    public void setServiceRequesterBio(String serviceRequesterBio) {
        this.serviceRequesterBio = serviceRequesterBio;
    }
}
