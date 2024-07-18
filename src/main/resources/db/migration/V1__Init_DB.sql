create table app_user (
    id uuid not null,
    name varchar(255),
    username varchar(255),
    password varchar(255),
    active boolean not null,
    primary key (id)
);

create table project (
    id uuid not null,
    heading varchar(255),
    description varchar(255),
    primary key (id)
);

create table project_user (
    project_id uuid not null,
    user_id uuid not null,
    primary key (project_id, user_id)
);

create table role (
    id uuid not null,
    name varchar(255),
    primary key (id)
);

create table role_user (
    user_id uuid not null,
    role_id uuid not null,
    primary key (user_id, role_id)
);

create table task (
    id uuid not null,
    heading varchar(255),
    description varchar(255),
    date_of_completion date,
    status varchar(255) check (status in ('NEW','IN_WORK','DONE')),
    project_id uuid,
    user_id uuid,
    primary key (id)
);

alter table if exists project_user
    add constraint user_projectUser_fk
    foreign key (user_id) references app_user;

alter table if exists project_user
    add constraint project_projectUser_fk
    foreign key (project_id) references project;

alter table if exists role_user
    add constraint role_roleUser_fk
    foreign key (role_id) references role;

alter table if exists role_user
    add constraint user_roleUser_fk
    foreign key (user_id) references app_user;

alter table if exists task
    add constraint task_project_fk
    foreign key (project_id) references project;

alter table if exists task
    add constraint task_user_fk
    foreign key (user_id) references app_user;