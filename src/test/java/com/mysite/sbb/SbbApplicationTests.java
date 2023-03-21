package com.mysite.sbb;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SbbApplicationTests {
    @Autowired
    private QuestionRepository questionRepository;

    // 얘는 데이터 저장이니까 한번만 들어가야함
    // 두번 들어가면 오류날 수도 있음
    // 그럴땐 sql에서 init.sql의 테이블 내용 초기화를 실행하자

    @BeforeEach
    void beforeEach() {
        // 모든 데이터 삭제
        questionRepository.deleteAll();

        // 흔적 삭제 (다음번 INSERT 때 id가 1번으로 설정되도록)
        questionRepository.clearAutoIncrement();

        // 질문 1개 생성
        Question q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        questionRepository.save(q1);  // 첫번째 질문 저장

        // 질문 1개 생성
        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        questionRepository.save(q2);  // 두번째 질문 저장
    }
    @Test
    @DisplayName("데이터 저장")
    void t001() {
        Question q = new Question();
        q.setSubject("세계에서 가장 부유한 국가가 어디인가요?");
        q.setContent("알고 싶습니다.");
        q.setCreateDate(LocalDateTime.now());
        questionRepository.save(q);

        assertEquals("세계에서 가장 부유한 국가가 어디인가요?", questionRepository.findById(3).get().getSubject());
    }

    /*
    SQL
    SELECT * FROM question
     */
    @Test
    @DisplayName("findAll")
    void t002() {
        List<Question> all = questionRepository.findAll();
        assertEquals(2, all.size());

        Question q = all.get(0);
        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }

    /*
    SQL
    SELECT *
    FROM question
    WHERE id = 1
     */
    @Test
    @DisplayName("findByID")
    void t003() {
        Optional<Question> oq = questionRepository.findById(1);

        if (oq.isPresent()) {
            Question q = oq.get();
            assertEquals("sbb가 무엇인가요?", q.getSubject());
        }
    }

    /*
    SQL
    SELECT *
    FROM question
    WHERE subject = 'sbb가 무엇인가요?'
     */
    @Test
    @DisplayName("findBySubject")
    void t004() {
        Question q = questionRepository.findBySubject("sbb가 무엇인가요?");
        assertEquals(1, q.getId());
    }

    @Test
    @DisplayName("findBySubjectAndContent")
    void t005() {
        Question q = questionRepository.findBySubjectAndContent(
                "sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다."
        );
        assertEquals(1, q.getId());
    }

    @Test
    @DisplayName("findBySubjectAndContent")
    void t006() {
        List<Question> qList = questionRepository.findBySubjectLike("sbb%");
        Question q = qList.get(0);
        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }

    /*
    SQL
    UPDATE
        question
    SET
        content = ?,
        create_date = ?,
        subject = ?
    WHERE
        id = ?
    */
    @Test
    void t007() {
        Optional<Question> oq = this.questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();
        q.setSubject("수정된 제목");
        this.questionRepository.save(q);
    }

    /*
    SQL
    DELETE
    FROM
        question
    WHERE
        id = ?
    */
    @Test
    void t008() {
        // questionRepository.count()
        // SQL : SELECT COUNT(*) FROM question;
        assertEquals(2, this.questionRepository.count());
        Optional<Question> oq = this.questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();
        this.questionRepository.delete(q);
        assertEquals(1, this.questionRepository.count());
    }
}