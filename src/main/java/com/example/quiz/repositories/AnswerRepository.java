package com.example.quiz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.quiz.entities.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {}