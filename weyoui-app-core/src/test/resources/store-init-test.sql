set FOREIGN_KEY_CHECKS = 0;

truncate table group_member;
truncate table image;
truncate table order_line;
truncate table product;
truncate table store;
truncate table groups;
truncate table users;
truncate table orders;

set FOREIGN_KEY_CHECKS = 1;

insert into users(user_id,nickname,email) values('user1','임의의 회원1', 'anyuser1@test.com');
insert into users(user_id,nickname, email) values('user2', '임의의 회원2', 'anyuser2@test.com');

insert into store(store_id,store_name,category,state,owner_id,owner_name,zip_code,address1,address2,point)
values('store1','임의의 가게','SERVICE','OPEN','user1','임의의 회원1','123-456','경기도','성남시',ST_GeomFromText('POINT (37.37720712440026 127.11215415764482)'));

insert into product(product_id,product_name,price,product_state,description,store_id)
values('product1','임의의 상품',10000,'FOR_SALE','판매중인 상품입니다.','store1');

insert into image(image_path, image_type, product_id, list_idx) values('external/1','external','product1',0);
insert into image(image_path, image_type, product_id, list_idx) values('internal/1','internal','product1',1);


