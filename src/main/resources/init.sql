CREATE TABLE airport.role (
	id INT PRIMARY KEY AUTO_INCREMENT
);

CREATE TABLE airport.user (
	id INT PRIMARY KEY AUTO_INCREMENT,
	id_role INT,
    CONSTRAINT fk_role FOREIGN KEY (id_role) REFERENCES airport.role(id)
);

CREATE TABLE airport.order (
	id INT PRIMARY KEY AUTO_INCREMENT,
	number INT,
    order_date varchar(256) NOT NULL,
    id_user INT,
    CONSTRAINT fk_user FOREIGN KEY (id_user) REFERENCES airport.user(id)
);

CREATE TABLE airport.aircompany (
	id INT PRIMARY KEY AUTO_INCREMENT
);

CREATE TABLE airport.airplane (
	id INT PRIMARY KEY AUTO_INCREMENT,
    id_aircompany INT,
    CONSTRAINT fk_aircompany FOREIGN KEY (id_aircompany) REFERENCES airport.aircompany(id)
);

CREATE TABLE airport.route (
	id INT PRIMARY KEY AUTO_INCREMENT,
	id_departure INT,
    id_arrival INT
);

CREATE TABLE airport.airplane_route (
	id_airplane INT,
    id_route INT,
    CONSTRAINT fk_airplane FOREIGN KEY (id_airplane) REFERENCES airport.airplane(id),
    CONSTRAINT fk_route FOREIGN KEY (id_route) REFERENCES airport.route(id)
);

CREATE TABLE airport.ticket (
    id INT PRIMARY KEY AUTO_INCREMENT,
	id_route INT,
    id_order INT UNIQUE,
    passport_data varchar(256) NOT NULL,
    CONSTRAINT fk_route_2 FOREIGN KEY (id_route) REFERENCES airport.route(id),
    CONSTRAINT fk_order FOREIGN KEY (id_order) REFERENCES airport.order(id)
);
