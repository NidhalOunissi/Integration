package tn.esprit.clubconnect.services;

import tn.esprit.clubconnect.entities.Department;

public interface INotificationService {
    void sendDepartmentAddedNotification(Department department);
}
