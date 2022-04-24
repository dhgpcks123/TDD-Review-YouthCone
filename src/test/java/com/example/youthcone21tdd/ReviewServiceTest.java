package com.example.youthcone21tdd;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

//@SpringBootTest


class ReviewServiceTest {

    ReviewRepository reviewRepository = mock(ReviewRepository.class);
    ReviewService reviewService = new ReviewService(reviewRepository);

    //fixture
    private Long id = 1L;
    private String content = "재밌어요";
    private String phoneNumber = "010-1111-2222";

    @Test
    void 후기_조회_성공(){
        //given
        given(reviewRepository.findById(id))
                .willReturn(Optional.of(new Review(id, content, phoneNumber)));
        //when
        Review review = reviewService.getById(id);
        //then
        assertThat(review.getId()).isEqualTo(id);
        assertThat(review.getContent()).isEqualTo(content);
        assertThat(review.getPhoneNumber()).isEqualTo(phoneNumber);
    }
}