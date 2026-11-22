# MyFinances

Личный трекер финансов: расходы, доходы, категории и аналитика — полностью
офлайн, без аккаунтов, серверов и сетевых разрешений.

Приложение появилось как эксперимент «а что если убрать сеть совсем»: все
данные живут только в Room на устройстве, `INTERNET`-разрешение из манифеста
удалено, облачный бэкап Android отключён (`allowBackup="false"`). Нечему
утекать — потому и спокойнее.

## Что умеет

- Расходы и доходы «за сегодня» с быстрым добавлением (FAB)
- История с фильтром по периоду и аналитикой по суммам
- Категории с эмодзи (10 предустановленных при первом запуске)
- Один счёт: название, валюта (₽ / $ / €), баланс считается из транзакций
- Свайп-удаление транзакций, валидация суммы, тёмная тема
- Адаптивная верстка: нижняя панель на телефоне, navigation rail и
  двухпанельный режим «список → транзакция» на планшетах и раскладушках

## Технологии

| Что | Чем |
|---|---|
| Язык | Kotlin 2.4 |
| UI | Jetpack Compose, Material 3 (+ adaptive layouts) |
| Навигация | Navigation 3 (type-safe `NavKey`, несколько бэкстеков на табы) |
| Данные | Room 3 (SQLite через `BundledSQLiteDriver`), KSafe для настроек |
| DI | Dagger 2 (без Hilt), convention-плагины вместо копипасты |
| Сборка | Gradle 9, AGP 9, KSP |
| Качество | ktlint, detekt, kover, 99 unit-тестов и 14 instrumented |

minSdk 26, target/compile SDK 37.

## Архитектура

Классический Clean Architecture разрез, только разбитый на Gradle-модули —
зависимости направлены строго внутрь, и компилятор следит за этим:
`domain`, `core:common` и `core:testing` — чистые Kotlin JVM-модули, им
физически недоступны ни `android.*`, ни data-слой.

```
app ──▶ feature/{expenses, income, account, history, analysis,
               categories, transaction, settings, splash}
 │              │
 │              ▼
 │           domain ◀── data/repository ──▶ data/local (Room)
 │              ▲                    ──▶ data/preferences (KSafe)
 └─▶ core/di ───┘ (Dagger-компоненты + модули)
     core/ui, core/common, core/resources, core/testing
```

- **domain** — модели, use cases, интерфейсы репозиториев, доменные правила
  (баланс = Σ доходов − Σ расходов, валидация суммы) и `DomainError`
- **data/repository** — реализации на Room/KSafe, мапперы entity ↔ model
- **feature/** — экраны на Compose + ViewModel; навигацию фичи не знают,
  экраны получают колбэки
- **app** — точка входа, Nav3-граф (`entryProvider` + `NavDisplay`),
  стейт навигации с бэкстеком на каждый таб
- **core/di** — composition root: Dagger-компоненты и модули

Доменные ошибки типизированы (`DomainError.NotFound / Storage / InvalidAmount`)
и переводятся в строки ресурсов только на границе UI.

## Сборка

Нужны JDK 17+ и Android SDK (или просто Android Studio).

```bash
./gradlew assembleDebug          # debug-сборка
./gradlew assembleRelease        # release с R8
./gradlew test                   # все unit-тесты
./gradlew connectedDebugAndroidTest   # instrumented-тесты (нужен эмулятор/девайс)
./gradlew detekt ktlintCheck     # статический анализ
./gradlew koverHtmlReport        # отчёт покрытия: build/reports/kover/html
```

`versionCode` — число коммитов в HEAD, поэтому номер растёт сам; имя APK
включает вариант, версию и хэш: `MyFinances-debug-1.0.0-<n>-<hash>.apk`.

## Структура репозитория

```
app/                    точка входа, навигация
build-logic/            Gradle convention-плагины (android, kotlin, compose, dagger, room, ktlint, detekt, kover)
core/{ui,common,di,resources,testing}/
data/{repository,local,preferences}/
domain/
feature/{9 фич}/
config/detekt/          профиль detekt
```

## Статус

Личный проект, развивается под собственные нужды. Идеи и правки — через issues/PR.
