CREATE TABLE product (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(300) NOT NULL,
  model VARCHAR(300) NOT NULL,
  description VARCHAR(600) NOT NULL,
  manufacturer VARCHAR(300) NOT NULL,
  price DOUBLE NOT NULL,
  availability BOOLEAN NOT NULL,
  constraint product_pk primary key (id));