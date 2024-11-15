package com.tunlin.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;

    private double discountPercentage; //%

    private LocalDate validityStartDate;

    private LocalDate validityEndDate;

    private double minimumOrderValue; // valid min

    private boolean isActive =true;

    @ManyToMany(mappedBy = "usedCoupons") //map usedCoupons in User
    private Set<User> userByUsers = new HashSet<>();

}
