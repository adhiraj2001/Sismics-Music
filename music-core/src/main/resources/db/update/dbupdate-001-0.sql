alter table T_USER alter column LASTFMSESSIONTOKEN set default null;
update T_USER set LASTFMSESSIONTOKEN = null where LASTFMSESSIONTOKEN = '0';
alter table T_TRACK alter column FORMAT type varchar(50);

-- alter table T_PLAYLIST
--    add column ACCESS varchar(10) not null default 'PRIVATE'; 

-- alter table T_ALBUM
--    add column USER_ID varchar(36) not null; 