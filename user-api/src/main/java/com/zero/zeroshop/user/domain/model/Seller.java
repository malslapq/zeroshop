package com.zero.zeroshop.user.domain.model;

import com.zero.zeroshop.user.domain.BaseEntity;
import com.zero.zeroshop.user.domain.SignUpForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@AuditOverride(forClass = BaseEntity.class)
public class Seller extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String name;
    private String password;
    private LocalDate birth;
    private String phone;
    private LocalDateTime verifyExpireAt;
    private String verificationCode;
    private boolean verify;
    private Integer balance;

    public static Seller from(SignUpForm form) {
        return Seller.builder()
                .id(form.getId())
                .email(form.getEmail())
                .name(form.getName())
                .password(form.getPassword())
                .birth(form.getBirth())
                .phone(form.getPhone())
                .build();
    }

    public void changeVerifications(String verificationCode) {
        this.verificationCode = verificationCode;
        this.verifyExpireAt = LocalDateTime.now().plusDays(1);
    }

    public void verificationCompleted() {
        this.verify = true;
    }

}
