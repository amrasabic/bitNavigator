# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table place (
  title                     varchar(255),
  description               varchar(255),
  address                   varchar(255),
  phone                     varchar(255),
  place_created             datetime(6))
;

create table user (
  id                        integer auto_increment not null,
  email                     varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  password                  varchar(255),
  account_created           datetime(6),
  constraint pk_user primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table place;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

