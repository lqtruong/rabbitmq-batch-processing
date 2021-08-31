use `sample`;

create table if not exists `users`(
    `id`            integer primary key auto_increment,
    `username`      varchar(100) not null,
    `email`         varchar(100) not null,
    `tenant`        varchar(20) not null,
    `createdAt`     datetime not null default current_timestamp,
    `modifiedAt`    datetime not null default current_timestamp
);

insert into `users`
(
    id, email, username, tenant, createdAt, modifiedAt
) values
(1, 'truong@mail.com', 'truong', 'vn', sysdate(), sysdate())
,(2, 'peter@mail.com', 'peter', 'tr', sysdate(), sysdate())
;