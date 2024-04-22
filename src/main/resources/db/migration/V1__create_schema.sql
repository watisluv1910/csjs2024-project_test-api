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
