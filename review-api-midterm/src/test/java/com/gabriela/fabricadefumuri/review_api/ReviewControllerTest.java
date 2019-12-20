package com.gabriela.fabricadefumuri.review_api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriela.fabricadefumuri.review_api.crud_repository.ProductRepository;
import com.gabriela.fabricadefumuri.review_api.crud_repository.ReviewRepository;
import com.gabriela.fabricadefumuri.review_api.entity.Product;
import com.gabriela.fabricadefumuri.review_api.entity.Review;
import com.gabriela.fabricadefumuri.review_api.repository.ProductMongoRepository;

/**
 * @author Gabriela Spiescu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ReviewControllerTest {
	
	@MockBean
	private ReviewRepository reviewRepository;
	
	@MockBean
	private ProductMongoRepository productMongoRepository;
	
	@MockBean
	private ProductRepository productRepository;
	
	@Autowired
	private MockMvc mvc;
	    
	@Autowired
	private JacksonTester<Review> json;
	
	public Review getReview(int id) {
		Review r = new Review();
		r.setId(id);
		r.setAuthor("Booby");
		r.setScore(4);
		r.setTitle("Lovely thing");
		r.setProductid(1);
		return r;
	  }	
	
	@Test
	public void listReviewsForProduct() throws Exception {
		List<Review> reviews = new ArrayList<Review>();
		reviews.add(getReview(1));
		reviews.add(getReview(2));
		reviews.add(getReview(3));
		Product product = new Product();
		product.setId(1);
		product.setReviews(reviews);
		Mockito.when(reviewRepository.findAllByProductid(product.getId())).thenReturn(reviews);
		mvc.perform(get("/reviews/products/" + product.getId()+"/true")
		    	 .contentType(MediaType.APPLICATION_JSON_UTF8)
		    	 .accept(MediaType.APPLICATION_JSON_UTF8)
		.content(convertObjectToJsonBytes(reviews)))
		         .andExpect(status().isOk())
		         .andExpect(jsonPath("$.[0].id", is(1)))
		         .andExpect(jsonPath("$.[1].id", is(2)))
		         .andExpect(jsonPath("$.[0].author", is("Booby")))
		         .andExpect(jsonPath("$", hasSize(3)));
		reviews.add(getReview(4));
		reviews.add(getReview(5));
		Optional<Product> opt = Optional.of(product);
		Mockito.when(productMongoRepository.findById(product.getId())).thenReturn(opt);
		mvc.perform(get("/reviews/products/" + product.getId()+"/false")
		    	 .contentType(MediaType.APPLICATION_JSON_UTF8)
		    	 .accept(MediaType.APPLICATION_JSON_UTF8)
		.content(convertObjectToJsonBytes(reviews)))
		         .andExpect(status().isOk())
		         .andExpect(jsonPath("$.[0].id", is(1)))
		         .andExpect(jsonPath("$.[1].id", is(2)))
		         .andExpect(jsonPath("$.[0].author", is("Booby")))
		         .andExpect(jsonPath("$", hasSize(5)));
	}
	
	
	@Test
	public void createReviewForProduct() throws URISyntaxException, IOException, Exception {
		Product product = new Product();
		product.setId(1);
		Review r = getReview(1);
		r.setProductid(product.getId());
		Optional<Product> opt = Optional.of(product);
		Mockito.when(productMongoRepository.findById(product.getId())).thenReturn(opt);
		Mockito.when(productRepository.findById(product.getId())).thenReturn(opt);
		Mockito.when(productRepository.findById(product.getId())).thenReturn(opt);
		mvc.perform(post(new URI("/reviews/products/" + product.getId()))
				  .content(json.write(r).getJson())
				  .contentType(MediaType.APPLICATION_JSON_UTF8)
				  .accept(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk());
		
	}
	
	
    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsBytes(object);
    }

}
