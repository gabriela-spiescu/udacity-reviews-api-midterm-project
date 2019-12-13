package com.gabriela.fabricadefumuri.review_api.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import org.springframework.data.annotation.Id;
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
@Document
public class Comment {
	
	@Id
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String body;
	
	private String title;
	
	private Boolean isPositiv;

	private int reviewId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getIsPositiv() {
		return isPositiv;
	}

	public void setIsPositiv(Boolean isPositiv) {
		this.isPositiv = isPositiv;
	}


}
