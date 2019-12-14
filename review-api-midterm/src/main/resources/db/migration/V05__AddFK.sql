ALTER TABLE review 
  	ADD constraint review_product_fk foreign key (productId) references product (id);
ALTER TABLE comment 
	ADD constraint comment_review_fk foreign key (review_id) references review (id);
