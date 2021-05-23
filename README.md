# Spring Boot JWT Authentication example with Spring Security & Spring Data JPA

# App Properties

```
## Run Spring Boot application
```
mvn spring-boot:run
```

## Run following SQL insert statements
```
INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
```

For ubuntu
install:
sudo apt install default-jdk

direct port to 8080:
sudo iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-ports 8080
mvn spring-boot:run

open port for 8000:
sudo ufw allow 8000

open port for 8001: