CREATE DATABASE IF NOT EXISTS blog;

USE blog;

CREATE TABLE IF NOT EXISTS post (
  id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  body MEDIUMTEXT NOT NULL,
  dateAndTime DATETIME NOT NULL DEFAULT current_timestamp,
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS comment (
  id INT,
  post_id INT,
  email VARCHAR(255) NOT NULL,
  body TEXT NOT NULL,
  dateAndTime DATETIME NOT NULL DEFAULT current_timestamp,
  FOREIGN KEY (post_id) REFERENCES post(id),
  PRIMARY KEY (post_id, id)
);

CREATE TABLE IF NOT EXISTS admin (
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  PRIMARY KEY(id)
);


/* Default username/password is "admin", "testpwd". Please change as soon as deployed. */
INSERT IGNORE INTO admin (id, username, password) VALUE (1, "admin", "eom48lPu43pjYSGzX3VE9c5IcEFhKttV4oFgyD6CwlU=");