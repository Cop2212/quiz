package com.example.quiz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.quiz.entities.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {}