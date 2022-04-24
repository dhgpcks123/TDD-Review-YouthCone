package com.example.youthcone21tdd;

import com.example.youthcone21tdd.Infra.GiftApi;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final com.example.youthcone21tdd.Infra.GiftApi giftApi;


    public ReviewService(ReviewRepository reviewRepository, GiftApi giftApi) {
        this.reviewRepository = reviewRepository;
        this.giftApi = giftApi;
    }

    public Review getById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("no review"+id));
    }
    public Review sendGift(Long id) throws DuplicateSendGiftException, SendGiftInternalException {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("no review" + id));

        if(review.getIsSent()){
            throw new DuplicateSendGiftException("duplicate review id : "+ id);
        }

        if(!giftApi.send(review.getPhoneNumber())){
            throw new SendGiftInternalException("internal exception");
        }
        review.makeTrue();

        return review;
    }
    //Command shift T
}
