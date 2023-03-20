package com.mysite.sbb;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// answer 테이블이 생김
@Getter
@Setter
@Entity
public class Answer {
    @Id // PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Integer id; // INT id
    @Column(columnDefinition = "TEXT") //TEXT
    private String content;

    private LocalDateTime createDate; //DATETIME

    @ManyToOne // 아래처럼 다른 엔티티 클래스 리모콘을 저장할 때는 꼭 관계를 적어준다.
    private Question question;
}
