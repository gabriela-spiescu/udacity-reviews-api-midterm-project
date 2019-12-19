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
import com.gabriela.fabricadefumuri.review_api.repository.CommentMongoRepository;
import com.gabriela.fabricadefumuri.review_api.repository.ProductMongoRepository;
import com.gabriela.fabricadefumuri.review_api.repository.ReviewMongoRepository;

/**
 * @author Gabriela Spiescu
 */
@RestController
public class Comments2Controller {
	
	@Autowired private ProductMongoRepository productRepo;
	@Autowired private ReviewMongoRepository reviewRepo;
	@Autowired private CommentMongoRepository commentRepo;
	
	@GetMapping("/findComments")
	public ResponseEntity<List<Comment>> getComments() {
		return ResponseEntity.ok(commentRepo.findAll());
	}
	
	@PostMapping("/addComment")
	public ResponseEntity<Comment> saveProduct(@RequestBody Comment comment) {
		commentRepo.save(comment);
		Review review = reviewRepo.findById(comment.getReviewId()).get();
		List<Comment>  comments = review.getComments();
		comments.add(comment);
		review.setComments(comments);
		reviewRepo.save(review);
		return ResponseEntity.ok(comment);
	}

	@GetMapping("/findComments/product/{id}")
	public ResponseEntity<Product>  getProduct(@PathVariable int id) {
		Comment c = commentRepo.findById(id).get();
		Review r = reviewRepo.findById(c.getReviewId()).get();
		return ResponseEntity.ok(productRepo.findById(r.getProductid()).get());
	}
	
	@GetMapping("/findComments/{id}")
	public ResponseEntity<Comment> getComment(@PathVariable int id) {
		return ResponseEntity.ok(commentRepo.findById(id).get());
	}
	
	@DeleteMapping("/deleteComment/{id}")
	public ResponseEntity<?> deleteComment(@PathVariable int id) {
		commentRepo.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
}
