CREATE TABLE comment (
  id INT NOT NULL AUTO_INCREMENT,
  body VARCHAR(300) NOT NULL,
  is_positiv BOOLEAN,
  title VARCHAR(300) NOT NULL,
  review_id INT NOT NULL,
  constraint comment_pk primary key (id));
  