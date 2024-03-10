create table if not exists product
(
    id                 uuid not null primary key,
    article            text unique,
    name               text,
    description        text,
    category           text,
    price              double precision,
    count              int,
    creation_date      date,
    last_modified_date timestamp
);
