ALTER TABLE cars ADD COLUMN class_car_id int NOT NULL references classes(id);
ALTER TABLE cars ADD COLUMN body_id int NOT NULL references car_bodies(id);