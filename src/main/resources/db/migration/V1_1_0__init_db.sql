create table discord_servers
(
    id               BIGINT NOT NULL primary key,
    voice_channel_id BIGINT
);

create unique index unique_discord_servers_voice_channel_id_index
    on discord_servers (voice_channel_id);

create table tracks
(
    id                BIGINT auto_increment NOT NULL primary key,
    title             VARCHAR(255),
    url               CLOB NOT NULL,
    discord_server_id BIGINT  NOT NULL
);

create index tracks_discord_server_id_index
    on tracks (discord_server_id);

INSERT INTO tracks (title, url, discord_server_id)
VALUES ('Lofi Girl', 'https://www.youtube.com/watch?v=jfKfPfyJRdk', -1);
INSERT INTO tracks (title, url, discord_server_id)
VALUES ('Coffee Shop Radio', 'https://www.youtube.com/watch?v=-5KAN9_CzSA', -1);
INSERT INTO tracks (title, url, discord_server_id)
VALUES ('Best Drum & Bass Mix 2020', 'https://www.youtube.com/watch?v=7j4WQ5qOBZY', -1);
INSERT INTO tracks (title, url, discord_server_id)
VALUES ('Memphis 66.6, 24/7 Phonk Radio', 'https://www.youtube.com/watch?v=L2sTrfHP9uU', -1);

create table play_history
(
    track_id          BIGINT,
    track_url         CLOB,
    start_time        TIMESTAMP not null,
    discord_server_id BIGINT    not null,
    constraint "play_history_DISCORD_SERVERS_ID_fk"
        foreign key (discord_server_id) references DISCORD_SERVERS,
    constraint "play_history_TRACKS_t_fk"
        foreign key (track_id) references TRACKS
);
