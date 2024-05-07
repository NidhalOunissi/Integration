package tn.esprit.clubconnect.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.clubconnect.entities.Department;
import tn.esprit.clubconnect.repositories.NotificationRepository;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService{

   private final NotificationRepository notificationRepository;

    @Override
    public void sendDepartmentAddedNotification(Department department) {
      /*  // Implement FCM API call to send a notification
        String fcmServerKey = "YOUR_FCM_SERVER_KEY";
        String fcmTopic = "YOUR_FCM_TOPIC";

        // Construct the notification data
        Map<String, String> notificationData = new HashMap<>();
        notificationData.put("title", "New Department Added");
        notificationData.put("body", "Department '" + department.getNameD() + "' has been added.");

        // Call FCM API to send the notification
        FCMMessageSender.sendMessage(fcmServerKey, fcmTopic, notificationData);*/
    }




    /* public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }*/

  /*  public void createNotification(String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        notification.setRead(false);
        notificationRepository.save(notification);
    }*/



}

