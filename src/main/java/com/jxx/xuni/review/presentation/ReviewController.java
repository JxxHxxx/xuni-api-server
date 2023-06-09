package com.jxx.xuni.review.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.presentation.AuthenticatedMember;
import com.jxx.xuni.review.application.ReviewService;
import com.jxx.xuni.review.dto.request.ReviewForm;
import com.jxx.xuni.review.dto.request.ReviewUpdateForm;
import com.jxx.xuni.review.dto.response.RatingResponse;
import com.jxx.xuni.review.dto.response.ReviewApiResult;
import com.jxx.xuni.review.dto.response.ReviewApiSimpleResult;
import com.jxx.xuni.review.dto.response.ReviewOneResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.jxx.xuni.review.dto.response.ReviewApiMessage.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/study-products/{study-product-id}/reviews")
    public ResponseEntity<ReviewApiSimpleResult> createReview(@AuthenticatedMember MemberDetails memberDetails,
                                                              @PathVariable("study-product-id") String studyProductId,
                                                              @RequestBody @Validated ReviewForm form) {

        reviewService.create(memberDetails, studyProductId, form);
        return new ResponseEntity<>(ReviewApiSimpleResult.create(REVIEW_CREATE), CREATED);
    }

    @GetMapping("/study-products/{study-product-id}/reviews")
    public ResponseEntity<ReviewApiResult> readReviewBy(@PathVariable("study-product-id") String studyProductId) {
        List<ReviewOneResponse> response = reviewService.read(studyProductId);

        return ResponseEntity.ok(new ReviewApiResult(200, REVIEW_READ, response));
    }

    @GetMapping("/study-products/{study-product-id}/rating-avg")
    public ResponseEntity<ReviewApiResult> readRatingAvg(@PathVariable("study-product-id") String studyProductId) {
        RatingResponse response = reviewService.readRatingAvg(studyProductId);

        return ResponseEntity.ok(new ReviewApiResult(200, RATING_AVG, response));
    }

    @PatchMapping("/reviews/{review-id}")
    public ResponseEntity<ReviewApiSimpleResult> updateReview(@AuthenticatedMember MemberDetails memberDetails,
                                                              @PathVariable("review-id") Long reviewId,
                                                              @RequestBody @Validated ReviewUpdateForm form) {
        reviewService.updateReview(reviewId, memberDetails.getUserId(), form);
        return new ResponseEntity<>(ReviewApiSimpleResult.update(REVIEW_UPDATE), OK);
    }

    @DeleteMapping("/reviews/{review-id}")
    public ResponseEntity<ReviewApiSimpleResult> deleteReview(@AuthenticatedMember MemberDetails memberDetails,
                                                              @PathVariable("review-id") Long reviewId) {
        reviewService.deleteReview(reviewId, memberDetails.getUserId());
        return new ResponseEntity<>(ReviewApiSimpleResult.update(REVIEW_DELETE), OK);
    }

}
