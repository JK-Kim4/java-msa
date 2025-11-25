package com.tutomato.userservice.infrastructure;

import com.tutomato.userservice.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @CreatedDate
    private Instant createdAt;


    protected UserEntity() {
    }

    protected UserEntity(String userId, String email, String password, String name) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static UserEntity from(User user) {
        return new UserEntity(
            user.getUserId(),
            user.getEmail(),
            user.getPassword(),
            user.getName()
        );
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
