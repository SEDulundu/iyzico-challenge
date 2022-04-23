CREATE TABLE payment (
  id    BIGINT PRIMARY KEY,
  price DECIMAL(30, 8) NOT NULL
);

CREATE TABLE product (
  id    BIGINT PRIMARY KEY,
  version BIGINT,
  price DECIMAL(30, 8) NOT NULL
  name  VARCHAR(50),
  description VARCHAR(50),
  stock  INT,
);