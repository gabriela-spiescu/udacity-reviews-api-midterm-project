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
public class Comment2ControllerTest {
	
	  @MockBean
	  private ReviewMongoRepository reviewRepository;
	  
	  @MockBean
	  private CommentMongoRepository commentRepository;
	  
	  @MockBean
	  private ProductMongoRepository productRepository;
	  
	  @Autowired
	  private MockMvc mvc;
	    
	  @Autowired
	  private JacksonTester<Comment> json;

	  
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
		  p.setReviews(null);
		  return p;
	  }
	  
	  @Test
	  public void createComment() throws URISyntaxException, IOException, Exception {
		  Comment comment = getComment(1);
		  Review r = getReview(2);
		  Optional<Review> review = Optional.of(r);
		  comment.setReviewId(r.getId());
		  Mockito.when(reviewRepository.findById(r.getId())).thenReturn(review);
		  mvc.perform(post(new URI("/addComment"))
				  .content(json.write(comment).getJson())
				  .contentType(MediaType.APPLICATION_JSON_UTF8)
				  .accept(MediaType.APPLICATION_JSON_UTF8))
		  .andExpect(status().isOk());
	  }
	  
	  @Test
	  public void listCommentsById() throws IOException, Exception {
		  Comment c1 = getComment(1);
		  Optional<Comment> co1 = Optional.of(c1);
		  Mockito.when(commentRepository.findById(c1.getId())).thenReturn(co1);
		  mvc.perform(get("/findComments/" + c1.getId())
			    	 .contentType(MediaType.APPLICATION_JSON_UTF8)
			    	 .accept(MediaType.APPLICATION_JSON_UTF8)
			    	 .content(convertObjectToJsonBytes(c1)))
			         .andExpect(status().isOk())
			         .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			         .andExpect(jsonPath("$.id", is(1)))
			         .andExpect(jsonPath("$.title", is("Nice job")))
			         .andExpect(jsonPath("$").exists());
	  }
	  
	  @Test
	  public void listAllComments() throws IOException, Exception {
		  Comment c1 = getComment(1);
		  Comment c2 = getComment(2);
		  Comment c3 = getComment(3);
		  List<Comment> comments = new ArrayList<Comment>();
		  comments.add(c1);
		  comments.add(c2);
		  comments.add(c3);
		  Mockito.when(commentRepository.findAll()).thenReturn(comments);
		  mvc.perform(get("/findComments/")
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
	  public void getProductFromCommentId() throws URISyntaxException, IOException, Exception {
		  Comment comment = getComment(1);
		  Review r = getReview(2);
		  comment.setReviewId(r.getId());
		  Product product = getProduct(1);
		  r.setProductid(product.getId());
		  Optional<Review> review = Optional.of(r);
		  Optional<Comment> co = Optional.of(comment);
		  Optional<Product> prodOpt = Optional.of(product);
		  Mockito.when(commentRepository.findById(comment.getId())).thenReturn(co);
		  Mockito.when(reviewRepository.findById(r.getId())).thenReturn(review);
		  Mockito.when(productRepository.findById(product.getId())).thenReturn(prodOpt);
		  mvc.perform(get(new URI("/findComments/product/" + product.getId()))
				  .content(json.write(comment).getJson())
				  .contentType(MediaType.APPLICATION_JSON_UTF8)
				  .accept(MediaType.APPLICATION_JSON_UTF8)
				  .content(convertObjectToJsonBytes(product)))
				  .andExpect(jsonPath("$.id", is(1)))
				  .andExpect(jsonPath("$.name", is("TRF")))
		  .andExpect(status().isOk());
	  }
	  
	  @Test
	  public void deleteComment() throws IOException, Exception {
		  Comment c1 = getComment(1);
		  Comment c2 = getComment(2);
		  Comment c3 = getComment(3);
		  List<Comment> comments = new ArrayList<Comment>();
		  comments.add(c1);
		  comments.add(c2);
		  comments.add(c3);
		  Mockito.when(commentRepository.findAll()).thenReturn(comments);
		  mvc.perform(delete("/deleteComment/" + c1.getId())
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
