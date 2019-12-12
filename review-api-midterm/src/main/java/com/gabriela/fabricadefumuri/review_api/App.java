package com.gabriela.fabricadefumuri.review_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication()
public class App {
	
    public static void main( String[] args ) {
    	SpringApplication.run(App.class, args);
    }

}