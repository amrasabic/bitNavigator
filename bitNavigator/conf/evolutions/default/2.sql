
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
     INSERT INTO user(id, email, first_name, last_name, password, account_created, admin, validated) VALUES(NULL, 'admin@bitcamp.ba', 'Admin', 'BitNavigator', '1000:8a87a9cf2b595cb5482d850e7ab218279e475b1aacf43ceb:2a51ccf6f7cd5f3644eaee2785baaa6a7fb09430c2a32b89', NULL, 1, 1);
     INSERT INTO user(id, email, first_name, last_name, password, account_created, admin, validated) VALUES(NULL, 'semir.sahman@bitcamp.ba', 'Semir', 'Sahman', '1000:a3ddce2002f3e150ab58e1b9a6881505835afacacec622c1:3a18ccfe1073301b44b5be6b8aeb08a41b3b0c90233a786d', NULL, 0, 1);
     INSERT INTO user(id, email, first_name, last_name, password, account_created, admin, validated) VALUES(NULL, 'amra.sabic@bitcamp.ba', 'Amra', 'Sabic', '1000:5215a18d5ff013d7d5ea3b77c10000fe4511d1a6d01bfbc4:946b596635e83bc9484c53810ce87f6ff4a9c6aadca9adb2', NULL, 0, 1);
     INSERT INTO user(id, email, first_name, last_name, password, account_created, admin, validated) VALUES(NULL, 'ognjen.cetkovic@bitcamp.ba', 'Ognjen', 'Cetkovic', '1000:2e93de0d798a1a61c5681676c25e6e9af675d8810d9840bc:2797b698cd16bfa0ac28c3e24d4f9c34e495ba54d1e0b9f7', NULL, 0, 1);
     INSERT INTO user(id, email, first_name, last_name, password, account_created, admin, validated) VALUES(NULL, 'tomislav.trifunovic@bitcamp.ba', 'Tomislav', 'Trifunovic', '1000:2ba3a4cdccc498955f1fa05adfedc3f11eeb4636b4e219aa:f80ea28eb06b419884a8386c73d5002a1038b86f74fa6fab', NULL, 0, 1);
     INSERT INTO user(id, email, first_name, last_name, password, account_created, admin, validated) VALUES(NULL, 'kristina.pupavac@bitcamp.ba', 'Kristina', 'Pupavac', '1000:d6e2b719b2d87b8ef99f9602b66e8f41480fe3568cacdda5:b11c8d2c13fd997207da810b8b8327dd81e99b9ad5964a7b', NULL, 0, 1);