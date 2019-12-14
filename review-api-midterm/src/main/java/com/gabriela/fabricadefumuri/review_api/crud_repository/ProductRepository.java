package com.gabriela.fabricadefumuri.review_api.crud_repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//import com.gabriela.fabricadefumuri.review_api.entity.Product;
import com.gabriela.fabricadefumuri.review_api.entity.Product;

/**
 * Spring Data JPA Repository for {@link Product} entity.
 */
@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

}