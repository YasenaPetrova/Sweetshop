//package com.academy.cakeshop.persistance.repository;
//
//import com.academy.cakeshop.persistance.entity.Review;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface ReviewRepository extends JpaRepository<Review, Long> {
//
//    @Query("select r from Review r where r.user.userName = ?1 order by r.date DESC")
//    List<Review> findAllByUserName(String userName);
//}
