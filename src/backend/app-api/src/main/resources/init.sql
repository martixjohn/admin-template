drop table if exists user;
create table user
(
    id                 bigint primary key auto_increment,

    username           char(255) unique not null,
    nickname           char(255),
    password           char(255)        not null,
    email              char(255),
    avatar_local_path  varchar(512) comment "头像本地路径",
    avatar_server_path varchar(512) comment "头像服务端路径",
    account_locked     boolean          not null default false,
    last_login_time    datetime,
    disabled           boolean          not null default false,

    created_time       datetime         not null default current_date(),
    created_by         bigint,
    updated_time       datetime,
    updated_by         bigint,
    description        varchar(512),
    deleted            boolean          not null default false
);

create trigger tr_update_user
    before update
    on user
    for each row
    set updated_time = current_date();

drop table if exists role;
create table role
(
    id           bigint primary key auto_increment,

    name         char(20) unique not null comment "对应角色名称",

    created_time       datetime         not null default current_date(),
    created_by         bigint,
    updated_time       datetime,
    updated_by         bigint,
    description        varchar(512),
    deleted            boolean          not null default false
);


drop table if exists user_role;
create table user_role
(
    id           bigint primary key auto_increment,

    user_id      bigint   not null,
    role_id      bigint   not null,

    created_time       datetime         not null default current_date(),
    created_by         bigint,
    updated_time       datetime,
    updated_by         bigint,
    description        varchar(512),
    deleted            boolean          not null default false
);

create unique index index_user_role on user_role (user_id, role_id);

drop table if exists permission;
create table permission
(
    id           bigint primary key auto_increment,

    name         char(20) unique not null comment "对应权限名称",

    created_time       datetime         not null default current_date(),
    created_by         bigint,
    updated_time       datetime,
    updated_by         bigint,
    description        varchar(512),
    deleted            boolean          not null default false
);

drop table if exists role_permission;
create table role_permission
(
    id            bigint primary key auto_increment,

    role_id       bigint   not null,
    permission_id bigint   not null,

    created_time       datetime         not null default current_date(),
    created_by         bigint,
    updated_time       datetime,
    updated_by         bigint,
    description        varchar(512),
    deleted            boolean          not null default false
);

create unique index index_role_permission on role_permission (role_id, permission_id);

