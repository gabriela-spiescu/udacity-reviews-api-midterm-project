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
public class Product2ControllerTest {
	
	  @MockBean
	  private ReviewMongoRepository reviewRepository;
	  
	  @MockBean
	  private CommentMongoRepository commentRepository;
	  
	  @MockBean
	  private ProductMongoRepository productRepository;
	  
	  @Autowired
	  private MockMvc mvc;
	    
	  @Autowired
	  private JacksonTester<Product> json;

	  
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
	  public void createProduct() throws URISyntaxException, IOException, Exception {
		  Product p = getProduct(1);
		  Optional<Product> proOpt = Optional.of(p);
		  Mockito.when(productRepository.findById(p.getId())).thenReturn(proOpt);
		  mvc.perform(post(new URI("/addProduct"))
				  .content(json.write(p).getJson())
				  .contentType(MediaType.APPLICATION_JSON_UTF8)
				  .accept(MediaType.APPLICATION_JSON_UTF8))
		  .andExpect(status().isOk());
	  }
	  
	  @Test
	  public void listProductById() throws IOException, Exception {
		  Product p = getProduct(1);
		  Optional<Product> proOpt = Optional.of(p);
		  Mockito.when(productRepository.findById(p.getId())).thenReturn(proOpt);
		  mvc.perform(get("/findProducts/" + p.getId())
			    	 .contentType(MediaType.APPLICATION_JSON_UTF8)
			    	 .accept(MediaType.APPLICATION_JSON_UTF8)
			    	 .content(convertObjectToJsonBytes(p)))
			         .andExpect(status().isOk())
			         .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			         .andExpect(jsonPath("$.id", is(1)))
			         .andExpect(jsonPath("$.name", is("TRF")))
			         .andExpect(jsonPath("$").exists());
	  }
	  
	  @Test
	  public void listAllProducts() throws IOException, Exception {
		  Product c1 = getProduct(1);
		  Product c2 = getProduct(2);
		  Product c3 = getProduct(3);
		  List<Product> products = new ArrayList<Product>();
		  products.add(c1);
		  products.add(c2);
		  products.add(c3);
		  Mockito.when(productRepository.findAll()).thenReturn(products);
		  mvc.perform(get("/findProducts/")
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
	  public void getReviewsFromProduct() throws URISyntaxException, IOException, Exception {
		  Review r = getReview(1);
		  Product product = getProduct(1);
		  r.setProductid(product.getId());
		  List<Review> reviews = new ArrayList<Review>();
		  reviews.add(r);
		  product.setReviews(reviews);
		  Optional<Product> prodOpt = Optional.of(product);
		  Mockito.when(productRepository.findById(product.getId())).thenReturn(prodOpt);
		  mvc.perform(get(new URI("/findProducts/reviews/" + product.getId()))
				  .content(json.write(product).getJson())
				  .contentType(MediaType.APPLICATION_JSON_UTF8)
				  .accept(MediaType.APPLICATION_JSON_UTF8)
				  .content(convertObjectToJsonBytes(reviews)))
				  .andExpect(jsonPath("$.[0].id", is(1)))
				  .andExpect(jsonPath("$.[0].title", is("Lovely thing")))
		  .andExpect(status().isOk());
	  }
	  
	  @Test
	  public void deleteProduct() throws IOException, Exception {
		  Product c1 = getProduct(1);
		  Product c2 = getProduct(2);
		  Product c3 = getProduct(3);
		  List<Product> products = new ArrayList<Product>();
		  products.add(c1);
		  products.add(c2);
		  products.add(c3);
		  Mockito.when(productRepository.findAll()).thenReturn(products);
		  mvc.perform(delete("/deleteProduct/" + c1.getId())
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
