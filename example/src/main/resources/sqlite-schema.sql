create table if not exists player (id text, hp int, attack int, primary key (id));
create table if not exists monster (id text, hp int, attack int, primary key (id));
delete from player;
delete from monster;
