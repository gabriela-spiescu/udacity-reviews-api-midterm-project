package com.gabriela.fabricadefumuri.review_api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gabriela.fabricadefumuri.review_api.entity.Comment;

/**
 * @author Gabriela Spiescu
 */
@Repository
public interface CommentMongoRepository extends MongoRepository<Comment, Integer> {

}
