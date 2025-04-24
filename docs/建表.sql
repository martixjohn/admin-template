drop database if exists admin_template;

create database admin_template;

use admin_template;

create table user
(
    id                 bigint primary key auto_increment,
    username           char(255) unique not null,
    password           char(255)        not null,
    email              char(255),
    avatar_local_path  varchar(512),
    avatar_server_path varchar(512),
    created_time       datetime,
    updated_time       datetime,
    deleted_time       datetime,
    deleted            boolean default false
);

create table role
(
    id           bigint primary key auto_increment,
    name         char(20) unique not null,
    created_time datetime,
    updated_time datetime,
    deleted_time datetime,
    deleted      boolean default false
);

create table user_role
(
    id           bigint primary key auto_increment,
    user_id      bigint not null,
    role_id      bigint not null,
    created_time datetime,
    updated_time datetime,
    deleted_time datetime,
    deleted      boolean default false
);

create unique index index_user_role on user_role (user_id, role_id);

create table permission
(
    id           bigint primary key auto_increment,
    name         char(20) unique not null,
    created_time datetime,
    updated_time datetime,
    deleted_time datetime,
    deleted      boolean default false
);

create table role_permission
(
    id            bigint primary key auto_increment,
    role_id       bigint not null,
    permission_id bigint not null,
    created_time  datetime,
    updated_time  datetime,
    deleted_time  datetime,
    deleted       boolean default false
);

create unique index index_role_permission on role_permission (role_id, permission_id);


