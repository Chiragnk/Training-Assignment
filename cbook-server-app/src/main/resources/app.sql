create table if not exists contact
(
id uuid primary key,
firstName varchar(255),
lastName varchar(255),
email varchar(255),
phone varchar(10),
deleted boolean default false
)