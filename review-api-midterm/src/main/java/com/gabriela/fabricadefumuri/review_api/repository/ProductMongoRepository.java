package com.gabriela.fabricadefumuri.review_api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gabriela.fabricadefumuri.review_api.entity.Product;

/**
 * @author Gabriela Spiescu
 */
@Repository
public interface ProductMongoRepository extends MongoRepository<Product, Integer> {
	
//	Product findByDocumentId(String ddd);
//	Product findByName(String name);
//	
//	@Query(value = "{'reviews.author': ?0}", fields = "{'reviews':0}")
//	Product findProductByReviewAuthor(String name);
	
}
