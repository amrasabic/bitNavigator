# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table comment (
  id                        integer auto_increment not null,
  comment_content           varchar(255),
  rate                      integer,
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
  user_id                   integer,
  constraint uq_image_user_id unique (user_id),
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
  constraint pk_place primary key (id))
;

create table report (
  id                        integer auto_increment not null,
  comment_id                integer,
  user_id                   integer,
  constraint pk_report primary key (id))
;

create table reservation (
  id                        integer auto_increment not null,
  user_id                   integer,
  place_id                  integer,
  title                     varchar(255),
  description               varchar(255),
  status_id                 integer,
  constraint pk_reservation primary key (id))
;

create table service (
  id                        integer auto_increment not null,
  service_type              varchar(255),
  service_icon              varchar(255),
  is_reservable             tinyint(1) default 0,
  constraint pk_service primary key (id))
;

create table status (
  id                        integer auto_increment not null,
  status                    varchar(255),
  constraint uq_status_status unique (status),
  constraint pk_status primary key (id))
;

create table user (
  id                        integer auto_increment not null,
  email                     varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  password                  varchar(255),
  account_created           datetime(6),
  phone_number              varchar(255),
  admin                     tinyint(1) default 0,
  image_id                  integer,
  constraint uq_user_email unique (email),
  constraint uq_user_image_id unique (image_id),
  constraint pk_user primary key (id))
;

alter table comment add constraint fk_comment_place_1 foreign key (place_id) references place (id) on delete restrict on update restrict;
create index ix_comment_place_1 on comment (place_id);
alter table comment add constraint fk_comment_user_2 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_comment_user_2 on comment (user_id);
alter table image add constraint fk_image_place_3 foreign key (place_id) references place (id) on delete restrict on update restrict;
create index ix_image_place_3 on image (place_id);
alter table image add constraint fk_image_user_4 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_image_user_4 on image (user_id);
alter table place add constraint fk_place_user_5 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_place_user_5 on place (user_id);
alter table place add constraint fk_place_service_6 foreign key (service_id) references service (id) on delete restrict on update restrict;
create index ix_place_service_6 on place (service_id);
alter table report add constraint fk_report_comment_7 foreign key (comment_id) references comment (id) on delete restrict on update restrict;
create index ix_report_comment_7 on report (comment_id);
alter table report add constraint fk_report_user_8 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_report_user_8 on report (user_id);
alter table reservation add constraint fk_reservation_user_9 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_reservation_user_9 on reservation (user_id);
alter table reservation add constraint fk_reservation_place_10 foreign key (place_id) references place (id) on delete restrict on update restrict;
create index ix_reservation_place_10 on reservation (place_id);
alter table reservation add constraint fk_reservation_status_11 foreign key (status_id) references status (id) on delete restrict on update restrict;
create index ix_reservation_status_11 on reservation (status_id);
alter table user add constraint fk_user_image_12 foreign key (image_id) references image (id) on delete restrict on update restrict;
create index ix_user_image_12 on user (image_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table comment;

drop table image;

drop table place;

drop table report;

drop table reservation;

drop table service;

drop table status;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

