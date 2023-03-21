package com.mysite.sbb;

import org.hibernate.boot.model.relational.QualifiedSequenceName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findBySubject(String subject);

    Question findBySubjectAndContent(String subject, String content);

    List<Question> findBySubjectLike(String subject);

    @Query(value = "ALTER TABLE question AUTO_INCREMENT = 1", nativeQuery = true)
    void clearAutoIncrement();
}
