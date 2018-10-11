drop database if exists activedb;
create database activedb;
\connect activedb;

create sequence userid_seq;
create table userinfo(
	userid int primary key default nextval('userid_seq'),
	username varchar(30),
	password varchar(50)
);

create unique index idx_username on userinfo (username);

create table geoloc(
	city varchar(30) primary key,
	lon float,
	lat float
);

create table userpref(
	userid int primary key references userinfo(userid),
	city varchar(30) references geoloc(city),
	country varchar(30),
	company varchar(30),
	get_news_alerts boolean,
	get_weather_alerts boolean
);

create or replace function create_user(username varchar, password varchar, city varchar, country varchar, company varchar, get_news_alert boolean, get_weather_alert boolean)	
returns int as
$$
declare v_userid int;
begin
	with u as (insert into userinfo values(default, $1, $2) returning userid)
	select into v_userid userid from u;
	insert into userpref values (v_userid, $3, $4, $5, $6, $7);
	return v_userid;
end
$$ language plpgsql;

insert into geoloc values ('Bloomington, IN', -86.60, 39.17);

\connect postgres;