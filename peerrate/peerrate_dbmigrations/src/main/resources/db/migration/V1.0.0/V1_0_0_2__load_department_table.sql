insert into department (id,version,name,create_ts, create_username, update_ts, update_username )
	values (nextval('department_id_seq'), 0, 'SALES', now(), 'DBA', now(), 'DBA');

insert into department (id,version,name,create_ts, create_username, update_ts, update_username )
	values (nextval('department_id_seq'), 0, 'OPERATIONS', now(), 'DBA', now(), 'DBA');
	
insert into department (id,version,name,create_ts, create_username, update_ts, update_username )
	values (nextval('department_id_seq'), 0, 'DEVELOPMENT', now(), 'DBA', now(), 'DBA');

insert into department (id,version,name,create_ts, create_username, update_ts, update_username )
	values (nextval('department_id_seq'), 0, 'SUPPORT', now(), 'DBA', now(), 'DBA');	