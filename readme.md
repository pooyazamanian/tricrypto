# TradeApp Project Analysis Report

This document provides a comprehensive technical analysis of the TradeApp Android application, designed for a cryptocurrency trading platform. It covers architecture, technology stack, module implementations, and database design, serving as source material for Chapters 3 and 4 of a university final project report.

---

# SECTION 1 - PROJECT OVERVIEW

* **Project Name**: TradeApp
* **Main Purpose**: A mobile application for real-time cryptocurrency market monitoring, asset trading (buy/sell orders), and portfolio management.
* **Business Domain**: Fintech / Cryptocurrency Exchange.
* **Main Features**:
    * Real-time price tracking via WebSockets.
    * User authentication and profile management.
    * Order book management (creating and viewing buy/sell orders).
    * Wallet and asset tracking.
    * Interactive price charts.
    * Secure session management.
* **Target Users**: Individual traders and cryptocurrency enthusiasts looking for a mobile-first trading experience.

---

# SECTION 2 - TECHNOLOGY STACK

| Technology | Purpose | Usage Location |
| :--- | :--- | :--- |
| **Kotlin** | Primary programming language | Entire project |
| **Jetpack Compose** | Declarative UI framework | All screens in `ui/pages/` and `ui/tools/` |
| **MVI Architecture** | Architectural pattern for state management | `viewmodel/`, `viewmodel/state/`, `viewmodel/intent/` |
| **Hilt (Dagger)** | Dependency Injection | `damin/modul/`, `@HiltViewModel`, `@AndroidEntryPoint` |
| **Ktor Client** | Network communication (HTTP & WebSockets) | `OkxMarketRepository`, `NetworkModule` |
| **Supabase (Auth)** | User authentication and session management | `LoginViewModel`, `AuthenticationRepositoryImpl` |
| **Supabase (Postgrest)** | Database interactions (CRUD & RPC) | `SupabaseClientWrapper`, all Repositories |
| **Supabase (Realtime)**| Listening to database changes | `SupabaseClientWrapper.observeTable` |
| **Coroutines & Flow** | Asynchronous programming and data streams | Entire project, especially Repositories and ViewModels |
| **Kotlin Serialization**| JSON parsing and DTO mapping | `dto/`, `damin/modul/SupabaseModule` |
| **Compose Navigation** | In-app navigation | `ui/navigations/`, `MainActivity` |
| **Vico Charts** | Market price visualization (Candlestick/Line charts) | `ui/pages/ChartPage.kt` |
| **Coil** | Image loading (e.g., asset logos) | Used in various UI components |
| **EncryptedPrefs** | Secure storage for sensitive tokens | `SecureStorage`, `ToolsModule` |

---

# SECTION 3 - PROJECT STRUCTURE

```text
app/src/main/java/com/example/tradeapp/
├── damin/                 # Data layer (typo for "data")
│   ├── api/               # Supabase wrapper and API clients
│   ├── database/          # Local storage and secure storage
│   ├── modul/             # Hilt DI modules (Base, Network, Supabase, etc.)
│   ├── repository/        # Repository interfaces
│   └── util/              # Data layer utilities (Result class)
├── dto/                   # Data Transfer Objects for remote data
├── models/                # Domain models
├── repository/            # Repository implementations
├── ui/                    # Presentation layer (Compose UI)
│   ├── model/             # UI-specific models (e.g., UiUserAsset)
│   ├── navigations/       # Navigation graphs (BaseNav, MainPageNavigation)
│   ├── pages/             # Main screens (Home, Trade, Wallet, etc.)
│   ├── theme/             # Compose theme and styling
│   ├── tools/             # Reusable UI components (TopBar, BottomBar, Cards)
│   └── BasePage.kt        # Main scaffold for the app
├── usecase/               # Domain layer business logic (Use Cases)
├── utils/                 # General utility classes
├── viewmodel/             # ViewModels (MVI-based)
│   ├── state/             # MVI State definitions
│   ├── intent/            # MVI Intent definitions
│   ├── effect/            # MVI Side-effect definitions
│   └── util/              # ViewModel utilities
├── MainActivity.kt        # Entry point activity
└── MyApp.kt               # Hilt Application class
```

### Package Responsibilities:
* **damin (Data Layer)**: Handles external data sources. `SupabaseClientWrapper` abstracts Supabase calls, while `modul` configures Hilt.
* **dto**: Defines the contract between the app and Supabase/OKX.
* **repository (Implementation)**: Coordinates data from different sources (Supabase vs WebSocket).
* **usecase**: Contains reusable business logic, ensuring ViewModels remain lean.
* **viewmodel**: Manages UI state and handles user intents using MVI.
* **ui**: Contains all Compose-based UI elements.

---

# SECTION 4 - ARCHITECTURE ANALYSIS

The project follows a modified **Clean Architecture** with **MVI (Model-View-Intent)** in the presentation layer.

### Data Flow (MVI):
1. **Intent**: User performs an action (e.g., clicks "Buy"). The UI sends an `Intent` to the `ViewModel`.
2. **ViewModel**: Handles the intent, often calling a `UseCase`.
3. **UseCase**: Executes business logic and interacts with one or more `Repositories`.
4. **Repository**: Fetches data from `Supabase` (via `SupabaseClientWrapper`) or `OKX` (via WebSockets).
5. **State**: The `ViewModel` updates a `State` flow based on the repository result.
6. **UI**: The UI observes the `State` and re-renders accordingly.

### Architecture Diagram:

```text
       User Interface (Jetpack Compose)
               ↑              ↓
          State Flow      User Intents
               ↑              ↓
        [ Presentation Layer (ViewModels) ]
               ↑              ↓
          Invoke Use Case / Return Result
               ↑              ↓
          [ Domain Layer (Use Cases) ]
               ↑              ↓
          Call Repository / Return DTO/Model
               ↑              ↓
        [ Data Layer (Repositories) ]
         /           |             \
[ Supabase ]   [ OKX WebSocket ]   [ SecureStorage ]
(Database)      (Real-time)         (Tokens)
```

### Key Patterns:
* **Repository Pattern**: Abstracts data sources.
* **Dependency Injection**: Hilt is used for providing singleton instances of repositories, clients, and storage.
* **State Management**: `MutableStateFlow` in ViewModels ensures a single source of truth for the UI.

---

# SECTION 5 - SCREEN ANALYSIS

## 1. Splash Screen (`SplashPage.kt`)
* **Purpose**: Initial landing screen for branding and checking session status.
* **UI Components**: Logo, loading animation.
* **User Actions**: None (automated transition).
* **Navigation Destinations**: Login Page or Home Page (via `BaseNav`).

## 2. Login / Sign Up Screen (`LoginPage.kt`)
* **Purpose**: User authentication (Email/Password).
* **UI Components**: Email TextField, Password TextField, Submit Button, Toggle Mode Button (Login vs. Sign up).
* **User Actions**: Enter credentials, toggle between login/signup modes, submit.
* **Connected ViewModel**: `LoginViewModel`
* **Connected Repository**: `AuthenticationRepositoryImpl`
* **API Calls**: Supabase Auth (Sign Up / Sign In).
* **Navigation Destinations**: Base Page (on success).
* **State Objects**: `LoginState` (currentPage, isSignUpMode, authStatus, toastMessage).

## 3. Home Screen (`HomePage.kt`)
* **Purpose**: Overview of user balance, market trends, and watchlist.
* **UI Components**: Balance display, "Deposit/Withdraw" buttons, Trending Coins (LazyRow), Watchlist (LazyColumn).
* **User Actions**: Click on a coin to view chart, click deposit/withdraw (navigation to payment).
* **Connected ViewModel**: `WatchlistViewModel`, `MarketTrendsViewModel`.
* **Connected Repository**: `UserWatchlistRepositoryImp`, `MarketTrendsRepositoryImp`, `OkxMarketRepository`.
* **API Calls**: Get watchlist, Get trends, Stream real-time prices.
* **Navigation Destinations**: Chart Page, Payment Page.
* **State Objects**: `WatchlistState`, `MarketTrendsState`.

## 4. Market / Trade List Screen (`TradePage.kt`)
* **Purpose**: View open buy/sell orders and recent trade history.
* **UI Components**: Buy Orders list, Sell Orders list, Recent Trades list, Filter chips.
* **User Actions**: View order details, navigate to chart.
* **Connected ViewModel**: `TradeListViewModel`, `OrderViewModel`.
* **Connected Repository**: `TradeRepositoryImp`, `OrderRepositoryImp`.
* **API Calls**: Get user orders, Get recent trades.
* **Navigation Destinations**: Chart Page.

## 5. Chart Screen (`ChartPage.kt`)
* **Purpose**: Detailed technical analysis of a specific asset.
* **UI Components**: Candlestick Chart (Vico), Time range selectors (1D, 1W, 1M, etc.), Key stats (Open, High, Low, Volume), Buy/Sell buttons.
* **User Actions**: Change time range, refresh data, initiate buy/sell (opens bottom sheet).
* **Connected ViewModel**: `ChartViewModel`.
* **Connected Repository**: `ChartRepository`, `OkxMarketRepository`.
* **API Calls**: Get asset info, Get historical data (K-lines), Stream real-time price.
* **Navigation Destinations**: Trade Bottom Sheet.

## 6. Wallet Screen (`WalletPage.kt`)
* **Purpose**: Manage and view owned assets.
* **UI Components**: Asset list with quantities and current values.
* **User Actions**: View asset details.
* **Connected ViewModel**: `UserAssetViewModel`, `AssetViewModel`.
* **Connected Repository**: `WalletRepositoryImp`, `AssetRepositoryImp`.
* **API Calls**: Get user assets, Get asset details.

## 7. Profile Screen (`ProfilePage.kt`)
* **Purpose**: View user account information.
* **UI Components**: Profile image, Email, Phone, Full Name, National ID, Edit Profile button.
* **User Actions**: Click edit profile.
* **Connected ViewModel**: `ProfileViewModel`.
* **Connected Repository**: `ProfileRepositoryImp`.
* **API Calls**: Get user profile.
* **Navigation Destinations**: Profile Editor Page.

## 8. Profile Editor Screen (`ProfileEditorPage.kt`)
* **Purpose**: Update user account details.
* **UI Components**: Editable text fields for Name, Username, Phone, National ID.
* **User Actions**: Modify fields, click "Confirm" to save.
* **Connected ViewModel**: `ProfileEditorViewModel`.
* **Connected Repository**: `ProfileRepositoryImp`.
* **API Calls**: Upsert profile, Update user phone (Supabase Auth).

---

# SECTION 6 - NAVIGATION ANALYSIS

The application uses **Jetpack Compose Navigation** with a multi-level structure.

### Routes:
* `SPLASHSCREEN`: Initial state.
* `LOGIN`: Authentication flow.
* `BASE_PAGE`: Main application container (Scaffold + BottomBar).
    * `HOME`: Home dashboard.
    * `TRADE`: Orders and trades.
    * `WALLET`: User portfolio.
    * `PROFILE`: User account view.
    * `CHART/{assetId}`: Asset details.
    * `PROFILE_EDITOR/{profileJson}/{userJson}`: Edit profile.
    * `TOP_UP`, `PAYMENT`, `FINAL_PAYMENT`: Wallet funding flow.

### Start Destination: `SPLASHSCREEN` (handled by `BaseNav` logic).

### Navigation Flow Diagram:

```text
[SplashPage]
      ↓
(Check Session)
      ↓
[LoginPage] ←------→ [SignupPage]
      ↓
[BasePage] (Bottom Navigation)
  ├─→ [HomePage] --------→ [ChartPage] ←─┐
  ├─→ [TradePage] -------→ [ChartPage]   │
  ├─→ [WalletPage] ------→ [ChartPage]   │ (Open Bottom Sheet)
  └─→ [ProfilePage]                      │
          ↓                              │
    [ProfileEditorPage]                  │
                                         │
[TradeBottomSheet] ----------------------┘
```

---

# SECTION 7 - AUTHENTICATION ANALYSIS

The application integrates **Supabase Auth** for identity management.

### Registration Flow:
1. User enters Email and Password in `LoginPage`.
2. `LoginViewModel` calls `supabase.auth.signUpWith(Email)`.
3. Supabase sends a confirmation email.
4. After confirmation, the user can log in.

### Login Flow:
1. User enters credentials.
2. `LoginViewModel` calls `supabase.auth.signInWith(Email)`.
3. Upon success, a `Session` is returned.
4. `accessToken` and `refreshToken` are stored securely in `SecureStorage` (using `EncryptedSharedPreferences`).
5. `LoginViewModel` updates `currentPage` to `BASE_PAGE`.

### Session Management:
* **Initial Check**: `LoginViewModel.init` calls `checkSession()`.
* **Persistent Login**: Supabase SDK's `autoSaveToStorage` and `autoLoadFromStorage` are enabled in `SupabaseModule`.
* **Token Handling**: Tokens are automatically refreshed by the Supabase SDK.

### Sequence Flow:

```text
User → [LoginPage] → [LoginViewModel] → [Supabase Auth]
                                             ↓
(Success) ← [Store Tokens] ← [Return Session] ← (Verify Credentials)
    ↓
[Navigate to Home]
```

### Classes Involved:
* `LoginViewModel`: Orchestrates the UI state for auth.
* `SupabaseClient`: Main entry point for Supabase SDK.
* `SecureStorage`: Handles encrypted persistence of tokens.
* `BaseNav`: Decides which top-level screen to show based on session state.

---

# SECTION 8 - SUPABASE ANALYSIS

The application uses Supabase as its primary backend, leveraging its PostgreSQL database, Authentication, Realtime, and Storage services.

## Tables

### 1. `profiles`
* **Purpose**: Stores extended user information.
* **Columns**:
    * `id` (uuid, PK, references auth.users): Unique user ID.
    * `username` (text, unique): User's chosen display name.
    * `full_name` (text): User's real name.
    * `national_id` (text): User's identification number.
    * `created_at` (timestamptz): Creation timestamp.
    * `updated_at` (timestamptz): Last update timestamp.

### 2. `assets`
* **Purpose**: Stores information about tradable cryptocurrencies.
* **Columns**:
    * `id` (uuid, PK): Unique asset ID.
    * `name` (text): Asset name (e.g., "Bitcoin").
    * `symbol` (text): Asset ticker (e.g., "BTC").
    * `logo_url` (text): URL to asset icon.
    * `asset_link` (text): External link for more info.
    * `is_active` (boolean): Whether the asset is currently tradable.

### 3. `user_assets` (Wallet)
* **Purpose**: Tracks asset holdings for each user.
* **Columns**:
    * `id` (uuid, PK): Unique record ID.
    * `user_id` (uuid, FK references profiles): Owner of the asset.
    * `asset_id` (uuid, FK references assets): The asset being held.
    * `quantity` (numeric): Amount held.
    * `updated_at` (timestamptz): Last balance update.

### 4. `orders`
* **Purpose**: Stores buy and sell orders.
* **Columns**:
    * `id` (uuid, PK): Unique order ID.
    * `user_id` (uuid, FK references profiles): User who placed the order.
    * `asset_id` (uuid, FK references assets): Asset to trade.
    * `order_type` (text): "BUY" or "SELL".
    * `price` (numeric): Target price.
    * `quantity` (numeric): Original quantity.
    * `remaining_quantity` (numeric): Unfilled quantity.
    * `status` (text): "OPEN", "FILLED", "CANCELLED".

### 5. `trades`
* **Purpose**: Records executed transactions.
* **Columns**:
    * `id` (uuid, PK): Unique trade ID.
    * `buy_order_id` (uuid, FK): Reference to buy order.
    * `sell_order_id` (uuid, FK): Reference to sell order.
    * `asset_id` (uuid, FK): Asset traded.
    * `price` (numeric): Execution price.
    * `quantity` (numeric): Executed amount.
    * `timestamp` (timestamptz): Execution time.

## Views

* **`open_orders_view`**: Aggregates open orders with asset details (joins `orders` and `assets`).
* **`my_open_orders_view`**: Filtered version of `open_orders_view` for the authenticated user.
* **`market_trends_view`**: Aggregates market statistics for the home page.
* **`user_watchlist_with_assets`**: Joins user's watchlist items with asset metadata.

## Functions (RPC)

* **`create_order`**:
    * **Parameters**: `p_asset_id`, `p_order_type`, `p_price`, `p_quantity`.
    * **Logic**: Inserts a new order and potentially triggers a matching engine to execute trades if matching orders exist.

## Policies (RLS)

* **Profiles**: Users can only update their own profile; everyone can read public profile info (if enabled).
* **User Assets**: Users can only see and manage their own holdings (`user_id = auth.uid()`).
* **Orders**: Users can only manage their own orders (`user_id = auth.uid()`).

## ERD (ASCII)

```text
  [ auth.users ]
        | (1:1)
  [ profiles ] 1 -------- ∞ [ user_assets ]
        |                      |
        | (1:∞)                | (∞:1)
        |                      |
  [ orders ] ∞ ---------- 1 [ assets ]
        |                      |
        | (2:1)                | (∞:1)
        |                      |
  [ trades ] ∞ ---------- 1 ---/
```

---

# SECTION 9 - DOMAIN MODELS

The application separates DTOs (used for network) from Domain Models (used in business logic).

### 1. `Asset`
* **Properties**: `id`, `name`, `symbol`, `logoUrl`, `isActive`.
* **Usage**: Core entity representing a tradable cryptocurrency.

### 2. `Order`
* **Properties**: `id`, `asset`, `orderType` (Buy/Sell), `price`, `quantity`, `remainingQuantity`, `status`.
* **Usage**: Represents a user's intent to trade.

### 3. `Trade`
* **Properties**: `price`, `quantity`, `asset`, `timestamp`.
* **Usage**: Represents a completed transaction.

### 4. `MarketTrend`
* **Properties**: `asset`, `price`, `changePercent`.
* **Usage**: Data displayed on the Home screen trends list.

---

# SECTION 10 - MARKET MODULE

The Market Module is responsible for fetching and displaying real-time cryptocurrency data.

### Implementation Details:
* **Initial Load**: ViewModels fetch current trends and asset lists from Supabase views (`market_trends_view`).
* **Real-Time Updates**:
    * Uses **Ktor WebSockets** to connect to OKX exchange (`ws.okx.com`).
    * `OkxMarketRepository` manages the socket connection and subscribes to specific `instId` (e.g., "BTC-USDT").
    * Incoming JSON frames are parsed into `OkxTickerSocketResponse`.
    * A `SharedFlow` emits updated prices to the entire app.
* **UI Rendering**: ViewModels combine Supabase metadata with real-time OKX prices to show live changes on the Home and Chart pages.

### Market Workflow:
1. `MarketTrendsViewModel` calls `GetMarketTrendUseCase`.
2. Supabase returns list of trending assets.
3. ViewModel extracts symbols and calls `ObserveMarketBySymbolsUseCase`.
4. `OkxMarketRepository` connects to WebSocket and subscribes.
5. Live price updates are merged into the UI state.

---

# SECTION 11 - WALLET MODULE

The Wallet Module manages the user's holdings and portfolio value.

### Features:
* **Asset Tracking**: Fetches records from the `user_assets` table for the current `auth.uid()`.
* **Balance Calculation**:
    * Currently, the app displays individual asset quantities.
    * Total portfolio value is derived by multiplying `quantity` by the current market `price` (fetched via the Market Module).
* **Asset Integration**: Combines `UserAsset` DTOs with `AssetDto` to show logos and names alongside quantities.

### Classes:
* `UserAssetViewModel`: Loads holdings.
* `WalletRepositoryImp`: Fetches data from Supabase.
* `UiUserAsset`: UI-specific model that combines balance and asset info.

---

# SECTION 12 - BUY/SELL TRANSACTION MODULE

The transaction flow handles the creation of trade orders.

### Execution Steps:
1. **User Action**: User clicks "Buy" or "Sell" on the `ChartPage`.
2. **Input**: A bottom sheet or dialog allows entering `quantity` and `price`.
3. **Validation**: `CreateOrderViewModel` checks for empty or invalid inputs.
4. **Request**: `CreateOrderUseCase` maps the `Order` model to a `CreateOrderDto`.
5. **API Call**: `OrderRepositoryImp` calls the Supabase RPC function `create_order`.
6. **Database Update**: Supabase executes the function, inserts into `orders`, and potentially triggers matching logic.
7. **UI Refresh**: Upon success, a side-effect (`CreatingOrderEffect`) triggers a success message, and the orders list is reloaded.

### Transaction Sequence:

```text
User → [ChartPage] → [Buy/Sell Button] → [TradeBottomSheet]
                                             ↓ (Enter Details)
User ← [Success/Error Toast] ← [ViewModel] ← [OrderRepository] ← [RPC: create_order]
```

---

# SECTION 13 - ERROR HANDLING

The application uses a unified error handling strategy across all layers.

### Strategy:
* **Data Layer**: Repositories catch exceptions and wrap results in a `Result.Error` object.
* **Domain Layer**: Use cases propagate the `Result` or wrap it with domain-specific context.
* **Presentation Layer**: ViewModels observe the `Result`. If it's an error, they update the UI state with an error message and potentially trigger a side-effect (e.g., `ShowError` effect).
* **UI Layer**: Displays errors using Toasts or full-screen error views with retry buttons.

### Common Error Types:
* **Network Errors**: Handled via Ktor exception catching and `NetworkObserver`.
* **Authentication Errors**: Supabase Auth exceptions (e.g., "invalid credentials") are displayed directly to the user.
* **Validation Errors**: Empty fields in Login or Trade forms are caught before sending API requests.

---

# SECTION 14 - SECURITY ANALYSIS

Security is implemented at both the transport and storage levels.

### Security Features:
* **JWT (JSON Web Tokens)**: Supabase uses JWT for all database interactions. The `accessToken` is included in the header of every request.
* **Secure Token Storage**: Sensitive tokens (Access Token, Refresh Token) are stored using **`EncryptedSharedPreferences`** (AES256 encryption), ensuring they cannot be accessed on rooted devices or via local file inspection.
* **Transport Security**: All communication with Supabase and OKX occurs over **HTTPS/WSS** (SSL/TLS).
* **Row-Level Security (RLS)**: The Supabase database is protected by RLS policies, ensuring users can only access their own data even if they have a valid JWT.
* **Input Sanitization**: Use of prepared statements (handled by Supabase SDK) prevents SQL injection.

---

# SECTION 15 - CLASS DIAGRAM DATA

### Main ViewModels:
* `LoginViewModel`
* `MarketTrendsViewModel`
* `WatchlistViewModel`
* `ChartViewModel`
* `OrderViewModel`
* `UserAssetViewModel`
* `ProfileViewModel`
* `CreateOrderViewModel`

### Main Repositories:
* `AuthenticationRepositoryImpl`
* `MarketTrendsRepositoryImp`
* `UserWatchlistRepositoryImp`
* `OkxMarketRepository`
* `ChartRepository`
* `OrderRepositoryImp`
* `WalletRepositoryImp`
* `ProfileRepositoryImp`
* `TradeRepositoryImp`

### Main Models & DTOs:
* `Asset`, `AssetDto`
* `Order`, `OpenOrderDto`, `CreateOrderDto`
* `Trade`, `TradeDto`
* `Profile`
* `UserAsset`
* `CryptoTicker`

### Relationships:
```text
ViewModel → UseCase → Repository → SupabaseClientWrapper / HttpClient
```

---

# SECTION 16 - USE CASES

| Use Case | Actor | Preconditions | Main Flow | Postconditions |
| :--- | :--- | :--- | :--- | :--- |
| **Login** | User | App opened | Enter email/pass → Submit → Verify | Session started |
| **Register** | User | No account | Enter data → Confirm email → Login | Profile created |
| **View Market** | User | Logged in | Open Home → Observe WebSocket prices | Real-time trends visible |
| **View Chart** | User | Logged in | Click Asset → Load History + Live Price | Technical chart displayed |
| **Place Order** | User | Logged in | Enter Price/Qty → Submit RPC | Order added to DB |
| **View Wallet** | User | Logged in | Open Wallet → Load UserAssets | Portfolio visible |
| **Update Profile**| User | Logged in | Edit fields → Upsert to Supabase | Profile updated |

---

# SECTION 17 - DATABASE SUMMARY

* **Total Tables**: 5 (`profiles`, `assets`, `user_assets`, `orders`, `trades`)
* **Total Views**: 4 (`open_orders_view`, `my_open_orders_view`, `market_trends_view`, `user_watchlist_with_assets`)
* **Key Functions**: `create_order` (RPC)
* **Auth Integration**: Integrated with `auth.users` via UUID foreign keys.
* **Realtime Support**: Enabled for `trades` and `orders` tables.

---

# SECTION 18 - FINAL CHAPTER 3 SUPPORT (Analysis & Design)

## Functional Requirements
1. The system must allow users to register and log in securely.
2. The system must fetch real-time cryptocurrency prices from an external exchange.
3. The system must allow users to view historical price charts for various assets.
4. The system must support creating buy and sell orders.
5. The system must display the user's current holdings and portfolio value.
6. The system must allow users to update their personal profile information.

## Non-Functional Requirements
1. **Performance**: Real-time price updates must have a latency of less than 2 seconds.
2. **Security**: All sensitive data must be encrypted during transport and at rest.
3. **Availability**: The application should handle intermittent network connectivity gracefully.
4. **Scalability**: The database design should support a growing number of assets and users.

## System Architecture (Design Phase)
* **Client-Server Model**: Android app as the client, Supabase as the serverless backend.
* **Component-Based UI**: Using Jetpack Compose for modular UI design.
* **Layered Responsibility**: Clear separation between UI, Business Logic, and Data sources.

---

# SECTION 19 - FINAL CHAPTER 4 SUPPORT (Implementation)

## Implementation Highlights

### 1. Real-time Market Data
Implemented using **Ktor WebSockets** to maintain a persistent connection with the OKX exchange. Data is streamed into a `SharedFlow` and consumed by multiple ViewModels simultaneously.

### 2. State-Driven UI (MVI)
Each screen is driven by a single `State` object. User interactions are dispatched as `Intents`, resulting in state updates. This ensures predictable UI behavior and easier debugging.

### 3. Secure Session Persistence
Leveraged **EncryptedSharedPreferences** for storing JWT tokens. This implementation uses the AES256-GCM encryption scheme provided by the Android Jetpack Security library (wrapped by `dev.spght:encryptedprefs`).

### 4. Supabase Integration
Extensive use of Supabase Postgrest for data retrieval and RPC for complex operations like order creation. Real-time database changes are observed using Supabase Realtime (PostgreSQL replication).

### 5. Charts Implementation
Integrated the **Vico Chart Library** to render technical candlestick charts. Custom logic in `ChartViewModel` handles time range switching and data normalization for the chart producer.

### 6. Dependency Injection
**Hilt** is used to inject repositories and use cases into ViewModels, following the `Singleton` pattern for network clients and database wrappers to optimize resource usage.
