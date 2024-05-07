package tn.esprit.clubconnect.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ProjectPredictionResult {
    private double totalFundsRaised;
    private int numberOfProjects;
    private double averageProjectCost;
    private double availableBudget;
    private int additionalProjects;
    private double fundsPerProject;

}
