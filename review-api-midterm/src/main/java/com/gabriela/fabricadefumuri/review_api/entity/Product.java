package com.gabriela.fabricadefumuri.review_api.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Gabriela Spiescu
 */
@Getter
@Setter
@ToString

@Entity
@Table
@Document(collection = "Product")
public class Product {
	
	@Id
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	private String model;
	private String description;
	private String manufacturer;
	private Double price;
	private Boolean availability;
	
	@Transient
	@DBRef
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//	@JsonIgnore
	private List<Review> reviews = new ArrayList<Review>();

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}
	public Product() {
	}
	/**
	 * @param productid
	 */
	public Product(Integer productid) {
		this.id = productid;
	}
}
