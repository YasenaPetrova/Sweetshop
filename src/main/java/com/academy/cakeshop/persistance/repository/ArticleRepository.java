package com.academy.cakeshop.persistance.repository;

import com.academy.cakeshop.persistance.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

}
