
    # --- !Ups

    -- Adding services in database
    INSERT INTO service (id, service_type,is_reservable, service_icon) VALUES (NULL, 'Accommodation', 1, 'images/serviceImages/accommodation.png');
    INSERT INTO service (id, service_type,is_reservable, service_icon) VALUES (NULL, 'Arts&Entertainment', 0, 'images/serviceImages/arts.png');
    INSERT INTO service (id, service_type,is_reservable, service_icon) VALUES (NULL, 'Attractions', 0, 'images/serviceImages/attractions.png');
    INSERT INTO service (id, service_type,is_reservable, service_icon) VALUES (NULL, 'Business', 0, 'images/serviceImages/business.png');
    INSERT INTO service (id, service_type,is_reservable, service_icon) VALUES (NULL, 'Coffee', 1, 'images/serviceImages/coffe.png');
    INSERT INTO service (id, service_type,is_reservable, service_icon) VALUES (NULL, 'Food', 1, 'images/serviceImages/food.png');
    INSERT INTO service (id, service_type,is_reservable, service_icon) VALUES (NULL, 'Night life', 1, 'images/serviceImages/nightlife.png');
    INSERT INTO service (id, service_type,is_reservable, service_icon) VALUES (NULL, 'Services', 0, 'images/serviceImages/services.png');
    INSERT INTO service (id, service_type,is_reservable, service_icon) VALUES (NULL, 'Shopping', 0, 'images/serviceImages/shopping.png');
    INSERT INTO service (id, service_type,is_reservable, service_icon) VALUES (NULL, 'Sport', 1, 'images/serviceImages/sport.png');
    INSERT INTO service (id, service_type,is_reservable, service_icon) VALUES (NULL, 'Transportation', 1, 'images/serviceImages/transportation.png');
    INSERT INTO service (id, service_type,is_reservable, service_icon) VALUES (NULL, 'Other', 0, 'images/serviceImages/other.png');

    -- Adding status in database
    INSERT INTO status(id, status) VALUES (3,'Denied');
    INSERT INTO status(id, status) VALUES (1,'Approved');
    INSERT INTO status(id, status) VALUES (2,'Waiting');

    -- Adding Users --
    INSERT INTO user(id, email, first_name, last_name, password, account_created, phone_number, admin, image_id) VALUES(NULL, 'admin@bitcamp.ba', 'Admin', 'BitNavigator', 'admin123', NULL, NULL, 1, NULL );
