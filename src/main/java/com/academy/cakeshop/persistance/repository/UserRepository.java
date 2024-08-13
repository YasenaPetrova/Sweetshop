package com.academy.cakeshop.persistance.repository;

import com.academy.cakeshop.enumeration.Role;
import com.academy.cakeshop.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findLastNameByLastNameLikeIgnoreCaseOrderByLastNameAsc(String lastName);
    @Transactional
    @Modifying
    @Query("update User u set u.firstName = ?1 where u.id = ?2")
    int updateUserFirstNameById(String firstName, Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.lastName = ?1 where u.id = ?2")
    int updateUserLastNameById(String lastName, Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.email = ?1 where u.id = ?2")
    int updateUserEmailById(String email, Long id);

    User findByUserNameEquals(String userName);

    @Query("select u from User u where u.userName = ?1")
    User findByUserName(String userName);

    Optional<User> findByUserNameLike(String userName);

    @Query("select u from User u where u.role = ?1")
    User findByRole(Role role);
}