# iFood Challenge

This project is an Android application built using **Kotlin** and **Jetpack Compose**. It showcases a list of favorite movies, allowing users to view details, interact with the UI, and manage their favorite movies. The app follows modern Android development practices, including MVVM architecture, dependency injection with Koin, and Jetpack libraries.

## Features

- Display a list of favorite movies distributed on 4 categories: Top rated, Upcoming, Now Playing and Popular movies
- View detailed information about each movie.
- Add or remove movies from the favorites list.
- Error handling and retry functionality.
- For Top rated, Upcoming, now playing screen and popular movies we applied Local first principle using ROOM database for caching all 4 categories locally
- Fully implemented using Jetpack Compose for UI.
- Unit and instrumented tests for ViewModel and UI.

## Tech Stack

- **Programming Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Koin
- **Networking**: Retrofit
- **Image Loading**: Coil
- **Testing**:
  - Unit Tests: JUnit, MockK
  - UI Tests: Jetpack Compose Testing, Espresso, UiAutomator

## Project Structure

- `app/src/main/java/com/brunodegan/ifood_challenge/`
  - **ui**: Contains UI components and screens.
  - **viewModel**: ViewModel classes for managing UI state.
  - **domain**: Use cases for business logic.
  - **data**: Data sources and repository implementations.
  - **base**: Common utilities and base classes.
  - **metrics**: Base metrics layer, with object to handle onEvent and onEnteredScreen created and used

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/BrunoDegan/ifood_challenge.git
   cd ifood_challenge
