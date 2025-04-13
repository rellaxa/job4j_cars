ALTER TABLE cars ADD COLUMN brand_id int NOT NULL references car_brands(id);
ALTER TABLE owners ADD COLUMN user_id int NOT NULL references auto_user(id);
