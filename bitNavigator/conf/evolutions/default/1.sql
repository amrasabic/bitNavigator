# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table image (
  id                        integer auto_increment not null,
  name                      varchar(255),
  path                      varchar(255),
  constraint pk_image primary key (id))
;

create table place (
  id                        integer auto_increment not null,
  title                     varchar(255),
  description               TEXT,
  longitude                 double,
  latitude                  double,
  place_created             datetime(6),
  user_id                   integer,
  constraint pk_place primary key (id))
;

create table user (
  id                        integer auto_increment not null,
  email                     varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  password                  varchar(255),
  account_created           datetime(6),
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id))
;

alter table place add constraint fk_place_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_place_user_1 on place (user_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table image;

drop table place;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

