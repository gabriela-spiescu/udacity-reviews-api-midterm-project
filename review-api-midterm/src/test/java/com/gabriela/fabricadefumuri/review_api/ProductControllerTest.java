package com.gabriela.fabricadefumuri.review_api;

import static org.assertj.core.api.Assertions.assertThat;
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

import javax.persistence.EntityManager;
import javax.sql.DataSource;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriela.fabricadefumuri.review_api.crud_repository.ProductRepository;
import com.gabriela.fabricadefumuri.review_api.entity.Product;

/**
 * @author Gabriela Spiescu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ProductControllerTest {
	
	  @Autowired private DataSource dataSource;
	  @Autowired private JdbcTemplate jdbcTemplate;
	  @Autowired private EntityManager entityManager;

	  @MockBean
	  private ProductRepository productRepository;
	  
	  @Autowired
	  private MockMvc mvc;
	    
	  @Autowired
	  private JacksonTester<Product> json;
	  
	  public Product getProduct() {
		  Product p = new Product();
		  p.setName("TRF");
		  p.setModel("NICE");
		  p.setManufacturer("MYNE");
		  p.setPrice(123.6);
		  p.setAvailability(false);
		  p.setDescription("Nice photos");
		  p.setReviews(null);
		  return p;
	  }
	  
	  public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
	      ObjectMapper mapper = new ObjectMapper();
	      return mapper.writeValueAsBytes(object);
	  }
	  
	  @Test
	  public void injectedComponentsAreNotNull(){
	    assertThat(dataSource).isNotNull();
	    assertThat(jdbcTemplate).isNotNull();
	    assertThat(entityManager).isNotNull();
	    assertThat(productRepository).isNotNull();
	  }
	  
	  @Test
	  public void createProduct() throws URISyntaxException, IOException, Exception {
		  Product product = getProduct();
		  mvc.perform(post(new URI("/products/"))
				  .content(json.write(product).getJson())
				  .contentType(MediaType.APPLICATION_JSON_UTF8)
				  .accept(MediaType.APPLICATION_JSON_UTF8))
		  .andExpect(status().isCreated());
	  }
	  
	  @Test
	  public void findById() throws URISyntaxException, IOException, Exception {
		  Product p1 = getProduct();
		  p1.setId(1);
		  Optional<Product> opt = Optional.of(p1);
		  Mockito.when(productRepository.findById(opt.get().getId())).thenReturn(opt);
		  
		  mvc.perform(get(new URI("/products/" + p1.getId()))
				  .content(convertObjectToJsonBytes(p1))
				  .contentType(MediaType.APPLICATION_JSON_UTF8)
				  .accept(MediaType.APPLICATION_JSON_UTF8))
		  .andExpect(status().isOk())
		  .andExpect(jsonPath("$.id", is(1)))
	      .andExpect(jsonPath("$.name", is("TRF")))
	      .andExpect(jsonPath("$.model", is("NICE")))
	      .andExpect(jsonPath("$.price").exists());
		  
	  }
	  
	  @Test
	  public void listProducts() throws Exception {
		  List<Product> products = new ArrayList<Product>();
		  Product p1 = getProduct();
		  p1.setId(1);
		  products.add(p1);
		  Product p2 = getProduct();
		  p2.setId(2);
		  products.add(p2);
		  Mockito.when(productRepository.findAll()).thenReturn(products);
		  mvc.perform(get("/products/")
			    	 .contentType(MediaType.APPLICATION_JSON_UTF8)
			    	 .accept(MediaType.APPLICATION_JSON_UTF8))
			         .andExpect(status().isOk())
			         .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			         .andExpect(jsonPath("$.[0].id", is(1)))
			         .andExpect(jsonPath("$.[1].id", is(2)))
			         .andExpect(jsonPath("$.[0].name", is("TRF")))
			         .andExpect(jsonPath("$", hasSize(2)))
			         .andExpect(jsonPath("$").exists());
	  }
}
