
package com.example.quiz.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.quiz.entity.QuizFour;
import com.example.quiz.repository.QuizFourRepository;

@Service
@Transactional
public class QuizFourServiceImpl implements QuizFourService {

    /** Repository：注入 */
    @Autowired
    QuizFourRepository repository;

    @Override
    public Iterable<QuizFour> selectAll() {
        return repository.findAll();
    }

    @Override
    public Optional<QuizFour> selectOneById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Optional<QuizFour> selectOneRandomQuiz() {
        Integer randId = repository.getRandomId();

        if (randId == null) {
            return Optional.empty();
        }

        return repository.findById(randId);
    }

    @Override
    public Boolean checkQuiz(Integer id, Integer myAnswer) {
        Optional<QuizFour> optQuiz = repository.findById(id);

        if (optQuiz.isPresent()) {
            QuizFour quiz = optQuiz.get();
            return quiz.getAnswer().equals(myAnswer);
        }

        return false;
    }

    @Override
    public void insertQuiz(QuizFour quiz) {
        repository.save(quiz);
    }

    @Override
    public void updateQuiz(QuizFour quiz) {
        repository.save(quiz);
    }

    @Override
    public void deleteQuizById(Integer id) {
        repository.deleteById(id);
    }
}
