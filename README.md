# 〇×クイズアプリ

## 使用技術
- Java 21
- Spring Boot
- PostgreSQL
- Thymeleaf
- Gradle

## データベースの設定

1. PostgreSQLで `quiz` データベースを作成する。
2. `database/quiz_data.sql` を実行してテーブルと初期データを作成する。
3. `src/main/resources/application-local.properties` を作成する。

以下の内容を記述する。

spring.datasource.password=自分のPostgreSQLパスワード

## アプリケーションの起動

Eclipseから `QuizApplication.java` を実行する。

ブラウザで以下のURLを開く。

http://localhost:8080/