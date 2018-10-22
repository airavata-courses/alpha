drop database if exists activedb;
drop user if exists $DB_USER;
create database activedb;
\connect activedb;

create sequence userid_seq start 100;
create table userinfo(
	userid int primary key default nextval('userid_seq'),
	username varchar(30),
	password varchar(30)
);

create unique index idx_username on userinfo (username);

create table geoloc(
	city varchar(30) primary key,
	lon float,
	lat float
);

create table countries(
	country varchar(30) primary key
);

create table userpref(
	userid int primary key references userinfo(userid),
	city varchar(30) references geoloc(city),
	country varchar(30) references countries(country),
	company varchar(30),
	get_news_alerts boolean,
	get_weather_alerts boolean
);

create or replace function login_user(username varchar, password varchar)
returns table(userid int, city varchar, country varchar, company varchar, get_news_alerts boolean, get_weather_alerts boolean) as
$$
declare v_userid int;
begin
	if not exists (select 1 from userinfo ui where ui.username = $1 and ui.password = $2) then
		return query select -1, ''::varchar(1),''::varchar(1),''::varchar(1), false, false;
	else
		select into v_userid ui.userid from userinfo ui where ui.username = $1 and ui.password = $2; 
	end if;
	
	return query select v_userid, up.city, up.country, up.company, up.get_news_alerts, up.get_weather_alerts from userpref up where up.userid = v_userid;
end
$$ language plpgsql;

create or replace function create_user(username varchar, password varchar, city varchar, country varchar, company varchar, get_news_alerts boolean, get_weather_alerts boolean)	
returns int as
$$
declare v_userid int;
begin
	if exists (select 1 from userinfo ui where ui.username = $1) then
		return 0;
	end if;
	
	with u as (insert into userinfo values(default, $1, $2) returning userid)
	select into v_userid userid from u;
	
	insert into userpref values (v_userid, $3, $4, $5, $6, $7);
	return v_userid;
end
$$ language plpgsql;

insert into geoloc (city, lat, lon) values 
	('Bloomington, IN', 39.167,-86.53),
	('New York, NY', 40.67,-73.96),
	('Los Angeles, CA', 34.055, -118.397),
	('Chicago, IL', 41.875, -87.655),
	('Houston, TX', 29.764,-95.387);

insert into countries values ('US'), ('CA'), ('AU'), ('JP');

create user $DB_USER with encrypted password '$DB_PASSWORD';
grant all privileges on all tables in schema public to $DB_USER;
grant all privileges on all sequences in schema public to $DB_USER;
grant all privileges on all functions in schema public to $DB_USER;	

\connect postgres;
