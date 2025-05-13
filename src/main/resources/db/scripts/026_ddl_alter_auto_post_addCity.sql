ALTER TABLE auto_post ADD COLUMN city_id int NOT NULL references cities(id);
