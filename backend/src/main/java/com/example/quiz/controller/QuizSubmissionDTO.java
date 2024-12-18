package com.example.quiz.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class QuizSubmissionDTO {

    private Long quizId;
    private Long userId;
    private List<UserAnswerDTO> userAnswers;

    // Constructor mặc định
    public QuizSubmissionDTO() {
        // Khởi tạo danh sách để tránh null
        this.userAnswers = new ArrayList<>();
    }

    // Getters và Setters
    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<UserAnswerDTO> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(List<UserAnswerDTO> userAnswers) {
        this.userAnswers = userAnswers;
    }

    public static class UserAnswerDTO {
        private long questionId;
        private int selectedAnswerIndex;

        // Constructor mặc định
        public UserAnswerDTO() {}

        @JsonCreator
        public UserAnswerDTO(
                @JsonProperty("questionId") Long questionId,
                @JsonProperty("selectedAnswerIndex") int selectedAnswerIndex) {
            this.questionId = questionId;
            this.selectedAnswerIndex = selectedAnswerIndex;
        }

        // Getters và setters
        public Long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }

        public int getSelectedAnswerIndex() {
            return selectedAnswerIndex;
        }

        public void setSelectedAnswerIndex(int selectedAnswerIndex) {
            this.selectedAnswerIndex = selectedAnswerIndex;
        }
    }
}
