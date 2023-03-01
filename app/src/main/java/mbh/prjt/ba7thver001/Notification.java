package mbh.prjt.ba7thver001;

import java.io.Serializable;

public class Notification implements Serializable {
    private Integer serviceId;
    private String notificationType;
    private String fromId;
    private String toId;
    private String notificationSubject;
    private Long notificationcreatTime;
    private Boolean isSeen;

    public Notification() {
    }

    public Notification(Integer serviceId, String notificationType, String fromId, String toId, String notificationSubject, Long notificationcreatTime, Boolean isSeen) {
        this.serviceId=serviceId;
        this.notificationType = notificationType;
        this.fromId = fromId;
        this.toId = toId;
        this.notificationSubject = notificationSubject;
        this.notificationcreatTime = notificationcreatTime;
        this.isSeen=isSeen;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getNotificationSubject() {
        return notificationSubject;
    }

    public void setNotificationSubject(String notificationSubject) {
        this.notificationSubject = notificationSubject;
    }

    public Long getNotificationcreatTime() {
        return notificationcreatTime;
    }

    public void setNotificationcreatTime(Long notificationcreatTime) {
        this.notificationcreatTime = notificationcreatTime;
    }

    public Boolean getSeen() {
        return isSeen;
    }

    public void setSeen(Boolean seen) {
        isSeen = seen;
    }
}
