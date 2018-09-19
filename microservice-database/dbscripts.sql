drop database if exists activedb;
create database activedb;
\connect activedb;

create table userinfo(
	userid int primary key,
	username varchar(20),
	password varchar(50)
);

create table geoloc(
	city varchar(20) primary key,
	lon float,
	lat float
);

create table userpref(
	userid int primary key references userinfo(userid),
	city varchar(20) references geoloc(city),
	country varchar(20),
	companies text
);

insert into geoloc values ('Bloomington, IN', -86.60, 39.17);
insert into userinfo values (1000, 'default', 'password');
insert into userpref values (1000, 'Bloomington, IN', 'US', 'Apple');