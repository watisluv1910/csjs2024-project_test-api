CREATE DOMAIN hour_of_day AS INTEGER
    CHECK (VALUE >= 0 AND VALUE <= 23);

CREATE TABLE "polling_station"
(
    "id"             SERIAL      NOT NULL,
    "ps_number"      INTEGER     NOT NULL,
    "hotline"        VARCHAR(15) NOT NULL,
    "opens_at_hour"  hour_of_day NOT NULL,
    "closes_at_hour" hour_of_day NOT NULL
);
ALTER TABLE "polling_station"
    ADD PRIMARY KEY ("id");
CREATE INDEX "polling_station_ps_number_index" ON
    "polling_station" ("ps_number");


CREATE TABLE "attendance"
(
    "id"                 SERIAL      NOT NULL,
    "polling_station_id" INTEGER     NOT NULL,
    "date"               DATE        NOT NULL,
    "hour"               hour_of_day NOT NULL,
    "attendance"         BIGINT      NOT NULL
);
ALTER TABLE "attendance"
    ADD CONSTRAINT "attendance_polling_station_id_date_hour_unique"
        UNIQUE ("polling_station_id", "date", "hour");
ALTER TABLE "attendance"
    ADD PRIMARY KEY ("id");

CREATE TABLE "address"
(
    "id"           SERIAL       NOT NULL,
    "region_code"  VARCHAR(3)   NOT NULL,
    "city"         VARCHAR(128) NOT NULL,
    "street"       VARCHAR(64)  NOT NULL,
    "house_number" VARCHAR(10)  NOT NULL,
    "building"     VARCHAR(2)   NULL
);
ALTER TABLE "address"
    ADD PRIMARY KEY ("id");
ALTER TABLE "address"
    ADD CONSTRAINT address_fields_unique
        UNIQUE NULLS NOT DISTINCT (
    "region_code",
    "city",
    "street",
    "house_number",
    "building"
    );
CREATE INDEX "address_region_code_index" ON "address" ("region_code");

CREATE TYPE address_type AS ENUM (
    'ADDRESS_TYPE_BORDER',
    'ADDRESS_TYPE_ACTUAL',
    'ADDRESS_TYPE_COMMISSION');

CREATE TABLE "station_addresses"
(
    "id"                 SERIAL       NOT NULL,
    "polling_station_id" INTEGER      NOT NULL,
    "address_id"         INTEGER      NOT NULL,
    "address_type"       address_type NOT NULL
);
ALTER TABLE "station_addresses"
    ADD CONSTRAINT "station_addresses_fields_unique"
        UNIQUE (
                "polling_station_id",
                "address_id",
                "address_type"
            );
ALTER TABLE "station_addresses"
    ADD PRIMARY KEY ("id");
COMMENT
ON COLUMN
    "station_addresses"."address_type" IS
    'Represents an address type. '
        'BORDER address is a part of an election border for the specified polling station. '
        'ACTUAL address is an address where electors should go to vote. '
        'COMMISSION address is an address of an election commission for the specified polling station.';


ALTER TABLE "attendance"
    ADD CONSTRAINT "attendance_polling_station_id_foreign"
        FOREIGN KEY ("polling_station_id") REFERENCES "polling_station" ("id");
ALTER TABLE "station_addresses"
    ADD CONSTRAINT "station_addresses_polling_station_id_foreign"
        FOREIGN KEY ("polling_station_id") REFERENCES "polling_station" ("id");
ALTER TABLE "station_addresses"
    ADD CONSTRAINT "station_addresses_address_id_foreign"
        FOREIGN KEY ("address_id") REFERENCES "address" ("id");

-- Test border addresses for Moscow, East Izmaylovo (stations 918 to 927)
INSERT INTO address (region_code, city, street, house_number, building)
VALUES ('MOW', 'Москва', 'ул. 16-я Парковая', '16', '1'),
       ('MOW', 'Москва', 'ул. 16-я Парковая', '16', '2'),
       ('MOW', 'Москва', 'ул. 16-я Парковая', '16', '3'),
       ('MOW', 'Москва', 'ул. 16-я Парковая', '16', NULL), -- Station 919
       ('MOW', 'Москва', 'ул. 16-я Парковая', '18', NULL),
       ('MOW', 'Москва', 'Сиреневый бул.', '52', NULL),
       ('MOW', 'Москва', 'Сиреневый бул.', '54', NULL),
       ('MOW', 'Москва', 'Сиреневый бул.', '56', NULL),
       ('MOW', 'Москва', 'Сиреневый бул.', '58', NULL),
       ('MOW', 'Москва', 'Сиреневый бул.', '60', NULL),
       ('MOW', 'Москва', 'Сиреневый бул.', '62', NULL),
       ('MOW', 'Москва', 'Сиреневый бул.', '62', '1'),
       ('MOW', 'Москва', 'Сиреневый бул.', '64/31', NULL),
       ('MOW', 'Москва', 'ул. 16-я Парковая', '10', NULL),
       ('MOW', 'Москва', 'ул. 16-я Парковая', '14', NULL),
       ('MOW', 'Москва', 'ул. 16-я Парковая', '21', '2'),
       ('MOW', 'Москва', 'ул. 16-я Парковая', '23', NULL),
       ('MOW', 'Москва', 'ул. 16-я Парковая', '25', '1'),
       ('MOW', 'Москва', 'ул. 16-я Парковая', '25', '2'),
       ('MOW', 'Москва', 'ул. 16-я Парковая', '27', NULL),
       ('MOW', 'Москва', 'ул. 15-я Парковая', '26', '1'),
       ('MOW', 'Москва', 'ул. 15-я Парковая', '26', '2'),
       ('MOW', 'Москва', 'ул. 15-я Парковая', '26', '3'),
       ('MOW', 'Москва', 'ул. 15-я Парковая', '26', '4'),
       ('MOW', 'Москва', 'ул. 15-я Парковая', '28', NULL),
       ('MOW', 'Москва', 'ул. 15-я Парковая', '24', '1'),
       ('MOW', 'Москва', 'ул. 15-я Парковая', '24', '2'),
       ('MOW', 'Москва', 'Измайловский бул.', '63/12', '1'),
       ('MOW', 'Москва', 'Измайловский бул.', '63/12', '2'),
       ('MOW', 'Москва', 'Измайловский бул.', '63/12', '3'),
       ('MOW', 'Москва', 'Измайловский бул.', '67', '1'),
       ('MOW', 'Москва', 'Измайловский бул.', '67', '2'),
       ('MOW', 'Москва', 'Измайловский бул.', '71/25', '1'),
       ('MOW', 'Москва', 'Измайловский бул.', '71/25', '2'),
       ('MOW', 'Москва', 'Измайловский бул.', '71/25', '3'),
       ('MOW', 'Москва', 'ул. 13-я Парковая', '20', '4'),
       ('MOW', 'Москва', 'ул. 13-я Парковая', '22', '4'),
       ('MOW', 'Москва', 'ул. 11-я Парковая', '34', NULL),
       ('MOW', 'Москва', 'ул. 11-я Парковая', '36', NULL),
       ('MOW', 'Москва', 'ул. 11-я Парковая', '38', NULL),
       ('MOW', 'Москва', 'ул. 13-я Парковая', '14', NULL),
       ('MOW', 'Москва', 'ул. 13-я Парковая', '16', '1'),
       ('MOW', 'Москва', 'ул. 13-я Парковая', '16', '2'),
       ('MOW', 'Москва', 'ул. 13-я Парковая', '16', '3'),
       ('MOW', 'Москва', 'ул. 13-я Парковая', '16', '4'),
       ('MOW', 'Москва', 'ул. 13-я Парковая', '16', '5'),
       ('MOW', 'Москва', 'ул. 13-я Парковая', '17', NULL),
       ('MOW', 'Москва', 'ул. 13-я Парковая', '19', NULL),
       ('MOW', 'Москва', 'ул. 13-я Парковая', '20', '1'),
       ('MOW', 'Москва', 'ул. 13-я Парковая', '20', '2'),
       ('MOW', 'Москва', 'ул. 13-я Парковая', '20', '3');

-- Test actual (and commission) addresses for Moscow, East Izmaylovo (stations 918 to 927)
INSERT INTO address (region_code, city, street, house_number, building)
VALUES ('MOW', 'Москва', 'Сиреневый бул.', '68', NULL),     -- Station 918, 921
       ('MOW', 'Москва', 'Сиреневый бул.', '58а', NULL),    -- Station 920, 922, 923
       ('MOW', 'Москва', 'ул. 13-я Парковая', '18а', NULL), -- Station 924, 925, 927
       ('MOW', 'Москва', 'ул. 13-я Парковая', '21А', NULL); -- Station 926

-- Test stations for Moscow, East Izmaylovo (stations 918 to 927)
INSERT INTO polling_station (ps_number, hotline, opens_at_hour, closes_at_hour)
VALUES (918, '8-499-463-68-23', 9, 18),
       (919, '8-499-464-33-90', 8, 17),
       (920, '8-499-463-40-95', 9, 20),
       (921, '8-495-603-61-30', 9, 19),
       (922, '8-499-463-91-25', 7, 16),
       (923, '8-499-463-70-92', 12, 20),
       (924, '8-499-463-49-05', 9, 18),
       (925, '8-499-463-78-35', 8, 19),
       (926, '8-499-463-02-54', 10, 20),
       (927, '8-499-463-66-07', 8, 18);

-- Test station-address border relations for Moscow, East Izmaylovo (stations 918 to 927)
INSERT INTO station_addresses (polling_station_id, address_id, address_type)
VALUES
    -- Station 918
    (1, 1, 'ADDRESS_TYPE_BORDER'),
    (1, 2, 'ADDRESS_TYPE_BORDER'),
    (1, 3, 'ADDRESS_TYPE_BORDER'),
    (1, 5, 'ADDRESS_TYPE_BORDER'),
    -- Station 919
    (2, 4, 'ADDRESS_TYPE_BORDER'),
    -- Station 920
    (3, 6, 'ADDRESS_TYPE_BORDER'),
    (3, 7, 'ADDRESS_TYPE_BORDER'),
    (3, 8, 'ADDRESS_TYPE_BORDER'),
    (3, 9, 'ADDRESS_TYPE_BORDER'),
    (3, 10, 'ADDRESS_TYPE_BORDER'),
    (3, 11, 'ADDRESS_TYPE_BORDER'),
    (3, 12, 'ADDRESS_TYPE_BORDER'),
    (3, 13, 'ADDRESS_TYPE_BORDER'),
    -- Station 921
    (4, 14, 'ADDRESS_TYPE_BORDER'),
    (4, 17, 'ADDRESS_TYPE_BORDER'),
    (4, 18, 'ADDRESS_TYPE_BORDER'),
    (4, 19, 'ADDRESS_TYPE_BORDER'),
    (4, 20, 'ADDRESS_TYPE_BORDER'),
    -- Station 922
    (5, 21, 'ADDRESS_TYPE_BORDER'),
    (5, 22, 'ADDRESS_TYPE_BORDER'),
    (5, 23, 'ADDRESS_TYPE_BORDER'),
    (5, 24, 'ADDRESS_TYPE_BORDER'),
    (5, 25, 'ADDRESS_TYPE_BORDER'),
    -- Station 923
    (6, 26, 'ADDRESS_TYPE_BORDER'),
    (6, 27, 'ADDRESS_TYPE_BORDER'),
    -- Station 924
    (7, 28, 'ADDRESS_TYPE_BORDER'),
    (7, 29, 'ADDRESS_TYPE_BORDER'),
    (7, 30, 'ADDRESS_TYPE_BORDER'),
    (7, 31, 'ADDRESS_TYPE_BORDER'),
    (7, 32, 'ADDRESS_TYPE_BORDER'),
    (7, 33, 'ADDRESS_TYPE_BORDER'),
    (7, 34, 'ADDRESS_TYPE_BORDER'),
    (7, 35, 'ADDRESS_TYPE_BORDER'),
    -- Station 925
    (8, 36, 'ADDRESS_TYPE_BORDER'),
    (8, 37, 'ADDRESS_TYPE_BORDER'),
    -- Station 926
    (9, 38, 'ADDRESS_TYPE_BORDER'),
    (9, 39, 'ADDRESS_TYPE_BORDER'),
    (9, 40, 'ADDRESS_TYPE_BORDER'),
    -- Station 927
    (10, 41, 'ADDRESS_TYPE_BORDER'),
    (10, 42, 'ADDRESS_TYPE_BORDER'),
    (10, 43, 'ADDRESS_TYPE_BORDER'),
    (10, 44, 'ADDRESS_TYPE_BORDER'),
    (10, 45, 'ADDRESS_TYPE_BORDER'),
    (10, 46, 'ADDRESS_TYPE_BORDER'),
    (10, 47, 'ADDRESS_TYPE_BORDER'),
    (10, 48, 'ADDRESS_TYPE_BORDER'),
    (10, 49, 'ADDRESS_TYPE_BORDER'),
    (10, 50, 'ADDRESS_TYPE_BORDER'),
    (10, 51, 'ADDRESS_TYPE_BORDER');

-- Test station-address actual relations for Moscow, East Izmaylovo (stations 918 to 927)
INSERT INTO station_addresses (polling_station_id, address_id, address_type)
VALUES (1, 52, 'ADDRESS_TYPE_ACTUAL'),
       (2, 4, 'ADDRESS_TYPE_ACTUAL'),
       (3, 53, 'ADDRESS_TYPE_ACTUAL'),
       (4, 52, 'ADDRESS_TYPE_ACTUAL'),
       (5, 53, 'ADDRESS_TYPE_ACTUAL'),
       (6, 53, 'ADDRESS_TYPE_ACTUAL'),
       (7, 54, 'ADDRESS_TYPE_ACTUAL'),
       (8, 54, 'ADDRESS_TYPE_ACTUAL'),
       (9, 55, 'ADDRESS_TYPE_ACTUAL'),
       (10, 54, 'ADDRESS_TYPE_ACTUAL');

-- Test station-address commission relations for Moscow, East Izmaylovo (stations 918 to 927)
INSERT INTO station_addresses (polling_station_id, address_id, address_type)
VALUES (1, 52, 'ADDRESS_TYPE_COMMISSION'),
       (2, 4, 'ADDRESS_TYPE_COMMISSION'),
       (3, 53, 'ADDRESS_TYPE_COMMISSION'),
       (4, 52, 'ADDRESS_TYPE_COMMISSION'),
       (5, 53, 'ADDRESS_TYPE_COMMISSION'),
       (6, 53, 'ADDRESS_TYPE_COMMISSION'),
       (7, 54, 'ADDRESS_TYPE_COMMISSION'),
       (8, 54, 'ADDRESS_TYPE_COMMISSION'),
       (9, 55, 'ADDRESS_TYPE_COMMISSION'),
       (10, 54, 'ADDRESS_TYPE_COMMISSION');
