-- quizテーブルを作成
CREATE TABLE IF NOT EXISTS quiz (
    id SERIAL PRIMARY KEY,
    question TEXT NOT NULL,
    answer BOOLEAN NOT NULL,
    author VARCHAR(20) NOT NULL
);

-- 初期クイズデータを登録
INSERT INTO quiz (question, answer, author)
VALUES
    ('日本の首都は東京である', TRUE, 'テスト'),
    ('フォークは食器である', TRUE, 'テスト');