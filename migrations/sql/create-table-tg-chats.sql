CREATE TABLE tg_chats (
    id BIGINT PRIMARY KEY,
    created_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    created_by TEXT NOT NULL
);
