create sequence department_id_seq increment by 1 start with 1000;
create table department (
	id bigint primary key,
	version int4 default 0,
	name varchar(500),
	create_ts timestamp default now(),
	create_username varchar(320),
	update_ts timestamp default now(),
	update_username varchar(320),
	constraint deptartment_name_unq unique(name)	
);

create sequence user_profile_id_seq increment by 1 start with 1000;
create table user_profile (
	id bigint primary key,
	version int4 default 0,
	email varchar(320) not null,
	full_name varchar(500) not null,
	department_id bigint,
	job_title varchar(100),
	date_of_birth date,
	office_street_address varchar(200),
	office_city varchar(200),
	office_state_or_prov varchar(5),
	office_postal_code varchar(10),
	office_country varchar(3),
	office_phone varchar(10),
	personal_phone varchar(10),
	description text,
	profile_pic_content_type varchar(200),
	profile_pic bytea,
	create_ts timestamp default now(),
	create_username varchar(320),
	update_ts timestamp default now(),
	update_username varchar(320),
	CONSTRAINT user_profile_email_unq UNIQUE (email)
);
create sequence recognition_id_seq increment by 1 start with 1000;
create table recognition (
	id bigint primary key,
	version int4 default 0,
	submit_ts timestamp,
	sending_user_profile_id bigint,
	recipient_user_profile_id bigint,
	recipient_department_id bigint,
	message text,
	attachment bytea,	
	attachment_content_type varchar(200),
	create_ts timestamp default now(),
	create_username varchar(320),
	update_ts timestamp default now(),
	update_username varchar(320),
	foreign key(sending_user_profile_id) references user_profile(id),
	foreign key(recipient_user_profile_id) references user_profile(id),
	foreign key(recipient_department_id) references department(id)
	
);