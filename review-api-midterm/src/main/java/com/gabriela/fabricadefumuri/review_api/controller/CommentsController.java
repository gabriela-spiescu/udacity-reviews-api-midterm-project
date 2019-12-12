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

    // TODO: Wire needed JPA repositories here

    /**
     * Creates a comment for a review.
     *
     * 1. Add argument for comment entity. Use {@link RequestBody} annotation.
     * 2. Check for existence of review.
     * 3. If review not found, return NOT_FOUND.
     * 4. If found, save comment.
     *
     * @param reviewId The id of the review.
     */
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.POST)
    public ResponseEntity<?> createCommentForReview(@PathVariable("reviewId") Integer reviewId) {
        throw new HttpServerErrorException(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * List comments for a review.
     *
     * 2. Check for existence of review.
     * 3. If review not found, return NOT_FOUND.
     * 4. If found, return list of comments.
     *
     * @param reviewId The id of the review.
     */
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.GET)
    public List<?> listCommentsForReview(@PathVariable("reviewId") Integer reviewId) {
        throw new HttpServerErrorException(HttpStatus.NOT_IMPLEMENTED);
    }
}