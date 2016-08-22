create sequence department_id_seq increment by 1 start with 1000;
create table department (
	id numeric(19,0) primary key,
	version numeric(10,0) default 0,
	name varchar(500),
	create_ts timestamp default now(),
	create_username varchar(320),
	update_ts timestamp default now(),
	update_username varchar(320),
	constraint deptartment_name_unq unique(name)	
);

create sequence user_profile_id_seq increment by 1 start with 1000;
create table user_profile (
	id numeric(19,0) primary key,
	version numeric(10,0) default 0,
	email varchar(320) not null,
	full_name varchar(500) not null,
	department_id numeric(19,0),
	job_title varchar(100),
	birthday date,
	office_street_address varchar(200),
	office_city varchar(200),
	office_state_or_prov varchar(5),
	office_postal_code varchar(10),
	office_country varchar(3),
	office_phone varchar(10),
	personal_phone varchar(10),
	description text,
	profile_pic bytea,
	create_ts timestamp default now(),
	create_username varchar(320),
	update_ts timestamp default now(),
	update_username varchar(320),
	CONSTRAINT user_profile_email_unq UNIQUE (email)
);

create table recognition (
	id numeric(19,0) primary key,
	version numeric(10,0) default 0,
	submit_ts timestamp,
	sending_user_profile_id numeric(19,0),
	recipient_user_profile_id numeric(19,0),
	recipient_department_id numeric(19,0),
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