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

insert into users(user_id,nickname,email, user_state, device_id, device_type, phone) values('user1','임의의 회원1', 'anyuser1@test.com', 'ACTIVE', 'device1', 'ios', '01012345678');
insert into users(user_id,nickname, email, user_state, device_id, device_type, phone) values('user2', '임의의 회원2', 'anyuser2@test.com', 'ACTIVE', 'device2', 'android', '01087654321');

insert into groups(group_id, group_name, state, point)
values('group1', '임의의 모임', "END_ACTIVITY", ST_GeomFromText('POINT (37.37720712440026 127.11215415764482)'));

insert into group_member(group_member_id, group_id, user_id, state, role)
values('groupMember1', 'group1', 'user1', 'ACTIVE', 'LEADER');

insert into store(store_id,store_name,category,state,owner_id,owner_name,zip_code,address1,address2,point)
values('store1','임의의 가게','SERVICE','OPEN','user1','임의의 회원1','123-456','경기도','성남시',ST_GeomFromText('POINT (37.37720712440026 127.11215415764482)'));

insert into product(product_id,product_name,price,product_state,description,store_id)
values('product1','임의의 상품',10000,'FOR_SALE','판매중인 상품입니다.','store1');

insert into image(image_path, image_type, product_id, list_idx) values('external/1','external','product1',0);
insert into image(image_path, image_type, product_id, list_idx) values('internal/1','internal','product1',1);


