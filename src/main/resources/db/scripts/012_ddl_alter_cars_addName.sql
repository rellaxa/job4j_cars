ALTER TABLE cars ADD COLUMN car_name varchar;
ALTER TABLE engines ADD COLUMN engine_name varchar;
ALTER TABLE owners ADD COLUMN owner_name varchar;
ALTER TABLE owners ADD COLUMN user_id int references auto_user(id);