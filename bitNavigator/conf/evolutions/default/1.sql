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
  public_id                 varchar(255),
  image_url                 varchar(255),
  secret_image_url          varchar(255),
  is_published              tinyint(1) default 0,
  place_id                  integer,
  user_id                   integer,
  constraint uq_image_user_id unique (user_id),
  constraint pk_image primary key (id))
;

create table message (
  id                        integer auto_increment not null,
  content                   varchar(255),
  reservation_id            integer,
  message_created           datetime(6),
  sender_id                 integer,
  constraint pk_message primary key (id))
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
  status_id                 integer,
  reservation_created       datetime(6),
  reservation_day           varchar(255),
  constraint pk_reservation primary key (id))
;

create table service (
  id                        integer auto_increment not null,
  service_type              varchar(255),
  service_icon              varchar(255),
  is_reservable             tinyint(1) default 0,
  constraint uq_service_service_type unique (service_type),
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
  constraint uq_user_email unique (email),
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
alter table message add constraint fk_message_reservation_5 foreign key (reservation_id) references reservation (id) on delete restrict on update restrict;
create index ix_message_reservation_5 on message (reservation_id);
alter table message add constraint fk_message_sender_6 foreign key (sender_id) references user (id) on delete restrict on update restrict;
create index ix_message_sender_6 on message (sender_id);
alter table place add constraint fk_place_user_7 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_place_user_7 on place (user_id);
alter table place add constraint fk_place_service_8 foreign key (service_id) references service (id) on delete restrict on update restrict;
create index ix_place_service_8 on place (service_id);
alter table report add constraint fk_report_comment_9 foreign key (comment_id) references comment (id) on delete restrict on update restrict;
create index ix_report_comment_9 on report (comment_id);
alter table report add constraint fk_report_user_10 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_report_user_10 on report (user_id);
alter table reservation add constraint fk_reservation_user_11 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_reservation_user_11 on reservation (user_id);
alter table reservation add constraint fk_reservation_place_12 foreign key (place_id) references place (id) on delete restrict on update restrict;
create index ix_reservation_place_12 on reservation (place_id);
alter table reservation add constraint fk_reservation_status_13 foreign key (status_id) references status (id) on delete restrict on update restrict;
create index ix_reservation_status_13 on reservation (status_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table comment;

drop table image;

drop table message;

drop table place;

drop table report;

drop table reservation;

drop table service;

drop table status;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

