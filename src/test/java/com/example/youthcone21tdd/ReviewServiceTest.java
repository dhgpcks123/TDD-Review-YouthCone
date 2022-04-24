package com.example.youthcone21tdd;

import com.example.youthcone21tdd.Infra.GiftApi;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

//@SpringBootTest


class ReviewServiceTest {

    private GiftApi giftApi = mock(GiftApi.class); //외부서비스
    ReviewRepository reviewRepository = mock(ReviewRepository.class);
    ReviewService reviewService = new ReviewService(reviewRepository, giftApi);

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

    @Test
    void 후기_조회_실패(){
        //given
        given(reviewRepository.findById(1000L))
                .willReturn(Optional.empty());

        assertThatThrownBy(() ->
            reviewService.getById(1000L))
                .isInstanceOf(ReviewNotFoundException.class);
    }

    @Test
    void 선물하기_성공() throws Exception, SendGiftInternalException, DuplicateSendGiftException {
        //given
        given(reviewRepository.findById(id)).willReturn(Optional.of(new Review(id, content, phoneNumber, false)));
        given(giftApi.send(phoneNumber)).willReturn(true);
        given(reviewRepository.save(any(Review.class))).willReturn(new Review(id, content, phoneNumber, true));

        //when
        Review review = reviewService.sendGift(id);
        //then
        assertThat(review.getId()).isEqualTo(id);
        assertThat(review.getIsSent()).isEqualTo(true);
    }

    @Test
    void 선물하기_중복_지급_실패(){
        // 준비
        given(reviewRepository.findById(id)).willReturn(Optional.of(new Review(id, content, phoneNumber, true)));

        assertThatThrownBy(() ->
                // 실행
                reviewService.sendGift(id))
                // 검증
                .isInstanceOf(DuplicateSendGiftException.class);
    }

    @Test
    void 선물하기_외부_요청_실패(){
        // 준비
        given(reviewRepository.findById(id)).willReturn(Optional.of(new Review(id, content, phoneNumber, false)));
        given(giftApi.send(phoneNumber)).willReturn(false);

        assertThatThrownBy(() ->
                // 실행
                reviewService.sendGift(id))
                // 검증
                .isInstanceOf(SendGiftInternalException.class);
    }

}