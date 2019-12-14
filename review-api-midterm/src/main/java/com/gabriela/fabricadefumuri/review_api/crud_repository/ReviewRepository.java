package com.gabriela.fabricadefumuri.review_api.crud_repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gabriela.fabricadefumuri.review_api.entity.Product;
import com.gabriela.fabricadefumuri.review_api.entity.Review;

/**
 * @author Gabriela Spiescu
 */
@Repository
public interface ReviewRepository extends CrudRepository<Review, Integer> {
	
    /**
     * Finds all {@link ReviewMysql} for a product.
     *
     * @param product The {@link Product} object.
     * @return The list of reviews for the product.
     */
    List<Review> findAllByProductid(int productid);

}
