# 📋 企業名簿管理アプリケーション (Company Directory App)

企業情報の管理・検索・Excel連携を効率化するためのモダンなWebアプリケーションです。
Spring Boot 4 + MySQLを用いた堅牢なバックエンドと、Tailwind CSSによるレスポンシブで洗練されたUIを組み合わせています。

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-3.4-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)

## ✨ 主な機能 (Key Features)

### 1. 高度なExcel連携 (Import/Export)
業務効率を最大化するため、単なるアップロードではなく「事前検証（Pre-validation）」を重視した設計です。
*   **ドラッグ＆ドロップ**: 直感的な操作でファイルをアップロード。
*   **事前プレビュー**: 登録前に解析結果（成功/警告/エラー）を一覧表示。エラー箇所は赤くハイライトされ、理由が明示されます。
*   **柔軟なバリデーション**: 必須チェック、型チェックに加え、「DB内の同名企業警告」「同一ファイル内の重複チェック」など高度な整合性確認を行います。
*   **エクスポート**: 検索結果をExcelファイルとしてダウンロード可能。

### 2. コンシューマ向けレベルのモダンUI
管理画面であっても使いやすさと美しさを追求しました。
*   **レスポンシブ対応**: PC、タブレット、スマホで快適に動作。
*   **ダークモード搭載**: OSの設定やユーザーの好みに合わせてテーマ切り替えが可能。
*   **インタラクティブな操作**: 住所自動入力、詳細検索のアコーディオン展開、トースト通知など。

### 3. エンタープライズ水準のセキュリティ
*   **堅牢な認証**: Spring Securityによるログイン制御。
*   **アカウントロック**: 連続失敗時のロック機能（15分間）およびIPアドレスベースの遅延処理（スロットリング）を実装し、ブルートフォース攻撃を防ぎます。
*   **CSRF/XSS対策**: フレームワーク標準の保護に加え、適切なサニタイズを実施。

### 4. データ保全とライフサイクル管理
*   **論理削除 (Logical Delete)**: 誤操作に備え、削除データは「ゴミ箱」へ移動。いつでも復元可能です。
*   **自動パージ**: ゴミ箱のリソース圧迫を防ぐため、30日経過したデータはバッチ処理により自動的に物理削除されます。

---

## 🛠 技術スタック (Tech Stack)

| Category | Technology |
|---|---|
| **Backend** | Java 21, Spring Boot 4.0.0 (Web, Data JPA, Security, Validation) |
| **Database** | MySQL 8.0 (Docker Container) |
| **Frontend** | Thymeleaf, Tailwind CSS (CDN), Vanilla JavaScript |
| **Excel Ops** | Apache POI 5.2.5 |
| **Build/Infra** | Gradle, Docker Compose |

---

## 🚀 セットアップと実行 (Getting Started)

### 前提条件
*   Docker Desktop がインストールされていること
*   Java 21 (ローカルでビルドする場合)

### 実行手順

1. **リポジトリのクローン**
   ```bash
   git clone <repository-url>
   cd company-directory
   ```

2. **Dockerコンテナの起動（DB & App）**
   `compose.yaml` を使用して、MySQLとアプリケーションを一括で起動します。
   ```bash
   docker compose up -d
   ```
   *   初回起動時はMySQLの初期化などで数分かかる場合があります。

3. **ブラウザでアクセス**
   *   URL: `http://localhost:8080/login`
   *   初期アカウント（※初回起動時にDBシードデータがある場合）
       *   ID: `admin`
       *   Pass: `admin123` (※適宜変更してください)

### 開発環境での実行 (Hot Reload等)
DBのみDockerで起動し、アプリはローカルで動かす場合：
```bash
# DB起動
docker compose up db -d

# アプリ起動
./gradlew bootRun
```

---

## 📂 ディレクトリ構成 (Structure)

```
src/main/java/com/example/company_directory
├── config/        # Security設定, MVC設定
├── controller/    # 画面遷移とAPIのエンドポイント
├── service/       # ビジネスロジック (Excel解析, アカウントロック等)
├── repository/    # JPA Repository, Specification (検索条件)
├── entity/        # DBテーブル定義
├── dto/           # データ転送用オブジェクト (Excel行データ等)
└── batch/         # 定期実行タスク (ゴミ箱自動削除)
```

## 💡 工夫した点 (Architecture Highlights)

*   **JPA Specificationによる動的検索**: 複数の検索条件（キーワード、日付範囲、ID一致など）を柔軟に組み合わせるため、Specificationパターンを採用し、メンテナンス性の高い検索ロジックを構築しました。
*   **Excel解析の疎結合化**: Excel読み込み処理をHelperクラスとServiceに分離。DTOを用いてデータを正規化することで、将来的なフォーマット変更にも強い設計にしています。
*   **例外ハンドリング**: `DataIntegrityViolationException` などを適切にキャッチし、ユーザーフレンドリーなエラーメッセージに変換して表示しています。
