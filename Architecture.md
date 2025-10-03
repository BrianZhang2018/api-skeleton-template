# Architecture

## 🏗️ Tiers

```
┌─────────────────────────────────────────────────────────────┐
│                    REST Layer (JAX-RS)                      │
│  • UserResource, HealthResource                            │
│  • JWT Authentication Filter                               │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│                   Business Logic Layer                      │
│  • UserService, ConfigService                              │
│  • Caching (Caffeine)                                      │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│                    Data Access Layer                        │
│  • UserDao (JDBI)                                          │
│  • PostgreSQL Database                                     │
└─────────────────────────────────────────────────────────────┘
```

## Framework Design: Dependency Injection with Guice and Jersey

This project uses a powerful pattern to integrate the **Google Guice** dependency injection framework with the **Jersey** JAX-RS (web) framework. This allows for clean, modular, and testable code.

### Core Components

1.  **Guice (`MainModule`, `JwtModule`, etc.)**: The DI framework. Its job is to be the "factory" for the application. It knows how to create all objects (services, filters, DAOs) and wire them together by injecting their dependencies.

2.  **Jersey (`ResourceConfig`)**: The JAX-RS framework. Its job is to handle web requests. It needs instances of resources (e.g., `UserResource`) and providers (e.g., `JwtAuthFilter`) to do its work.

3.  **`JerseyConfig.java` (The Bridge)**: This class is the critical link between Guice and Jersey. When Jersey starts, it uses `JerseyConfig`. `JerseyConfig` then asks the Guice `Injector` for all the prepared JAX-RS components and registers them with Jersey.

### The Custom Binder Pattern

To make the integration seamless, we use a custom binder utility:

- **`@JaxrsComponent`**: A marker annotation. It doesn't do anything on its own, but it serves as a unique tag to identify all classes that need to be registered with Jersey.

- **`JaxrsComponentBinder`**: A helper class that adds components to a special collection within Guice, tagging the entire collection with `@JaxrsComponent`.

### How It Works

When you see this code in a module:

```java
jaxrsComponentBinder(binder()).bind(JwtAuthFilter.class);
```

- It tells Guice to manage `JwtAuthFilter`.
- It adds `JwtAuthFilter` to the special set of objects marked with `@JaxrsComponent`.

Later, `JerseyConfig` asks Guice for this exact set and registers each component with Jersey, effectively "injecting" Guice-managed objects into the Jersey framework.

### Why This Pattern is Used

- **Modularity**: Features can be bundled into self-contained modules (like `JwtModule`). To disable JWT authentication, you would only need to comment out `install(new JwtModule())` in `MainModule`.
- **Centralized Configuration**: Guice becomes the single source of truth for object creation and wiring.
- **Testability**: It's easy to substitute mock objects for testing purposes.
