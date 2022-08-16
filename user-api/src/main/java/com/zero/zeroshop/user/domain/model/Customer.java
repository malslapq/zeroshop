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
import java.util.Locale;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
public class Customer extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String name;
    private String phone;
    private String password;
    private LocalDate birth;
    @Column(columnDefinition = "int default 0")
    private Integer balance;

    private LocalDateTime verifyExpiredAt;
    private String verificationCode;
    private boolean verify;

    public static Customer from(SignUpForm form) {
        return Customer.builder()
                .id(form.getId())
                .email(form.getEmail().toLowerCase(Locale.ROOT))
                .password(form.getPassword())
                .name(form.getName())
                .birth(form.getBirth())
                .phone(form.getPhone())
                .verify(false)
                .build();
    }

    public void changeVerifications(String verificationCode) {
        this.verificationCode = verificationCode;
        this.verifyExpiredAt = LocalDateTime.now().plusDays(1);
    }

    public void verificationCompleted() {
        this.verify = true;
    }

    public void changeBalance(Integer money) {
        this.balance = money;
    }

}
