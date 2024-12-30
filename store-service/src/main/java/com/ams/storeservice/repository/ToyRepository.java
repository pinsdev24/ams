package com.ams.storeservice.repository;

import com.ams.storeservice.entity.Toy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToyRepository extends JpaRepository<Toy, Long> { }
