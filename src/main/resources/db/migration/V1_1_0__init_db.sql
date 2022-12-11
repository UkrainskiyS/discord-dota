create table discord_servers
(
    id               BIGINT NOT NULL primary key,
    voice_channel_id BIGINT
);

create unique index unique_discord_servers_voice_channel_id_index
    on discord_servers (voice_channel_id);

create table tracks
(
    id                BIGINT NOT NULL generated always as identity (exhausted) primary key,
    title             VARCHAR,
    discord_server_id BIGINT not null
);

create index tracks_discord_server_id_index
    on tracks (discord_server_id);
