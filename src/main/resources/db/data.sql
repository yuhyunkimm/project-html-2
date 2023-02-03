insert into user_tb(username, password, email, created_at) values('ssar', '1234', 'ssar@nate.com', now());
insert into user_tb(username, password, email, created_at) values('cos', '1234', 'cos@nate.com', now());

insert into board_tb(title, user_id, created_at) values('첫 번째 글입니다.', 1, now());
insert into board_tb(title, user_id, created_at) values('두 번째 글입니다.', 1, now());
insert into board_tb(title, user_id, created_at) values('세 번째 글입니다.', 1, now());
insert into board_tb(title, user_id, created_at) values('네 번째 글입니다.', 1, now());
insert into board_tb(title, user_id, created_at) values('다섯 번째 글입니다.', 1, now());
insert into board_tb(title, user_id, created_at) values('여섯 번째 글입니다.', 2, now());
insert into board_tb(title, user_id, created_at) values('일곱 번째 글입니다.', 2, now());
insert into board_tb(title, user_id, created_at) values('여덟 번째 글입니다.', 2, now());
insert into board_tb(title, user_id, created_at) values('아홉 번째 글입니다.', 2, now());
insert into board_tb(title, user_id, created_at) values('열 번째 글입니다.', 2, now());


commit;