# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table comment (
  id                        integer auto_increment not null,
  comment_content           varchar(255),
  comment_created           datetime(6),
  place_id                  integer,
  user_id                   integer,
  constraint pk_comment primary key (id))
;

create table image (
  id                        integer auto_increment not null,
  name                      varchar(255),
  path                      varchar(255),
  place_id                  integer,
  constraint uq_image_place_id unique (place_id),
  constraint pk_image primary key (id))
;

create table place (
  id                        integer auto_increment not null,
  title                     varchar(255),
  description               TEXT,
  longitude                 double,
  latitude                  double,
  address                   varchar(255),
  place_created             datetime(6),
  user_id                   integer,
  service_id                integer,
  constraint uq_place_service_id unique (service_id),
  constraint pk_place primary key (id))
;

create table service (
  id                        integer auto_increment not null,
  service_type              varchar(255),
  constraint pk_service primary key (id))
;

create table user (
  id                        integer auto_increment not null,
  email                     varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  password                  varchar(255),
  account_created           datetime(6),
  admin                     tinyint(1) default 0,
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id))
;

alter table comment add constraint fk_comment_place_1 foreign key (place_id) references place (id) on delete restrict on update restrict;
create index ix_comment_place_1 on comment (place_id);
alter table comment add constraint fk_comment_user_2 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_comment_user_2 on comment (user_id);
alter table image add constraint fk_image_place_3 foreign key (place_id) references place (id) on delete restrict on update restrict;
create index ix_image_place_3 on image (place_id);
alter table place add constraint fk_place_user_4 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_place_user_4 on place (user_id);
alter table place add constraint fk_place_service_5 foreign key (service_id) references service (id) on delete restrict on update restrict;
create index ix_place_service_5 on place (service_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table comment;

drop table image;

drop table place;

drop table service;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

