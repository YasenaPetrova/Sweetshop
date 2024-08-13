//package com.academy.cakeshop.service;
//
//import com.academy.cakeshop.errorHandling.BusinessNotFound;
//import com.academy.cakeshop.persistance.entity.User;
//import com.academy.cakeshop.persistance.repository.ReviewRepository;
//import com.academy.cakeshop.persistance.repository.UserRepository;
//import com.academy.cakeshop.persistance.entity.Review;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@RequiredArgsConstructor
//@Service
//public class ReviewService {
//    private final ReviewRepository reviewRepository;
//    private final UserRepository userRepository;
//
//    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
//
//    public List<Review> getAllReviewsByUserName(String userName) {
//        List<Review> reviews = reviewRepository.findAllByUserName(userName);
//        if (reviews != null) {
//            logger.info("Request to DB: get all reviews from userName: " + userName);
//            return reviews;
//        } else {
//            BusinessNotFound businessNotFound = new BusinessNotFound("No reviews found from userName: " + userName);
//            logger.error("Error: No reviews found for userName: {}", userName, businessNotFound);
//            throw businessNotFound;
//        }
//    }
//
//    public void create(String comment, String userName) {
//        User user = userRepository.findByUserName(userName);
//        Review review = new Review();
//        review.setComment(comment);
//        review.setUser(user);
//        review.setDate(LocalDate.now());
//        logger.info("Request to DB: add new review from userName: " + userName);
//        reviewRepository.saveAndFlush(review);
//    }
//
//    public boolean existsById(Long id) {
//        logger.info("Request to DB: check for existing id: " + id);
//        return reviewRepository.existsById(id);
//    }
//    public void deleteById(Long id) {
//        if (existsById(id)) {
//            logger.info("Request to DB: delete review with id: " + id);
//            reviewRepository.deleteById(id);
//        } else {
//            BusinessNotFound businessNotFound = new BusinessNotFound("No review with id: " + id + " found!");
//            logger.error("Error: No review with id: {} found", id, businessNotFound);
//            throw businessNotFound;
//        }
//    }
//}
