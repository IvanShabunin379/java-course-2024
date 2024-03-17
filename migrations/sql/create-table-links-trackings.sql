CREATE TABLE links_trackings (
    tg_chat_id BIGINT REFERENCES tg_chats(id),
    link_id BIGINT REFERENCES links(id),
    started_at TIMESTAMP(6) WITH TIME ZONE
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (tg_chat_id, link_id)
);
