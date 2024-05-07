package tn.esprit.clubconnect.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PredictionObject {

    private Double totalFundsRaised;
    private Double averageProjectCost;
    private Integer monthsSustainable;
    private Integer inPeriod;
    private Double requiredFunds;
    private String adviceMessage;
}
