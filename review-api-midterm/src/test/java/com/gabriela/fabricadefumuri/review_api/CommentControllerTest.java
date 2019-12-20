package com.gabriela.fabricadefumuri.review_api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
import com.gabriela.fabricadefumuri.review_api.crud_repository.CommentRepository;
import com.gabriela.fabricadefumuri.review_api.crud_repository.ReviewRepository;
import com.gabriela.fabricadefumuri.review_api.entity.Comment;
import com.gabriela.fabricadefumuri.review_api.entity.Review;
import com.gabriela.fabricadefumuri.review_api.repository.ReviewMongoRepository;

/**
 * @author Gabriela Spiescu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CommentControllerTest {
	
	  @MockBean
	  private ReviewRepository reviewRepository;
	  
	  @MockBean
	  private ReviewMongoRepository reviewMongoRepository;
	  
	  @MockBean
	  private CommentRepository commentRepository;
	  
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
		  r.setAuthor("Booby");
		  r.setScore(4);
		  r.setTitle("Lovely thing");
		  r.setProductid(1);
		  return r;
	  }
	  
	  @Test
	  public void createCommentForReview() throws URISyntaxException, IOException, Exception {
		  Review r = getReview(2);
		  Optional<Review> review = Optional.of(r);
		  Mockito.when(reviewRepository.findById(r.getId())).thenReturn(review);
		  Mockito.when(reviewMongoRepository.findById(r.getId())).thenReturn(review);
		  Comment comment = getComment(1);
		  mvc.perform(post(new URI("/comments/reviews/" + r.getId()))
				  .content(json.write(comment).getJson())
				  .contentType(MediaType.APPLICATION_JSON_UTF8)
				  .accept(MediaType.APPLICATION_JSON_UTF8))
		  .andExpect(status().isOk());
	  }
	  
	  @Test
	  public void listCommentsForReview() throws IOException, Exception {
		  Review r = getReview(1);
		  Comment c1 = getComment(1);
		  Comment c2 = getComment(2);
		  List<Comment> comments = new ArrayList<Comment>();
		  comments.add(c1);
		  comments.add(c2);
		  r.setComments(comments);
		  Mockito.when(commentRepository.findAllByReviewId(r.getId())).thenReturn(comments);
		  Optional<Review> review = Optional.of(r);
		  Mockito.when(reviewRepository.findById(r.getId())).thenReturn(review);
		  mvc.perform(get("/comments/reviews/" + r.getId()+ "/true")
			    	 .contentType(MediaType.APPLICATION_JSON_UTF8)
			    	 .accept(MediaType.APPLICATION_JSON_UTF8)
			    	 .content(convertObjectToJsonBytes(c1))
			    	 .content(convertObjectToJsonBytes(c2)))
			         .andExpect(status().isOk())
			         .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			         .andExpect(jsonPath("$.[0].id", is(1)))
			         .andExpect(jsonPath("$.[1].id", is(2)))
			         .andExpect(jsonPath("$.[0].isPositiv", is(true)))
			         .andExpect(jsonPath("$", hasSize(2)))
			         .andExpect(jsonPath("$").exists());
		  Comment mongo1 = getComment(4);
		  Comment mongo2 = getComment(5);
		  comments.add(mongo1);
		  comments.add(mongo2);
		  Mockito.when(reviewMongoRepository.findById(r.getId())).thenReturn(review);
		  mvc.perform(get("/comments/reviews/" + r.getId()+ "/false")
			    	 .contentType(MediaType.APPLICATION_JSON_UTF8)
			    	 .accept(MediaType.APPLICATION_JSON_UTF8)
			    	 .content(convertObjectToJsonBytes(c1))
			    	 .content(convertObjectToJsonBytes(c2)))
			         .andExpect(status().isOk())
			         .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			         .andExpect(jsonPath("$.[0].id", is(1)))
			         .andExpect(jsonPath("$.[1].id", is(2)))
			         .andExpect(jsonPath("$.[0].isPositiv", is(true)))
			         .andExpect(jsonPath("$", hasSize(4)))
			         .andExpect(jsonPath("$").exists());
	  }
	  
	    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
	        ObjectMapper mapper = new ObjectMapper();
	        return mapper.writeValueAsBytes(object);
	    }
}
