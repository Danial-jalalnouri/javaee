CREATE TABLE customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    type VARCHAR(10)
);

CREATE TABLE real_customer (
    id BIGINT PRIMARY KEY,
    family VARCHAR(255) NOT NULL,
    FOREIGN KEY (id) REFERENCES customer(id) ON DELETE CASCADE
);

CREATE TABLE legal_customer (
    id BIGINT PRIMARY KEY,
    fax VARCHAR(20),
    FOREIGN KEY (id) REFERENCES customer(id) ON DELETE CASCADE
);