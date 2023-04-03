alter table T_USER alter column LASTFMSESSIONTOKEN set default null;
update T_USER set LASTFMSESSIONTOKEN = null where LASTFMSESSIONTOKEN = '0';
-- alter table T_USER alter column SPOTIFYACCESSTOKEN set default null;
-- update T_USER set SPOTIFYACCESSTOKEN = null where SPOTIFYACCESSTOKEN = '0';
-- alter table T_USER alter column SPOTIFYREFRESHTOKEN set default null;
-- update T_USER set SPOTIFYREFRESHTOKEN = null where SPOTIFYREFRESHTOKEN = '0';
-- alter table T_USER alter column SPOTIFYACCESSTOKENEXPIRESIN set default null;
-- -- update T_USER set SPOTIFYACCESSTOKENEXPIRESIN = null where SPOTIFYACCESSTOKENEXPIRESIN = '0';
-- alter table T_USER alter column SPOTIFYAUTHCODE set default null;
-- update T_USER set SPOTIFYAUTHCODE = null where SPOTIFYAUTHCODE = '0';

alter table T_TRACK alter column FORMAT type varchar(50);

-- alter table T_PLAYLIST
--    add column ACCESS varchar(10) not null default 'PRIVATE'; 

-- alter table T_ALBUM
--    add column USER_ID varchar(36) not null; 