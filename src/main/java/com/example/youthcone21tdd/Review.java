package com.example.youthcone21tdd;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Review {
    
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "IS_SENT")
    private Boolean isSent;

    public Review(Long id, String content, String phoneNumber) {
        this.id = id;
        this.content = content;
        this.phoneNumber = phoneNumber;
    }

    public void makeTrue(){
        this.isSent = true;
    }

    @PrePersist
    public void prePersistYn() {
        this.isSent = false;
    }
}
