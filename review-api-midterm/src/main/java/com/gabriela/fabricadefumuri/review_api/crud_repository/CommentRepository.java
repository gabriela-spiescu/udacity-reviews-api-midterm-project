package com.gabriela.fabricadefumuri.review_api.crud_repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gabriela.fabricadefumuri.review_api.entity.Comment;

/**
 * @author Gabriela Spiescu
 */
@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {
    /**
     * Finds all {@link Comment} for a review.
     *
     * @param review The {@link ReviewMysql} object.
     * @return The list of comments for the review.
     */
    List<Comment> findAllByReviewId(int reviewid);
}
