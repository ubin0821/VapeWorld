package com.solo.Personalproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter
public class User {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;

    private String picture;

    private String role = "ROLE_USER";

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
    public User(String name,String email,String picture) {
        this.name = name;
        this.email = email;
        this.picture = picture;
    }
    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }
}
