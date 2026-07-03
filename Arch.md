# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

### Building the Project
```bash
./gradlew build                    # Build all variants
./gradlew assembleUatDebug         # Build UAT debug APK
./gradlew assembleBvtDebug         # Build BVT debug APK
./gradlew assembleProductionRelease # Build production release APK (requires signing)
./gradlew installDebug             # Install debug build to connected device
./gradlew clean                    # Clean build artifacts
```

### Testing
```bash
./gradlew testUatDebugUnitTest     # Run unit tests for UAT variant
./gradlew connectedUatDebugAndroidTest  # Run instrumented tests
./gradlew :app:testDebugUnitTest   # Run unit tests for app module only
./gradlew :<module>:testDebugUnitTest  # Run tests for specific module
```

### Code Quality
```bash
./gradlew koverReport              # Generate code coverage report (Kover)
./gradlew sonarqube                # Run SonarQube analysis (requires server config)
```

Lint is configured in each module but does not abort on error (`lint { abortOnError = false }`)

### Module-Specific Tasks
```bash
./gradlew :products:msp:build      # Build specific module
./gradlew :features:auth:test      # Test specific feature module
```

## Architecture

### Module Structure
This is a multi-module Android project following a feature-based architecture with Clean Architecture principles and MVVM UI pattern.

**Core Modules** (shared infrastructure):
- `core:common` - Utilities, extensions, constants, base classes
- `core:model` - Domain models, entities, API request/response models, enums
- `core:network` - Retrofit/OkHttp networking layer, API services, interceptors
- `core:data` - Repository implementations, data sources (local/remote)
- `core:database` - Realm database configuration, migrations, DAOs

**Feature Modules** (independent vertical slices):
- `features:auth` - Authentication and authorization
- `features:fna` - Financial Needs Analysis
- `features:landingpage` - Landing/onboarding screens
- `features:profile` - User profile management
- `features:myleads` - Lead management and synchronization
- `features:syncfile` - File synchronization
- `features:documentstorage` - Document management

**Product Modules**:
- `products:msp` - Mandiri Wealth Signature (MWS) product

**Supporting Modules**:
- `app` - Main application module (UI, navigation, feature composition)
- `shared` - Shared UI components, activities, fragments, adapters, base classes
- `styling` - Theme, colors, and reusable UI components
- `appconfig` - Feature toggle and remote configuration (with no-op variant)
- `testing` - Test utilities and shared test configurations

### Build Logic
Custom Gradle conventions in `build-logic/convention/`:
- `ApplicationConvention` - Applied to app module
- `LibraryConvention` - Applied to feature/core/product modules
- Shared configuration via `internal/` package for flavors, Kotlin, Android setup

### Build Flavors
Three product flavors with different environments:
- `uat` - User Acceptance Testing (version name suffix: `[UAT]`)
- `bvt` - Build Verification Testing (version name suffix: `[BVT]`)
- `production` - Production environment

Each flavor requires its own `google-services.json` in `src/<flavor>/` directory. Flavors are configured via `sharedFlavorConfiguration` in build-logic.

### Key Technologies
- **Dependency Injection**: Koin 3.2.4
- **Networking**: Retrofit 2.9.0 + OkHttp 4.10.0
- **Database**: Realm 10.19.0 (primary), Room 2.4.3 with SQLCipher (secondary)
- **Async**: RxJava2
- **UI**: Material Design, Navigation Component, Data Binding
- **Image Loading**: Glide
- **Payment**: Midtrans
- **PDF**: iText, PDFBox, Barteksc PDF Viewer
- **Analytics**: Firebase (Analytics, Crashlytics, Messaging), Microsoft AppCenter
- **Monitoring**: Dynatrace
- **Code Coverage**: Kover

### Architecture Patterns

**BaseViewModel** (`shared` module): All ViewModels extend this base class which provides:
- Loading state management (`showLoadingLiveData`, `showPSLoadingLiveData`)
- Error handling with custom exceptions (`PSException` hierarchy: `SessionExpiredException`, `UnauthorizedException`, `NotFoundException`, `InternalServerErrorException`, `UnknownException`)
- Toast notifications (`showToastLiveData`)
- Token expiration handling (`tokenExpiredLiveData`) - auto-triggers for 403 responses
- `launchWithErrorHandling()` extension function for automatic session expiration detection on Flow<Status<T>> responses

**Repository Pattern**: Data layer abstracts data sources behind repository interfaces. Repositories are injected via Koin.

**Resource Pattern**: API responses use a `Resource<Status, Data>` wrapper pattern for consistent state representation.

**Feature Structure**: Each feature module follows this pattern:
```
features/<name>/
├── di/           # Koin DI modules
├── domain/       # Use cases, repository interfaces
├── data/         # Repository implementations, data sources
└── view/         # Activities, fragments, adapters
```

### Proposal System Architecture
The proposal system is designed for multiple insurance products with a standardized flow:

1. **Base Proposal Flow** (`app/src/main/java/.../view/proposal/base/`)
   - `FragmentProposalBase` - Orchestrates multi-step proposal creation
   - `FragmentInput*` fragments - Collect inputs (value, pay mode, pay period, coverage)
   - `FragmentRingkasanBase` + `RingkasanBaseViewModel` - Summary and validation

2. **Product Configuration** (`app/src/main/java/.../view/proposal/base/config/`)
   - `ProposalConfigInterfaces` - Define contracts for calculation, validation, data, UI
   - `AbstractProposalConfig` - Base implementation
   - `ProposalConfigFactory` - Selects product config based on product code

3. **Product Implementation Pattern** (example: MWS in `products/msp/`)
   - `<Product>Calculator.kt` - Core financial calculations
   - `<Product>ProposalConfigCalculator.kt` - Adapter layer for UI
   - `<Product>ProposalConfigValidator.kt` - Business rules and constraints
   - `<Product>ProposalConfigDataProvider.kt` - Default values and options
   - `<Product>ProposalConfig.kt` - Wires everything together

4. **Document Generation** (`app/src/main/java/.../utils/document/`)
   - `DocumentBuilder<Product>.kt` - Proposal illustration PDFs
   - `DocumentBuilderSpaj_*.java` - SPAJ form generation
   - `JSONtoString<Product>.kt` - JSON payload generation

### PDF Viewer System
Refactored from Java to Kotlin with MVVM pattern (see `app/src/main/java/.../utils/pdf/README.md`):
- `PdfType.kt` - Sealed class hierarchy for type-safe PDF handling
- `PdfViewerState.kt` - UI state management with sealed classes
- `PdfRepository.kt` - Data layer for PDF operations (load, encode, cache)
- `PdfViewerViewModel.kt` - Business logic with coroutines
- `PdfViewerActivity.kt` - UI layer with ViewBinding

### Dependency Injection
Koin modules are organized per feature and layer:
- Each feature/core module has its own `*Module.kt` in `di/` package
- `AppModule.kt` in app module loads all feature modules (`appModule` list)
- Scoped modules for singletons vs viewmodels

### Database
- **Realm** is the primary database (configured in `core:database`)
- `QueryRealm.java` - Centralized query operations
- `MyMigration.java` - Schema migrations
- `PerfectSolutionRealm.kt` - Realm configuration
- Room 2.4.3 with SQLCipher is also used as secondary database

### Key Patterns & Utilities
- **Flavor-aware versioning**: UAT/BVT flavors have version name suffixes
- **Chucker**: HTTP debugging in debug builds (no-op variant in release)
- **Hawk**: Encrypted key-value storage
- **Digital signatures**, **audio recording**, and **PDF generation** supported
- Multiple API service classes exist for different backend services (B2B, Backend, Eskdr, Stream)

### Adding New Products
Follow the MWS template in `products/msp`:
1. Create `products/<code>` module
2. Implement calculator, validator, data provider, and config classes
3. Register in `ProposalConfigFactory`
4. Add document builders
5. Update Realm schema if needed
6. Add constants to `BasicProductConst.kt`

See `docs/mws/MWS_Development_Guide.md` for detailed implementation steps.

## Important Notes

- **Namespace**: Main app uses `com.example.mygiftproject` (placeholder namespace)
- **Realm**: Requires proper migration handling when modifying schemas
- **PDF Assets**: Use synced assets instead of bundling PDFs in `app/src/main/assets/`
- **Coverage reports**: Generated via Kover; excludes Fragments/Activities/databinding by default (configured in `app/build.gradle.kts`)
- **Midtrans**: Currently configured for production (check `app/build.gradle.kts` for UAT/production switching)
- **Security**: Keystore and signing config in `app/build.gradle.kts` (DO NOT commit real credentials)
- **Session Expiration**: Automatically handled via `BaseViewModel.launchWithErrorHandling()` - triggers `tokenExpiredLiveData` for 403 responses
- **Git Push Policy**: Claude Code MUST NEVER push to remote git. All git operations (add, commit, branch) should remain local. Only human developers should push to remote after review.

## Coding Standards

### Language Policy
- **Kotlin-first** - All new code MUST be written in Kotlin
- **Java interop** - Only acceptable when modifying existing Java files; document any new Kotlin classes exposed to Java with `@JvmFile` or `@JvmStatic` as needed
- **Coroutines preferred** - Use `kotlinx.coroutines` for async operations. Only use RxJava2 when modifying existing reactive streams or integrating with RxJava dependencies

### Code Style
- Follow Android Kotlin style guide
- Use `data class` for models, `sealed class` for state hierarchies
- Prefer `val` over `var`, `?` nullable types explicitly declared
- Extension functions over utility classes
- No hardcoded strings (use `string resources` or constants)

### Koin DI Best Practices
- Use `singleOf(::Class)` for singletons (repositories, data sources)
- Use `factory` for stateful or one-time instances
- Use `viewModelOf()` for ViewModels
- Use `bind<Interface>()` when constructor takes interface
- One module per feature layer; organize by dependency direction

### Naming Conventions
- Features: `features:<name>` (lowercase, descriptive)
- ViewModels: `<Screen>ViewModel`
- Repositories: `<Entity>Repository` (interface), `<Entity>RepositoryImpl` (implementation)
- Use cases: `<Action>UseCase`
- Branches: `feat/PSNV-*`, `fix/PSNB-*`, `chore/*`, `product/*`

## Workflow Processes

### Branch Naming Convention
```
feat/PSNV-<ticket>-description     # New features
fix/PSNB-<ticket>-description      # Bug fixes
chore/<description>                # Maintenance, dependencies, refactoring
product/<product-code>-description  # Product-specific changes
release/<version>                  # Release branches (e.g., release/3.6.0)
```

- Jira ticket numbers (`PSNV-*`, `PSNB-*`) REQUIRED for feature/fix branches
- Use kebab-case for descriptions (e.g., `fix/PSNB-159-address-type`)
- **Branches are kept after merge** - post-merge fixes continue on the same branch
- Reuse existing branches for follow-up work instead of creating new ones

### Branch Lifecycle
1. Create branch from `dev/*` (e.g., `dev/3.6.0`)
2. Complete work and merge to `dev/*`
3. **Branch remains active** for any post-merge fixes
4. Only create new branch when starting different ticket/work

### Commit Message Standards
Follow conventional commit format:
```
<type>: <concise description>

(Optional detailed body)

Refs: <ticket-number>
```

**Types:** `feat`, `fix`, `chore`, `refactor`, `docs`, `test`, `perf`

**Examples:**
```
feat: add IMEI parameter to login API

- Add getDeviceId() utility to shared module
- Pass IMEI through LoginActivity to LoginRepository
- Update backend API contract

Refs: PSNV-2144
```

```
fix: handling error sttd file not found

Add try-catch for DocumentBuilder when STTD
template is missing from assets.

Refs: PSNV-2108
```

### Pull Request Process (Future Implementation)
- PRs required for all `dev/*` and `master` merges
- Title MUST include ticket number
- Description should include: what changed, why, testing done
- Minimum 1 approver (code owner or senior dev)
- Resolve all review comments before merge

### Version Management
- Version code/name managed in `app/build.gradle.kts`
- Update both `psVersionCode` and `psVersionName` for releases
- UAT/BVT suffixes added automatically via `sharedFlavorConfiguration`

## Mobile Best Practices

### Performance Guidelines

**Memory Management**
- Always unsubscribe/cancel coroutines in `onDestroy()` using `viewModelScope` or `lifecycleScope`
- Use `WeakReference` for Activity/Context in long-running operations
- Avoid bitmap memory leaks: recycle Glide requests in `onDestroy()`
- Profile with Android Profiler before optimizing

**ANR Prevention**
- No network/database calls on main thread (use coroutines with `Dispatchers.IO`)
- Keep main thread work under 16ms (60fps target)
- Lazy load heavy operations (PDF generation, large queries)
- Use `view.post { }` for non-critical UI updates

**Startup Performance**
- Initialize Koin modules lazily where possible (`createdAtStart = false`)
- Defer non-critical operations to `Application.onIdle()` or background
- Avoid static initialization blocks in `shared` module

### Architecture Patterns

**Activity Base Classes** (`shared` module)
- **Compose screens**: Extend `BaseComposeActivity` - includes auto-logoff session management
- **ViewBinding screens**: Extend `BaseBindingActivity<VB>` - handles binding lifecycle automatically
- Both extend `BaseActivity` which provides session management and common activity behaviors

**Fragment Base Classes** (`shared` module)
- **ViewBinding fragments**: Extend `BaseBindingFragment<VB, VM, MA>` - provides:
  - Automatic ViewBinding inflation and cleanup
  - ViewModel integration with Koin
  - Type-safe activity reference
  - Binding set to null in `onDestroy()` to prevent memory leaks

**ViewModel Implementation**
- ALWAYS extend `BaseViewModel` for consistent error handling
- Use `launchWithErrorHandling()` for repository calls to auto-detect session expiration
- Expose `LiveData` for UI observations, not `Flow` directly (keep UI simple)
- Keep ViewModels stateless - no Activity/Context references

**Repository Pattern**
- Return `Flow<Status<T>>` from repository for consistent state representation
- Handle errors in repository, return `Status.Error` with appropriate `PSException`
- Use `RetryDataSource` wrapper for retryable network operations
- Single source of truth: prefer database over network for cached data

**Fragment/Activity Best Practices**
- ViewBinding is handled by base classes - use `binding` directly
- Observe LiveData in `onViewCreated()`, remove observers in `onDestroyView()`
- Handle configuration changes with `ViewModel` - don't rely on savedState for critical data
- Base classes handle binding lifecycle - focus on business logic

### Security Considerations

**Data Storage**
- Use Hawk for encrypted key-value storage (tokens, sensitive prefs)
- Realm database: ensure files are not exported via manifest
- Never log sensitive data (tokens, passwords, IMEI, customer data)

**Network Security**
- All API calls through Retrofit with OkHttp interceptors
- Use HTTPS only (production forces cleartext traffic disabled)
- Token refresh handled automatically via session expiration flow

**Knox SDK Integration**
- App distributed via Samsung Knox - maintain Knox eligibility compliance
- IMEI sent to backend for device validation - ensure proper permission handling
- Follow Knox SDK guidelines for background operations

### Testing Guidelines

**Unit Tests**
- Test ViewModels with JUnit + Mockk (no Android dependencies)
- Test repositories with in-memory/fake data sources
- Test use cases independently
- Target: new business logic should have unit tests

**Instrumented Tests**
- Use for database migrations, Realm operations
- Test critical UI flows (login, proposal submission)
- Keep tests fast - avoid unnecessary UI interactions
