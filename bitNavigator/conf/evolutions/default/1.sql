# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table client_ip (
  id                        integer auto_increment not null,
  place_id                  integer,
  ip_address                varchar(255),
  constraint pk_client_ip primary key (id))
;

create table comment (
  id                        integer auto_increment not null,
  comment_content           varchar(255),
  rate                      integer,
  comment_created           datetime(6),
  place_id                  integer,
  user_id                   integer,
  constraint pk_comment primary key (id))
;

create table faq (
  id                        integer auto_increment not null,
  question                  varchar(255),
  answer                    TEXT,
  constraint pk_faq primary key (id))
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
  sent                      datetime(6),
  sender_id                 integer,
  reciever_id               integer,
  seen                      tinyint(1) default 0,
  constraint pk_message primary key (id))
;

create table phone_number (
  id                        integer auto_increment not null,
  number                    varchar(255),
  token                     integer,
  validated                 tinyint(1) default 0,
  token_sent                tinyint(1) default 0,
  user_id                   integer,
  constraint pk_phone_number primary key (id))
;

create table place (
  id                        integer auto_increment not null,
  title                     varchar(255),
  description               TEXT,
  longitude                 double,
  latitude                  double,
  address                   varchar(255),
  place_created             datetime(6),
  num_of_views              integer,
  num_of_reservations       integer,
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
  timestamp                 datetime(6),
  reservation_date          datetime(6),
  price                     double,
  payment_id                varchar(255),
  constraint pk_reservation primary key (id))
;

create table rest_token (
  id                        bigint auto_increment not null,
  token                     varchar(255) not null,
  constraint pk_rest_token primary key (id))
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
  admin                     tinyint(1) default 0,
  token                     varchar(255),
  validated                 tinyint(1) default 0,
  constraint uq_user_email unique (email),
  constraint uq_user_token unique (token),
  constraint pk_user primary key (id))
;

create table working_hours (
  id                        integer auto_increment not null,
  place_id                  integer,
  open1                     integer,
  close1                    integer,
  open2                     integer,
  close2                    integer,
  open3                     integer,
  close3                    integer,
  open4                     integer,
  close4                    integer,
  open5                     integer,
  close5                    integer,
  open6                     integer,
  close6                    integer,
  open7                     integer,
  close7                    integer,
  constraint uq_working_hours_place_id unique (place_id),
  constraint pk_working_hours primary key (id))
;

alter table client_ip add constraint fk_client_ip_place_1 foreign key (place_id) references place (id) on delete restrict on update restrict;
create index ix_client_ip_place_1 on client_ip (place_id);
alter table comment add constraint fk_comment_place_2 foreign key (place_id) references place (id) on delete restrict on update restrict;
create index ix_comment_place_2 on comment (place_id);
alter table comment add constraint fk_comment_user_3 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_comment_user_3 on comment (user_id);
alter table image add constraint fk_image_place_4 foreign key (place_id) references place (id) on delete restrict on update restrict;
create index ix_image_place_4 on image (place_id);
alter table image add constraint fk_image_user_5 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_image_user_5 on image (user_id);
alter table message add constraint fk_message_reservation_6 foreign key (reservation_id) references reservation (id) on delete restrict on update restrict;
create index ix_message_reservation_6 on message (reservation_id);
alter table message add constraint fk_message_sender_7 foreign key (sender_id) references user (id) on delete restrict on update restrict;
create index ix_message_sender_7 on message (sender_id);
alter table message add constraint fk_message_reciever_8 foreign key (reciever_id) references user (id) on delete restrict on update restrict;
create index ix_message_reciever_8 on message (reciever_id);
alter table phone_number add constraint fk_phone_number_user_9 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_phone_number_user_9 on phone_number (user_id);
alter table place add constraint fk_place_user_10 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_place_user_10 on place (user_id);
alter table place add constraint fk_place_service_11 foreign key (service_id) references service (id) on delete restrict on update restrict;
create index ix_place_service_11 on place (service_id);
alter table report add constraint fk_report_comment_12 foreign key (comment_id) references comment (id) on delete restrict on update restrict;
create index ix_report_comment_12 on report (comment_id);
alter table report add constraint fk_report_user_13 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_report_user_13 on report (user_id);
alter table reservation add constraint fk_reservation_user_14 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_reservation_user_14 on reservation (user_id);
alter table reservation add constraint fk_reservation_place_15 foreign key (place_id) references place (id) on delete restrict on update restrict;
create index ix_reservation_place_15 on reservation (place_id);
alter table reservation add constraint fk_reservation_status_16 foreign key (status_id) references status (id) on delete restrict on update restrict;
create index ix_reservation_status_16 on reservation (status_id);
alter table working_hours add constraint fk_working_hours_place_17 foreign key (place_id) references place (id) on delete restrict on update restrict;
create index ix_working_hours_place_17 on working_hours (place_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table client_ip;

drop table comment;

drop table faq;

drop table image;

drop table message;

drop table phone_number;

drop table place;

drop table report;

drop table reservation;

drop table rest_token;

drop table service;

drop table status;

drop table user;

drop table working_hours;

SET FOREIGN_KEY_CHECKS=1;

