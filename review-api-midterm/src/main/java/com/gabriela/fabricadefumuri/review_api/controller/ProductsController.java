package com.gabriela.fabricadefumuri.review_api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gabriela.fabricadefumuri.review_api.crud_repository.ProductRepository;
import com.gabriela.fabricadefumuri.review_api.entity.Product;
import com.gabriela.fabricadefumuri.review_api.repository.ProductMongoRepository;

/**
 * Spring REST controller for working with {@link Product} entity.
 */
@RestController
@RequestMapping("/products")
public class ProductsController {

    private ProductRepository productRepository;
    @Autowired private ProductMongoRepository repo;

    public ProductsController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Creates a {@link Product} entity.
     *
     * @param product The {@link Product} to create.
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody Product product) {
    	repo.save(product);
        productRepository.save(product);
    }

    /**
     * Finds a {@link Product} by id.
     *
     * @param id The id of the product.
     * @return The product if found, or a 404 not found.
     */
    @RequestMapping(value = "/{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") Integer id) {
        return ResponseEntity.of(productRepository.findById(id));
    }

    /**
     * Lists all products.
     *
     * @return The list of products.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Product> listProducts() {
    	List<Product> result = new ArrayList<Product>();
        productRepository.findAll().iterator().forEachRemaining(result::add);
        return result;
    }
}