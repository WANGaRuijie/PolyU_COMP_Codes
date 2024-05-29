CREATE TABLE ADMIN (
    PHONE_NUMBER VARCHAR(20),
    USER_NAME VARCHAR(20),
    PASSWORD VARCHAR(20),
    PERSONAL_INFORMATION VARCHAR(200),
    CONSTRAINT ADMIN PRIMARY KEY(PHONE_NUMBER)
);

CREATE TABLE CUSTOMER (
    PHONE_NUMBER VARCHAR(20),
    USER_NAME VARCHAR(20),
    PASSWORD VARCHAR(20),
    PERSONAL_INFORMATION VARCHAR(200),
    BALANCE FLOAT,
    SHIPPING_ADDRESS_A VARCHAR(100),
    SHIPPING_ADDRESS_B VARCHAR(100),
    SHIPPING_ADDRESS_C VARCHAR(100),
    CONSTRAINT CUSTOMER PRIMARY KEY(PHONE_NUMBER)
);

CREATE TABLE PRODUCT (
    PRODUCT_ID VARCHAR(20),
    PRODUCT_NAME VARCHAR(20),
    DESCRIPTION VARCHAR(200),
    SPECIFICATION VARCHAR(200),
    PRICE FLOAT,
    AVAILABLE_QUANTITY INTEGER,
    CATEGORY VARCHAR(20),
    CONSTRAINT PRODUCT PRIMARY KEY(PRODUCT_ID)
);

CREATE TABLE SHOPPING_CART (
    CUSTOMER_PHONE_NUMBER VARCHAR(20),
    PRODUCT_ID VARCHAR(20),
    PRODUCT_NAME VARCHAR(20),
    QUANTITY INTEGER,
    SINGLE_PRICE FLOAT,
    TOTAL_PRICE FLOAT
);

CREATE TABLE ORDER_ (
    ORDER_ID         VARCHAR(20),
    CUSTOMER_PHONE_NUMBER          VARCHAR(20),
    PRODUCT_ID       VARCHAR(20),
    QUANTITY         INTEGER,
    COST             FLOAT,
    SHIPPING_ADDRESS VARCHAR(100),
    CONSTRAINT ORDER_ PRIMARY KEY(ORDER_ID)
);

CREATE TABLE REVIEW (
    ORDER_ID VARCHAR(20),
    CUSTOMER_PHONE_NUMBER VARCHAR(20),
    PRODUCT_ID VARCHAR(20),
    CONTENT  VARCHAR(200),
    CONSTRAINT REVIEW PRIMARY KEY(ORDER_ID)
);

INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, SPECIFICATION, PRICE, AVAILABLE_QUANTITY,CATEGORY) VALUES ('1', 'Pen', 'A pen', '10cm*0.5cm*0.5cm', 10, 100, "Stationary");
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, SPECIFICATION, PRICE, AVAILABLE_QUANTITY,CATEGORY) VALUES ('2', 'Nuggets', 'Delicious but unhealthy', '1cm*1cm*0.5cm', 24, 100, "Food");
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, SPECIFICATION, PRICE, AVAILABLE_QUANTITY,CATEGORY) VALUES ('3', 'Oxford Dictionary', 'Full of English words', '60cm*60cm*10cm', 60, 100, "Book");

INSERT INTO ADMIN (PHONE_NUMBER, USER_NAME, PASSWORD, PERSONAL_INFORMATION) VALUES ("1234", "Tony", "1234", "nothing here");
INSERT INTO ADMIN (PHONE_NUMBER, USER_NAME, PASSWORD, PERSONAL_INFORMATION) VALUES ("5678", "Terry", "5678", "nothing here");
INSERT INTO ADMIN (PHONE_NUMBER, USER_NAME, PASSWORD, PERSONAL_INFORMATION) VALUES ("4321", "Terrence", "4321", "nothing here");

INSERT INTO CUSTOMER (PHONE_NUMBER, USER_NAME, PASSWORD, PERSONAL_INFORMATION, BALANCE, SHIPPING_ADDRESS_A, SHIPPING_ADDRESS_B, SHIPPING_ADDRESS_C) VALUES ("1", "Jeffrey", "1", "nothing here", 0, null, null, null);
INSERT INTO CUSTOMER (PHONE_NUMBER, USER_NAME, PASSWORD, PERSONAL_INFORMATION, BALANCE, SHIPPING_ADDRESS_A, SHIPPING_ADDRESS_B, SHIPPING_ADDRESS_C) VALUES ("2", "Jackson", "2", "nothing here", 0, null, null, null);
INSERT INTO CUSTOMER (PHONE_NUMBER, USER_NAME, PASSWORD, PERSONAL_INFORMATION, BALANCE, SHIPPING_ADDRESS_A, SHIPPING_ADDRESS_B, SHIPPING_ADDRESS_C) VALUES ("3", "Jimmy", "3", "nothing here", 0, null, null, null);

INSERT INTO SHOPPING_CART (CUSTOMER_PHONE_NUMBER, PRODUCT_ID, PRODUCT_NAME, QUANTITY, SINGLE_PRICE, TOTAL_PRICE) VALUES ("1", "1", "Pen", 1, 10, 10);
INSERT INTO SHOPPING_CART (CUSTOMER_PHONE_NUMBER, PRODUCT_ID, PRODUCT_NAME, QUANTITY, SINGLE_PRICE, TOTAL_PRICE) VALUES ("2", "2", "Nuggets", 1, 24, 24);
INSERT INTO SHOPPING_CART (CUSTOMER_PHONE_NUMBER, PRODUCT_ID, PRODUCT_NAME, QUANTITY, SINGLE_PRICE, TOTAL_PRICE) VALUES ("3", "3", "Oxford Dictionary", 1, 60, 60);
