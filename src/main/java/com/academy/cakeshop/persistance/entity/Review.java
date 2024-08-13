//package com.academy.cakeshop.persistance.entity;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.LocalDate;
//
//@Getter
//@Setter
//@Entity
//@Table(name = "review")
//public class Review {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", nullable = false)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @Lob
//    @Column(name = "comment")
//    private String comment;
//
//    @Column(name = "date")
//    private LocalDate date;
//
//}