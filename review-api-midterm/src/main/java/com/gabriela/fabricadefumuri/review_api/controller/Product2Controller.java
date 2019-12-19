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

import com.gabriela.fabricadefumuri.review_api.entity.Product;
import com.gabriela.fabricadefumuri.review_api.entity.Review;
import com.gabriela.fabricadefumuri.review_api.repository.ProductMongoRepository;
import com.gabriela.fabricadefumuri.review_api.repository.ReviewMongoRepository;

/**
 * @author Gabriela Spiescu
 */
@RestController
public class Product2Controller {
	
	@Autowired private ProductMongoRepository repo;
	@Autowired private ReviewMongoRepository reviewRepo;
	
	@GetMapping("/findProducts")
	public ResponseEntity<List<Product>> getProducts() {
		return ResponseEntity.ok(repo.findAll());
	}
	
	@PostMapping("/addProduct")
	public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
		reviewRepo.saveAll(product.getReviews());
		repo.save(product);
		return ResponseEntity.ok(product);
	}

	@GetMapping("/findProducts/reviews/{id}")
	public ResponseEntity<List<Review>> getReviews(@PathVariable int id) {
		return ResponseEntity.ok(repo.findById(id).get().getReviews());
	}
	
	@GetMapping("/findProducts/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable int id) {
		return ResponseEntity.ok(repo.findById(id).get());
	}
	
	@DeleteMapping("/deleteProduct/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable int id) {
		repo.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
}
