# ToDo List

Android-приложение для управления задачами с синхронизацией через Firebase Realtime Database и локальным офлайн-кэшем на Room.

## Стек

- Kotlin, Coroutines, Flow
- Jetpack Compose (Material 3)
- Firebase Realtime Database
- Room (локальный кэш)
- Hilt (Dependency Injection)
- Clean Architecture, multi-module Gradle

## Архитектура

Проект разбит на модули по слоям Clean Architecture:

```
:app                    — точка входа, DI-модули, навигация
:core:domain            — модели, UseCase'ы, порты репозиториев
:core:data              — реализация репозиториев: Firebase (remote) + Room (local)
:core:ui                — переиспользуемые Compose-компоненты
:feature:tasklist       — экран списка задач
:feature:taskdetail     — экраны создания и детализации задачи
```

Зависимости направлены строго внутрь: `:app` → `:feature:*` → `:core:data` → `:core:domain`. Модуль `:core:domain` не имеет ни одной Android-зависимости.

### Offline-first стратегия

`TaskRepositoryImpl` координирует два независимых источника данных:

- `FirebaseTaskDataSource` — слушает `ValueEventListener` и пишет изменения;
- `RoomTaskDataSource` — единственный источник данных для UI.

Firebase синхронизирует данные в Room в фоне; экраны читают исключительно из Room через `Flow`. Это даёт мгновенный отклик UI и работу списка задач в офлайне (с последующей синхронизацией при восстановлении сети).

**Известное упрощение**: синхронизация сейчас работает по принципу "затереть всё и вставить заново" (`clearAll()` + `upsertAll()`), а не diff-обновлением.

### Бизнес-правила переходов статусов

Правила ("нельзя выполнить задачу не в работе", "нельзя удалить задачу в работе", "нельзя вернуть выполненную задачу") реализованы в domain-слое через `UseCase`:
- `TakeInProgressUseCase`
- `CompleteTaskUseCase`
- `DeleteTaskUseCase`
- `CreateTaskUseCase`

Каждый UseCase возвращает `TaskActionResult` (`Success`/`Failure`) — компилятор через `when` гарантирует обработку обоих случаев на уровне ViewModel.

## Тестирование

- `:core:domain` — unit-тесты всех UseCase'ов на JUnit + `kotlinx-coroutines-test` + Turbine, включая проверку каждого запрещённого перехода статуса.
- `:feature:tasklist`, `:feature:taskdetail` — unit-тесты ViewModel с `FakeTaskRepository` (test fixtures из `:core:domain`).

Запуск тестов domain-слоя:
```
./gradlew :core:domain:test
```

Запуск всех unit-тестов:
```
./gradlew test
```

## Запуск проекта

1. Создайте проект в [Firebase Console](https://console.firebase.google.com/), добавьте Android-приложение.
2. Включите **Realtime Database** в тестовом режиме.
3. Скачайте `google-services.json` и положите в `app/` (файл не включён в репозиторий — см. `.gitignore`).
4. Откройте проект в Android Studio, дождитесь Gradle Sync, запустите на эмуляторе/устройстве.

### Правила безопасности Realtime Database

Правила открыты (`.read: true, .write: true`) — осознанное упрощение.
