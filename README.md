# Todo App — Консольное Java-приложение с CI/CD (GitHub Actions & Packages)

![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)
![Gradle](https://img.shields.io/badge/Gradle-8.8-blue.svg)
![JUnit](https://img.shields.io/badge/JUnit-5.10-green.svg)
![CI/CD](https://img.shields.io/badge/GitHub%20Actions-CI%2FCD-blueviolet.svg)
![Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)

Учебный проект по дисциплине **МДК 02.02 «Инструментальные средства разработки программного обеспечения»**, специальность 09.02.07 «Информационные системы и программирование» (ГБПОУ НСО НАКТ им. Б.С. Галущака).

Проект демонстрирует профессиональную организацию разработки консольного приложения на **Java 21** с применением:
* Системы сборки **Gradle** и генератора исполняемого Fat-JAR (**Shadow Plugin**);
* Модели ветвления **GitHub Flow**;
* Модульного тестирования на **JUnit 5 Jupiter**;
* Непрерывной интеграции и доставки (**CI/CD**) на базе **GitHub Actions**;
* Публикации релизов в **GitHub Releases** и артефактов в **GitHub Packages** (Maven-репозиторий);
* Запуска ночных сборок по расписанию (**Nightly builds**).

---

## 📌 Архитектура проекта и стек технологий

```
todo-app/
├── .github/
│   └── workflows/
│       ├── ci.yml                 # CI: компиляция, запуск тестов, сборка fat-JAR, релиз в GitHub Releases
│       ├── publish-package.yml    # CD: публикация fat-JAR в GitHub Packages (Maven)
│       └── nightly.yml            # Ночные проверки стабильности по расписанию (cron)
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── src/
│   ├── main/
│   │   └── java/
│   │       └── org/
│   │           └── example/
│   │               ├── TodoApp.java     # CLI интерфейс и диспетчер команд
│   │               └── TodoList.java    # Бизнес-логика работы со списком задач
│   └── test/
│       └── java/
│           └── org/
│               └── example/
│                   └── TodoListTest.java # Набор модульных тестов JUnit 5
├── .gitignore                     # Исключения бинарников, логов и файлов IDE
├── build.gradle                   # Конфигурация сборки, плагинов, shadowJar и публикации
├── gradle.properties              # Централизованное управление версией проекта
├── gradlew / gradlew.bat          # Gradle Wrapper для Linux/macOS и Windows
└── settings.gradle                # Настройки проекта (название корня)
```

---

## 💻 Функционал приложения (CLI)

Консольное приложение `TodoApp` поддерживает интерактивный цикл управления задачами:

| Команда | Аргументы | Описание | Пример |
|---|---|---|---|
| `add` | `<task>` | Добавляет новую задачу в список (с триммингом пробелов) | `add Подготовить отчет по МДК` |
| `list` | — | Выводит список всех задач с индексами | `list` |
| `remove` | `<index>` | Удаляет задачу по указанному индексу | `remove 0` |
| `done` | `<index>` | Отмечает задачу как выполненную (`[DONE] ...`) | `done 0` |
| `search` | `<substring>`| Ищет задачи по подстроке без учета регистра | `search отчет` |
| `clear` | — | Очищает весь список текущих задач | `clear` |
| `exit` | — | Завершает работу приложения | `exit` |

---

## 🚀 Локальный запуск и тестирование

### 1. Запуск через Gradle
```bash
# Windows
.\gradlew.bat run --console=plain

# Linux / macOS
./gradlew run --console=plain
```

### 2. Запуск юнит-тестов (JUnit 5)
```bash
# Windows
.\gradlew.bat test

# Linux / macOS
./gradlew test
```
Отчет о тестировании генерируется в `build/reports/tests/test/index.html`.

### 3. Сборка исполняемого Fat-JAR
```bash
# Windows
.\gradlew.bat clean build

# Запуск собранного fat-JAR
java -jar build/libs/todo-app-0.3.0.jar
```

---

## 🔄 Модель ветвления GitHub Flow

В репозитории строго соблюдается модель **GitHub Flow**:
1. Основная ветка `main` всегда стабильна и готова к релизу.
2. Для каждой задачи создается отдельная ветка от `main`:
   - `feat/todo-app-implementation` — реализация базовой логики `TodoList`, CLI `TodoApp` и юнит-тестов `TodoListTest`.
   - `feat/actions` — интеграция плагина Shadow, версионирование через `gradle.properties`, пайплайн `ci.yml`.
   - `feat/github-packages` — интеграция `maven-publish`, пайплайн `publish-package.yml`.
   - `feat/app-extensions-and-nightly` — расширение команд (`clear`, `done`, `search`), тесты и расписание `nightly.yml`.
3. Все изменения сливаются в `main` через Pull Request (Merge Commit).

### Граф коммитов (Git Graph)
```
*   Merge pull request #4 from feat/app-extensions-and-nightly
|\  
| * feat: implement clear, done, search commands, JUnit tests and nightly CI workflow
|/  
*   Merge pull request #3 from feat/github-packages
|\  
| * add cd
|/  
*   Merge pull request #2 from feat/actions
|\  
| * add ci
| * change build.gradle for jar build
|/  
*   Merge pull request #1 from feat/todo-app-implementation
|\  
| * add tests for the todo app
| * add TodoApp
| * add TodoList
|/  
* Initial commit
```

---

## ⚙️ Автоматизация CI/CD (GitHub Actions)

### 1. `ci.yml` — CI / Build & Release
* **Триггер:** `push` в ветку `main`, `pull_request` в `main`.
* **Действия:**
  1. Клонирование репозитория (`actions/checkout@v4`).
  2. Установка прав выполнения на `gradlew`.
  3. Настройка Eclipse Temurin JDK 21 (`actions/setup-java@v4`).
  4. Кеширование зависимостей Gradle (`actions/cache@v4`).
  5. Считывание версии из `gradle.properties`.
  6. Сборка и тестирование: `./gradlew --no-daemon clean build`. При падении тестов пайплайн завершается с ошибкой и релиз блокируется.
  7. Автоматическое создание **GitHub Release** с тегом `v<version>` и прикрепление собранного исполняемого **Fat-JAR** (`todo-app-<version>.jar`).

### 2. `publish-package.yml` — Publish to GitHub Packages
* **Триггер:** Завершение workflow `CI / Build & Release` со статусом `success` (`workflow_run`).
* **Действия:**
  1. Клонирование репозитория и настройка JDK 21.
  2. Публикация Fat-JAR как Maven-артефакта в **GitHub Packages** (`./gradlew publish`) с правами `packages: write`.

### 3. `nightly.yml` — Nightly Build & Test
* **Триггер:** Ежедневно в 02:00 UTC по расписанию (`schedule: - cron: '0 2 * * *'`) и вручную (`workflow_dispatch`).
* **Действия:** Выполняет полную чистую сборку и запуск всех тестов для регулярного мониторинга стабильности кодовой базы без выпуска релизов.

---

## 📦 Подключение пакета из GitHub Packages

Для подключения опубликованного пакета в другом Gradle-проекте:

```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/Diskae/todo-app")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")
        }
    }
    mavenCentral()
}

dependencies {
    implementation 'org.example:todo-app:0.3.0'
}
```

---

## 👨‍🎓 Автор
* Студент специальности 09.02.07 «Информационные системы и программирование»
* Репозиторий: [todo-app](https://github.com/Diskae/todo-app)
* Год: 2026
