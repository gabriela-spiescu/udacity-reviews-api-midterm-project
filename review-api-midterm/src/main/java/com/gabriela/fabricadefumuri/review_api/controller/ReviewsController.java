package com.gabriela.fabricadefumuri.review_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gabriela.fabricadefumuri.review_api.crud_repository.ProductRepository;
import com.gabriela.fabricadefumuri.review_api.crud_repository.ReviewRepository;
import com.gabriela.fabricadefumuri.review_api.entity.Product;
import com.gabriela.fabricadefumuri.review_api.entity.Review;
import com.gabriela.fabricadefumuri.review_api.repository.ProductMongoRepository;
import com.gabriela.fabricadefumuri.review_api.repository.ReviewMongoRepository;

/**
 * Spring REST controller for working with {@link ReviewMysql} entity.
 */
@RestController
public class ReviewsController {

	@Autowired
    private ReviewRepository reviewRepository;
	@Autowired
    private ProductRepository productRepository;
	@Autowired
    private ProductMongoRepository productMongoRepository;
	@Autowired
    private ReviewMongoRepository reviewMongoRepository;
	
    /**
     * Creates a {@link Review} for a {@link Product}.
     * I am using both DB: Mysql and MongoDB, so the new review
     * is saved in both of them.
     *
     * @param productId The id of the product.
     * @param review The {@link Review} to create.
     * @return The created review or 404 if product id is not found.
     */
	@RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.POST)
    public ResponseEntity<Review> createReviewForProduct(@PathVariable("productId") Integer productId, @RequestBody Review review) {
        createReviewForProductMongo(productId, review);
    	Product product = productRepository.findById(productId).get();
        if (product != null) {
            productRepository.save(product);
            review.setProductid(productId);
            return ResponseEntity.ok(reviewRepository.save(review));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    private void createReviewForProductMongo(Integer productId, Review review) {
    	Product productM = productMongoRepository.findById(productId).get();
        if (productM != null) {
            List<Review> reviewsM = productM.getReviews();
            reviewsM.add(review);
            productM.setReviews(reviewsM);
            productMongoRepository.save(productM);
            review.setProductid(productId);
            reviewMongoRepository.save(review);
        }
    }

    /**
     * Lists reviews by product.
     * I chose to list the reviews form Mysql DB, but 
     * it's 
     *
     * @param productId The id of the product.
     * @return The list of reviews.
     */
    @RequestMapping(value = "/reviews/products/{productId}/{isMysql}", method = RequestMethod.GET)
    public ResponseEntity<List<Review>> listReviewsForProduct(@PathVariable("productId") Integer productId, @PathVariable("isMysql") Boolean isMysql) {
        if (isMysql) {
        	return ResponseEntity.ok(reviewRepository.findAllByProductid(productId));
        } else {
        	return ResponseEntity.ok(productMongoRepository.findById(productId).get().getReviews());
        }
    }
    
}