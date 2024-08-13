package com.academy.cakeshop.persistance.entity;

import com.academy.cakeshop.enumeration.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_cake_store")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "first_name")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String firstName;

    @Column(name = "last_name")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String lastName;

    @Column(name = "user_name", unique = true)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String userName;

    @Column(name = "password")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String password;

    @Email
    @Column(name = "email")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String email;

    @Column(name = "phone_number")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String phoneNumber;

    @Column(name = "address")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private Role role;

}