package com.tunlin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tunlin.domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode // đơn giản hóa việc xử lý so sánh và tối ưu hiệu suất tìm kiếm đối tượng khi dùng HashMap hay HashSet.
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private String password;

    private String email;

    private String fullName;

    private String mobile;

    private USER_ROLE role =USER_ROLE.ROLE_CUSTOMER;

    @OneToMany
    private Set<Address> address = new HashSet<>();

    @ManyToMany
    @JsonIgnore //check backend no view frontend (bo qua khi Java <=|=> Json)
    private Set<Coupon> usedCoupons = new HashSet<>();

}
