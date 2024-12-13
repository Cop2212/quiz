package com.example.quiz.controller;

import com.example.quiz.model.Question;
import com.example.quiz.model.Quiz;
import com.example.quiz.model.QuizResult;
import com.example.quiz.model.User;
import com.example.quiz.repository.QuestionRepository;
import com.example.quiz.repository.QuizRepository;
import com.example.quiz.repository.UserRepository;
import com.example.quiz.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.quiz.service.QuizService;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private final QuizService quizService;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizResultRepository quizResultRepository;

    public QuizController(QuizService quizService, QuizRepository quizRepository,
                          QuestionRepository questionRepository, UserRepository userRepository) {
        this.quizService = quizService;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    // Phương thức nộp bài
    @PostMapping("/api/quiz/submit")
    public ResponseEntity<String> submitQuiz(@RequestBody QuizSubmissionDTO submissionDTO) {
        try {
            Long quizId = submissionDTO.getQuizId();
            Long userId = submissionDTO.getUserId();
            List<QuizSubmissionDTO.UserAnswerDTO> userAnswers = submissionDTO.getUserAnswers();

            // Lấy quiz từ ID
            Quiz quiz = quizRepository.findById(quizId)
                    .orElseThrow(() -> new RuntimeException("Quiz không tồn tại"));

            // Kiểm tra người dùng có quyền nộp bài không
            if (!quiz.getUser().getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Người dùng không có quyền nộp bài cho quiz này.");
            }

            // Tính số câu đúng và điểm
            int correctAnswers = 0;
            int totalQuestions = quiz.getQuestions().size();

            for (QuizSubmissionDTO.UserAnswerDTO userAnswer : userAnswers) {
                // Lấy câu hỏi từ quiz
                Question question = quiz.getQuestions().stream()
                        .filter(q -> q.getId().equals(userAnswer.getQuestionId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Câu hỏi không tồn tại"));

                // So sánh câu trả lời của người dùng với câu trả lời đúng
                if (question.getCorrectAnswerIndex() == userAnswer.getSelectedAnswerIndex()) {
                    correctAnswers++;
                }
            }

            // Tính điểm
            double score = correctAnswers * (10.0 / totalQuestions);

            // Lưu kết quả vào cơ sở dữ liệu
            QuizResult result = new QuizResult();
            result.setQuiz(quiz);
            result.setUserId(userId);
            result.setScore(score);
            result.setTotalQuestions(totalQuestions);
            quizResultRepository.save(result);

            return ResponseEntity.ok("Nộp bài thành công. Điểm của bạn là: " + score);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra khi nộp bài: " + e.getMessage());
        }}

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizDTO> getQuizById(@PathVariable Long quizId, @RequestParam Long userId) {
        // Tìm quiz theo ID
        Optional<Quiz> optionalQuiz = quizService.findById(quizId);
        if (!optionalQuiz.isPresent()) {
            // Trả về lỗi nếu không tìm thấy quiz
            return ResponseEntity.notFound().build();
        }
// Lấy quiz từ Optional
        Quiz quiz = optionalQuiz.get();

        // Chuyển quiz sang QuizDTO
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setQuizId(quiz.getId());
        quizDTO.setQuizName(quiz.getQuizName());
        quizDTO.setCodeQuiz(quiz.getCodeQuiz());
        quizDTO.setTimeLimit(quiz.getTimeLimit());
        quizDTO.setUserId(quiz.getUser().getId());

        // Thêm câu hỏi vào quizDTO
        List<QuestionDTO> questionDTOs = quiz.getQuestions().stream().map(question -> {
            QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setQuestionText(question.getQuestionText());
            questionDTO.setOptions(List.of(question.getOption1(), question.getOption2(), question.getOption3(), question.getOption4()));
            questionDTO.setCorrectAnswerIndex(question.getCorrectAnswerIndex());
            return questionDTO;
        }).collect(Collectors.toList());

        quizDTO.setQuestions(questionDTOs);

        return ResponseEntity.ok(quizDTO);
    }

    @PutMapping("/updatePrivacy/{quizId}")
    public ResponseEntity<String> updateQuizPrivacy(
            @PathVariable Long quizId,
            @RequestParam boolean isPublic) {
        if (quizId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quiz ID không được để trống.");
        }
        try {
            quizService.updatePrivacy(quizId, isPublic);
            return ResponseEntity.ok("Cập nhật trạng thái quyền riêng tư thành công.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi cập nhật trạng thái quyền riêng tư.");
        }
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<?> getQuizByCodeAndPrivacy(
            @PathVariable String code,
            @RequestParam Long userId // ID của người dùng hiện tại
    ) {
        try {
            // Lấy danh sách quiz theo code
            List<Quiz> quizzes = quizService.findByCodeQuiz(code);

            // Bộ lọc quiz dựa trên quyền truy cập
            List<QuizDTO> quizDTOs = quizzes.stream()
                    .filter(quiz ->
                            // Hiển thị tất cả quiz của người dùng hiện tại
                            quiz.getUser().getId().equals(userId) ||
                                    // Hoặc hiển thị quiz public của người dùng khác
                                    !quiz.isPrivate()
                    )
                    .map(quiz -> {
                        // Chuyển Quiz sang QuizDTO
                        QuizDTO quizDTO = new QuizDTO();
                        quizDTO.setQuizId(quiz.getId());
                        quizDTO.setQuizName(quiz.getQuizName());
                        quizDTO.setCodeQuiz(quiz.getCodeQuiz());
                        quizDTO.setTimeLimit(quiz.getTimeLimit());
                        quizDTO.setUserId(quiz.getUser().getId());

                        // Chuyển câu hỏi sang DTO
                        List<QuestionDTO> questionDTOs = quiz.getQuestions().stream().map(question -> {
                            QuestionDTO questionDTO = new QuestionDTO();
                            questionDTO.setQuestionText(question.getQuestionText());
                            questionDTO.setOptions(List.of(
                                    question.getOption1(),
                                    question.getOption2(),
                                    question.getOption3(),
                                    question.getOption4()
                            ));
                            questionDTO.setCorrectAnswerIndex(question.getCorrectAnswerIndex());
                            return questionDTO;
                        }).collect(Collectors.toList());

                        quizDTO.setQuestions(questionDTOs);
                        return quizDTO;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(quizDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving quizzes: " + e.getMessage());
        }
    }


    @PutMapping("/edit/{quizId}")
    public Quiz editQuiz(@PathVariable Long quizId, @RequestBody Quiz updatedQuiz) {
        return quizService.updateQuiz(quizId, updatedQuiz);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createQuiz(@RequestBody QuizDTO quizDTO) {
        try {
            Long userId = quizDTO.getUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID must not be null");
            }

            // Kiểm tra người dùng có tồn tại trong cơ sở dữ liệu
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Tạo quiz mới từ dữ liệu DTO
            Quiz quiz = new Quiz();
            quiz.setQuizName(quizDTO.getQuizName());
            quiz.setCodeQuiz(quizDTO.getCodeQuiz());
            quiz.setTimeLimit(quizDTO.getTimeLimit());
            quiz.setUser(user);  // Liên kết quiz với user

            // Tạo danh sách câu hỏi cho quiz
            List<Question> questions = new ArrayList<>();
            for (QuestionDTO questionDTO : quizDTO.getQuestions()) {
                Question question = new Question();
                question.setQuestionText(questionDTO.getQuestionText());
                question.setOption1(questionDTO.getOptions().get(0));
                question.setOption2(questionDTO.getOptions().get(1));
                question.setOption3(questionDTO.getOptions().get(2));
                question.setOption4(questionDTO.getOptions().get(3));
                question.setCorrectAnswerIndex(questionDTO.getCorrectAnswerIndex());
                question.setQuiz(quiz);  // Liên kết câu hỏi với quiz
                questions.add(question);
            }
            quiz.setQuestions(questions);

            // Lưu quiz vào cơ sở dữ liệu
            quizRepository.save(quiz);

            return ResponseEntity.status(HttpStatus.CREATED).body("Quiz created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while creating the quiz: " + e.getMessage());
        }
    }

    // Thêm chức năng xóa quiz
    @DeleteMapping("/delete/{quizId}")
    public ResponseEntity<String> deleteQuiz(@PathVariable Long quizId) {
        try {
            if (quizId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quiz ID không được để trống");
            }
            Quiz quiz = quizRepository.findById(quizId)
                    .orElseThrow(() -> new RuntimeException("Quiz không tồn tại"));

            quizRepository.delete(quiz); // Xóa quiz
            return ResponseEntity.ok("Xóa quiz thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra khi xóa quiz: " + e.getMessage());
        }
    }

    public static class QuizDTO {
        private Long quizId;
        private String quizName;
        private String codeQuiz;
        private int timeLimit;
        private Long userId;
        private List<QuestionDTO> questions;

        public Long getQuizId() {
            return quizId;
        }

        public void setQuizId(Long quizId) {
            this.quizId = quizId;
        }

        public String getQuizName() {
            return quizName;
        }

        public void setQuizName(String quizName) {
            this.quizName = quizName;
        }

        public String getCodeQuiz() {
            return codeQuiz;
        }

        public void setCodeQuiz(String codeQuiz) {
            this.codeQuiz = codeQuiz;
        }

        public int getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(int timeLimit) {
            this.timeLimit = timeLimit;
        }

        public List<QuestionDTO> getQuestions() {
            return questions;
        }

        public void setQuestions(List<QuestionDTO> questions) {
            this.questions = questions;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }

    // DTO cho Question
    public static class QuestionDTO {
        private String questionText;
        private List<String> options;
        private int correctAnswerIndex;

        public String getQuestionText() {
            return questionText;
        }

        public void setQuestionText(String questionText) {
            this.questionText = questionText;
        }

        public List<String> getOptions() {
            return options;
        }

        public void setOptions(List<String> options) {
            this.options = options;
        }

        public int getCorrectAnswerIndex() {
            return correctAnswerIndex;
        }

        public void setCorrectAnswerIndex(int correctAnswerIndex) {
            this.correctAnswerIndex = correctAnswerIndex;
        }
    }

    // Sửa lại phương thức lấy Quiz theo UserId để trả về DTO
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<QuizDTO>> getQuizzesByUserId(@PathVariable Long userId) {
        List<Quiz> quizzes = quizRepository.findByUserId(userId);

        // Chuyển đổi từ Quiz sang QuizDTO để trả về API
        List<QuizDTO> quizDTOs = new ArrayList<>();
        for (Quiz quiz : quizzes) {
            QuizDTO quizDTO = new QuizDTO();
            quizDTO.setQuizId(quiz.getId());
            quizDTO.setQuizName(quiz.getQuizName());
            quizDTO.setCodeQuiz(quiz.getCodeQuiz());
            quizDTO.setTimeLimit(quiz.getTimeLimit());
            quizDTO.setUserId(quiz.getUser().getId());

            // Thêm danh sách câu hỏi vào quizDTO
            List<QuestionDTO> questionDTOs = new ArrayList<>();
            for (Question question : quiz.getQuestions()) {
                QuestionDTO questionDTO = new QuestionDTO();
                questionDTO.setQuestionText(question.getQuestionText());
                questionDTO.setOptions(List.of(question.getOption1(), question.getOption2(), question.getOption3(), question.getOption4()));
                questionDTO.setCorrectAnswerIndex(question.getCorrectAnswerIndex());
                questionDTOs.add(questionDTO);
            }
            quizDTO.setQuestions(questionDTOs);
            quizDTOs.add(quizDTO);
        }

        return ResponseEntity.ok(quizDTOs);
    }

}
