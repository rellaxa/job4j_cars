create table cars(
    id        serial primary key,
    engine_id int not null references engines(id)
);
