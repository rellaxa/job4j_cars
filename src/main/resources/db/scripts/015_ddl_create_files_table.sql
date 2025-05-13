create table files
(
    id      serial primary key,
    name    varchar NOT NULL,
    path    varchar NOT NULL unique,
    post_id int references auto_post(id)
);

