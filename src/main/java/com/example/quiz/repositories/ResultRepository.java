package com.example.quiz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.quiz.entities.Result;

public interface ResultRepository extends JpaRepository<Result, Long> {
}
