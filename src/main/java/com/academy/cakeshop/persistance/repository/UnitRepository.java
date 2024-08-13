package com.academy.cakeshop.persistance.repository;

import com.academy.cakeshop.persistance.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
}

