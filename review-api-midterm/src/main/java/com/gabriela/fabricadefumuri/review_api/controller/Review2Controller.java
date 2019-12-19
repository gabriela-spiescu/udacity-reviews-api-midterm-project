package com.gabriela.fabricadefumuri.review_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gabriela.fabricadefumuri.review_api.entity.Comment;
import com.gabriela.fabricadefumuri.review_api.entity.Product;
import com.gabriela.fabricadefumuri.review_api.entity.Review;
import com.gabriela.fabricadefumuri.review_api.repository.ProductMongoRepository;
import com.gabriela.fabricadefumuri.review_api.repository.ReviewMongoRepository;

/**
 * @author Gabriela Spiescu
 */
@RestController
public class Review2Controller {
	
	@Autowired private ProductMongoRepository productRepo;
	@Autowired private ReviewMongoRepository reviewRepo;
	
	@GetMapping("/findReviews")
	public ResponseEntity<List<Review>> getProducts() {
		return ResponseEntity.ok(reviewRepo.findAll());
	}
	
	@PostMapping("/addReview")
	public ResponseEntity<Review> saveReview(@RequestBody Review review) {
		reviewRepo.save(review);
		Product product = productRepo.findById(review.getProductid()).get();
		List<Review> reviews = product.getReviews();
		reviews.add(review);
		productRepo.save(product);
		return ResponseEntity.ok(review);
	}

	@GetMapping("/findReviews/comments/{id}")
	public ResponseEntity<List<Comment>> getComments(@PathVariable int id) {
		return ResponseEntity.ok(reviewRepo.findById(id).get().getComments());
	}
	
	@GetMapping("/findReviews/{id}")
	public ResponseEntity<Review> getReview(@PathVariable int id) {
		return ResponseEntity.ok(reviewRepo.findById(id).get());
	}
	
	@DeleteMapping("/deleteReview/{id}")
	public ResponseEntity<?> deleteReview(@PathVariable int id) {
		reviewRepo.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
}
