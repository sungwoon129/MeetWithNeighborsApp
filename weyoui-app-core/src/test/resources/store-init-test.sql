set FOREIGN_KEY_CHECKS = 0;

truncate table group_member;
truncate table image;
truncate table order_line;
truncate table product;
truncate table store;
truncate table gathering;
truncate table users;
truncate table orders;

set FOREIGN_KEY_CHECKS = 1;

insert into users(user_id,nickname,email, user_state, device_id, device_type, phone) values('user1','임의의 회원1', 'anyuser1@test.com', 'ACTIVE', 'device1', 'ios', '01012345678');
insert into users(user_id,nickname, email, user_state, device_id, device_type, phone) values('user2', '임의의 회원2', 'anyuser2@test.com', 'ACTIVE', 'device2', 'android', '01087654321');

insert into gathering(group_id, group_name, state, point)
values('group1', '임의의 모임', "END_ACTIVITY", ST_GeomFromText('POINT (37.37720712440026 127.11215415764482)'));

insert into group_member(group_member_id, group_id, user_id, state, role)
values('groupMember1', 'group1', 'user1', 'ACTIVE', 'LEADER');

insert into store(store_id,store_name,category,state,owner_id,owner_name,zip_code,address1,address2,rating,review_count,point)
values('store1','임의의 가게','SERVICE','OPEN','user1','임의의 회원1','123-456','경기도','성남시',0,0,ST_GeomFromText('POINT (37.37720712440026 127.11215415764482)'));

insert into product(product_id,product_name,price,product_state,description,store_id)
values('product1','임의의 상품',10000,'FOR_SALE','판매중인 상품입니다.','store1');

/* test order */
insert into orders (message,order_date,name,order_store_id,orderer_id,orderer_name,orderer_phone,user_id,payment_id,payment_method,payment_state,state,total_amounts,order_id)
values ("테스트 주문",1695026980,"임의의 가게","store1","group1","임의의 모임","01012345678","user1",null,null,null,"ORDER",30000,"order1");

insert into order_line (order_id,line_idx,amounts,name,price,product_id,quantity)
values ("order1",0,30000,"임의의 상품",10000,"product1",3);

insert into orders (message,order_date,name,order_store_id,orderer_id,orderer_name,orderer_phone,user_id,payment_id,payment_method,payment_state,state,total_amounts,order_id,last_modified_time)
values ("테스트 주문2",1695026980,"임의의 가게","store1","group1","임의의 모임","01012345678","user1","payment1",null,null,"PAYMENT_COMPLETE",30000,"order2",NOW());

insert into order_line (order_id,line_idx,amounts,name,price,product_id,quantity)
values ("order2",0,30000,"임의의 상품",10000,"product1",3);

insert into image(image_path, image_type, product_id, list_idx) values('external/1','external','product1',0);
insert into image(image_path, image_type, product_id, list_idx) values('internal/1','internal','product1',1);


