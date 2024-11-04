create table if not exists player (id text, hp int, attack int, primary key (id));
create table if not exists monster (id text, hp int, attack int, primary key (id));
create table if not exists contact (id long, is_organization bool, primary key (id));
delete from player;
delete from monster;
delete from contact;
