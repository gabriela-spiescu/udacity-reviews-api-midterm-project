package com.gabriela.fabricadefumuri.review_api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import com.gabriela.fabricadefumuri.review_api.entity.Comment;
import com.gabriela.fabricadefumuri.review_api.entity.Product;
import com.gabriela.fabricadefumuri.review_api.entity.Review;
import com.gabriela.fabricadefumuri.review_api.repository.CommentMongoRepository;
import com.gabriela.fabricadefumuri.review_api.repository.ProductMongoRepository;
import com.gabriela.fabricadefumuri.review_api.repository.ReviewMongoRepository;

/**
 * @author Gabriela Spiescu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class Review2ControllerTest {
	
	  @MockBean
	  private ReviewMongoRepository reviewRepository;
	  
	  @MockBean
	  private CommentMongoRepository commentRepository;
	  
	  @MockBean
	  private ProductMongoRepository productRepository;
	  
	  @Autowired
	  private MockMvc mvc;
	    
	  @Autowired
	  private JacksonTester<Review> json;

	  
	  public Comment getComment(int id) {
		  Comment comment = new Comment();
		  comment.setId(id);
		  comment.setTitle("Nice job");
		  comment.setBody("More work need to be done");
		  comment.setIsPositiv(true);
		  return comment;
	  }
	  
	  public Review getReview(int id) {
		  Review r = new Review();
		  r.setId(id);
		  r.setAuthor("Boboby");
		  r.setScore(4);
		  r.setTitle("Lovely thing");
		  r.setProductid(1);
		  r.setComments(new ArrayList<Comment>());
		  return r;
	  }
	  
	  public Product getProduct(int id) {
		  Product p = new Product();
		  p.setId(id);
		  p.setName("TRF");
		  p.setModel("NICE");
		  p.setManufacturer("MYNE");
		  p.setPrice(123.6);
		  p.setAvailability(false);
		  p.setDescription("Nice photos");
		  p.setReviews(new ArrayList<Review>());
		  return p;
	  }
	  
	  @Test
	  public void createReview() throws URISyntaxException, IOException, Exception {
		  Review r = getReview(1);
		  Product p = getProduct(1);
		  r.setProductid(p.getId());
		  Optional<Product> proOpt = Optional.of(p);
		  Mockito.when(productRepository.findById(p.getId())).thenReturn(proOpt);
		  Optional<Review> review = Optional.of(r);
		  Mockito.when(reviewRepository.findById(r.getId())).thenReturn(review);
		  mvc.perform(post(new URI("/addReview"))
				  .content(json.write(r).getJson())
				  .contentType(MediaType.APPLICATION_JSON_UTF8)
				  .accept(MediaType.APPLICATION_JSON_UTF8))
		  .andExpect(status().isOk());
	  }
	  
	  @Test
	  public void listReviewsById() throws IOException, Exception {
		  Review c1 = getReview(1);
		  Optional<Review> review = Optional.of(c1);
		  Mockito.when(reviewRepository.findById(c1.getId())).thenReturn(review);
		  mvc.perform(get("/findReviews/" + c1.getId())
			    	 .contentType(MediaType.APPLICATION_JSON_UTF8)
			    	 .accept(MediaType.APPLICATION_JSON_UTF8)
			    	 .content(convertObjectToJsonBytes(c1)))
			         .andExpect(status().isOk())
			         .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			         .andExpect(jsonPath("$.id", is(1)))
			         .andExpect(jsonPath("$.title", is("Lovely thing")))
			         .andExpect(jsonPath("$").exists());
	  }
	  
	  @Test
	  public void listAllReviews() throws IOException, Exception {
		  Review c1 = getReview(1);
		  Review c2 = getReview(2);
		  Review c3 = getReview(3);
		  List<Review> reviews = new ArrayList<Review>();
		  reviews.add(c1);
		  reviews.add(c2);
		  reviews.add(c3);
		  Mockito.when(reviewRepository.findAll()).thenReturn(reviews);
		  mvc.perform(get("/findReviews/")
			    	 .contentType(MediaType.APPLICATION_JSON_UTF8)
			    	 .accept(MediaType.APPLICATION_JSON_UTF8)
			    	 .content(convertObjectToJsonBytes(c1))
			    	 .content(convertObjectToJsonBytes(c2))
			    	 .content(convertObjectToJsonBytes(c3)))
			         .andExpect(status().isOk())
			         .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			         .andExpect(jsonPath("$.[0].id", is(1)))
			         .andExpect(jsonPath("$.[1].id", is(2)))
			         .andExpect(jsonPath("$.[2].id", is(3)))
			         .andExpect(jsonPath("$", hasSize(3)));
	  }
	  
	  @Test
	  public void getCommentsFromReview() throws URISyntaxException, IOException, Exception {
		  Comment comment = getComment(1);
		  Review r = getReview(2);
		  comment.setReviewId(r.getId());
		  r.getComments().add(comment);
		  Optional<Review> review = Optional.of(r);
		  Mockito.when(reviewRepository.findById(r.getId())).thenReturn(review);
		  mvc.perform(get(new URI("/findReviews/comments/" + r.getId()))
				  .content(json.write(r).getJson())
				  .contentType(MediaType.APPLICATION_JSON_UTF8)
				  .accept(MediaType.APPLICATION_JSON_UTF8)
				  .content(convertObjectToJsonBytes(comment)))
				  .andExpect(jsonPath("$.[0].id", is(1)))
				  .andExpect(jsonPath("$.[0].title", is("Nice job")))
		  .andExpect(status().isOk());
	  }
	  
	  @Test
	  public void deleteReview() throws IOException, Exception {
		  Review c1 = getReview(1);
		  Review c2 = getReview(2);
		  Review c3 = getReview(3);
		  List<Review> reviews = new ArrayList<Review>();
		  reviews.add(c1);
		  reviews.add(c2);
		  reviews.add(c3);
		  Mockito.when(reviewRepository.findAll()).thenReturn(reviews);
		  mvc.perform(delete("/deleteReview/" + c1.getId())
			    	 .contentType(MediaType.APPLICATION_JSON_UTF8)
			    	 .accept(MediaType.APPLICATION_JSON_UTF8)
			    	 .content(convertObjectToJsonBytes(c1))
			    	 .content(convertObjectToJsonBytes(c2))
			    	 .content(convertObjectToJsonBytes(c3)))
			         .andExpect(status().isNoContent());
	  }
	  
	    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
	        ObjectMapper mapper = new ObjectMapper();
	        return mapper.writeValueAsBytes(object);
	    }
}
