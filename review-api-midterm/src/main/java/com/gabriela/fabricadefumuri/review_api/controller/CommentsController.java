package com.gabriela.fabricadefumuri.review_api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gabriela.fabricadefumuri.review_api.crud_repository.CommentRepository;
import com.gabriela.fabricadefumuri.review_api.crud_repository.ReviewRepository;
import com.gabriela.fabricadefumuri.review_api.entity.Comment;
import com.gabriela.fabricadefumuri.review_api.entity.Review;
import com.gabriela.fabricadefumuri.review_api.repository.CommentMongoRepository;
import com.gabriela.fabricadefumuri.review_api.repository.ReviewMongoRepository;


/**
 * Spring REST controller for working with {@link Comment} entity.
 */
@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private CommentRepository commentRepository;
    
    @Autowired private ReviewMongoRepository reviewMongoRepository;
    @Autowired private CommentMongoRepository commentMongoRepository;

    /**
     * Creates {@link Comment} for a {@link Review}.
     *
     * @param reviewId The id of the review.
     * @param comment The comment to create.
     * @return the created comment or NOT_FOUND if review is not found.
     */
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.POST)
    public ResponseEntity<Comment> createCommentForReview(@PathVariable("reviewId") Integer reviewId, @RequestBody Comment comment) {
    	createCommentForReviewMongo(reviewId,comment);
    	Optional<Review> optional = reviewRepository.findById(reviewId);
        if (optional.isPresent()) {
            comment.setReviewId(reviewId);
            Review review = optional.get();
            List<Comment> comments = review.getComments();
            comments.add(comment);
            review.setComments(comments);
            reviewRepository.save(review);
            return ResponseEntity.ok(commentRepository.save(comment));
            
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    private void createCommentForReviewMongo(Integer reviewId,Comment comment) {
    	Optional<Review> optional = reviewMongoRepository.findById(reviewId);
        if (optional.isPresent()) {
            comment.setReviewId(reviewId);
            Review review = optional.get();
            List<Comment> comments = review.getComments();
            comments.add(comment);
            review.setComments(comments);
            reviewMongoRepository.save(review);
            commentMongoRepository.save(comment);
        }
    }

    /**
     * List {@link Comment}s for a {@link Review}.
     *
     * @param reviewId The id of the review.
     * @return The list of comments.
     */
    @RequestMapping(value = "/reviews/{reviewId}/{isMysql}", method = RequestMethod.GET)
    public ResponseEntity<List<Comment>> listCommentsForReview(@PathVariable("reviewId") Integer reviewId, @PathVariable("isMysql") Boolean isMysql) {
        if (isMysql) {
        	return ResponseEntity.ok(commentRepository.findAllByReviewId(reviewId));
        } else {
        	return ResponseEntity.ok(reviewMongoRepository.findById(reviewId).get().getComments());
        }
    	
    }
}