# Astra Tech Posts Task

A sample Android application to browse, create, view, edit, and delete blog posts. Built as a
technical demonstration for modern Android development, applying clean architecture principles and
robust error handling with custom Retrofit networking patterns.

## Tech Stack / Dependencies

- **Languages/Frameworks:** Kotlin, Jetpack Compose
- **Networking:** Retrofit, OkHttp, Gson, Chucker (debug logging)
- **Architecture**: Clean Architecture, MVVM, UDF (Unidirectional Data Flow)
- **Dependency Injection:** Hilt Dagger
- **Image Loading:** Coil
- **Asynchronous:** Coroutines , Flow
- **Other:** Material 3, Navigation Compose

Dependencies are managed via Gradle with [version catalogs](./gradle/libs.versions.toml) and are
declared in [`app/build.gradle.kts`](./app/build.gradle.kts).

## Why three patterns in Retrofit? (Delegation, Adapter, Factory)

To ensure robust and maintainable networking, the project customizes Retrofit with three related
patterns:

- **Delegation**  
  `NetworkResponseCall` delegates the core network call logic, layering custom result and error
  handling. This decouples error handling from main business logic, improving reuse and testability.

- **Adapter**  
  `NetworkAdapter` is a Retrofit `CallAdapter` transforming responses into explicit, type-safe
  `Result<T, E>` wrappers. This centralizes network result mapping and provides a uniform interface
  for all network calls.

- **Factory**  
  `NetworkAdapterFactory` auto-selects and instantiates the correct adapters for Retrofit based on
  the type signature, enabling seamless integration of custom result wrappers in the API layer. This
  improves scalability and makes error flows easy to manage.

**Result:** All networking is explicit, robust, and errors are handled in a consistent, type-safe
way throughout the app.

## Features

- **Home Screen:**  
  Browse a list of blog posts with images and titles. Tap a post to view its details.

- **Create Blog:**  
  Create a new blog post using a form with image picker, title, and content fields.

- **Blog Details:**  
  View blog details including image, title, content, creation/update timestamps. Edit or delete
  posts directly from this screen, with confirmation dialogs and feedback.

- **Robust networking and error handling:**  
  Custom result types and navigation patterns ensure user feedback for errors (e.g., snackbars on
  failure).

- **Modern Android UI:**  
  Jetpack Compose & Material 3, with navigation and state managed via ViewModels and Flows.
