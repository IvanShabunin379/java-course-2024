CREATE TABLE IF NOT EXISTS links_trackings (
    tg_chat_id BIGINT REFERENCES tg_chats(id) ON DELETE CASCADE,
    link_id BIGINT REFERENCES links(id) ON DELETE CASCADE,

    PRIMARY KEY (tg_chat_id, link_id)
);
