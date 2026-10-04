package com.agenticIde.distributed_lovable.account_service.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    @Column(unique = true)
    String stripePriceId;                 //this is plan info
    Integer maxProjects;                // max projects allowed based on plan
    Integer maxTokensPerDay;
    Integer maxPreviews;              // running application on kubernates pod
    Boolean unlimitedAi;  //unlimited access if set true maxTokensPerDay doesn't matter
    Boolean active;
}
