# 📱 Android Java Development & Architecture Guide
## Integration with FindIt Campus Lost & Found Backend

This guide outlines a professional, robust, and clean **MVVM (Model-View-ViewModel)** and **Repository Pattern** architecture implemented in **Java** for your Android client app. It is configured to run seamlessly against your local Spring Boot backend (`http://10.0.2.2:8080`) through the Android Emulator.

---

## 🛠️ Part 1: Android Studio Project Setup

Before writing Java code, you must configure your Android project to support networking and allow communication with a local unencrypted (`http://`) backend.

### 1. Network Permissions & Cleartext Configuration
By default, Android 9 (API Level 28) and higher blocks unencrypted HTTP traffic. Since your local Spring Boot server runs on `http://localhost:8080`, you must explicitly configure Android to allow HTTP connections to `10.0.2.2` (the special IP mapping to the host's localhost inside the emulator).

#### Create `res/xml/network_security_config.xml`
In your Android Studio project, create a file at `app/src/main/res/xml/network_security_config.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <!-- 10.0.2.2 is the emulator's alias to your computer's localhost -->
        <domain includeSubdomains="true">10.0.2.2</domain>
        <domain includeSubdomains="true">localhost</domain>
    </domain-config>
</network-security-config>
```

#### Update `AndroidManifest.xml`
Add the Internet permission and link your network security configuration:
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.findit">

    <!-- 🌐 Add Internet Permission -->
    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.AppCompat.Light.DarkActionBar"
        
        <!-- 🔒 Reference Security Config -->
        android:networkSecurityConfig="@xml/network_security_config">
        
        <!-- Login Screen -->
        <activity
            android:name=".ui.LoginActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- Dashboard Screen -->
        <activity android:name=".ui.DashboardActivity" android:exported="false" />

        <!-- Reporting Screen -->
        <activity android:name=".ui.ReportItemActivity" android:exported="false" />

        <!-- Items Browser Screen -->
        <activity android:name=".ui.ItemListActivity" android:exported="false" />

        <!-- Match Finder Screen -->
        <activity android:name=".ui.MatchListActivity" android:exported="false" />
        
    </application>
</manifest>
```

### 2. Gradle Dependencies (`app/build.gradle`)
Add the dependencies for Retrofit, GSON converter, OkHttp Logging Interceptor, and Lifecycles (LiveData + ViewModel) under the `dependencies` block of your app-level `build.gradle` file:

```groovy
dependencies {
    // UI components
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.9.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.cardview:cardview:1.0.0'
    implementation 'androidx.recyclerview:recyclerview:1.3.0'

    // 📡 Retrofit & GSON Converter
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    
    // 🪵 OkHttp Logger for Debugging API calls in Logcat
    implementation 'com.squareup.okhttp3:logging-interceptor:4.11.0'

    // 🧠 Jetpack Lifecycle (ViewModel & LiveData)
    implementation 'androidx.lifecycle:lifecycle-viewmodel:2.6.2'
    implementation 'androidx.lifecycle:lifecycle-livedata:2.6.2'
    implementation 'androidx.lifecycle:lifecycle-common-java8:2.6.2'
}
```

---

## 📐 Part 2: MVVM Architecture Flow

This clean separation ensures your UI handles presentation, while the ViewModels preserve state, Repositories orchestrate data, and Retrofit manages networking.

```
 +-------------------------------------------------------------------------------+
 |                                  VIEW LAYER                                   |
 |  [LoginActivity.java]  <------------------->  [activity_login.xml]            |
 +-----------------------+-------------------------------------------------------+
                         | (observes LiveData & binds UI events)
                         v
 +-------------------------------------------------------------------------------+
 |                               VIEWMODEL LAYER                                 |
 |  [AuthViewModel.java] (Holds screen state & exposes LiveData)                 |
 +-----------------------+-------------------------------------------------------+
                         | (triggers web requests & fetches data)
                         v
 +-------------------------------------------------------------------------------+
 |                              REPOSITORY LAYER                                 |
 |  [AuthRepository.java] (Aggregates remote and local data sources)              |
 +-----------------------+-------------------------------------------------------+
                         | (makes Network API calls via Retrofit)
                         v
 +-------------------------------------------------------------------------------+
 |                                NETWORK LAYER                                  |
 |  [RetrofitClient.java] (HTTP Client & JWT Auth Interceptor)                   |
 |  [ApiService.java]     (Declares REST Endpoints & Java Data DTOs)             |
 +-------------------------------------------------------------------------------+
```

---

## 🗂️ Part 3: Android App Package Structure (Java)

Create the following package structure inside `app/src/main/java/com/example/findit/`:

```
com.example.findit/
├── data/
│   ├── models/
│   │   ├── AuthResponse.java
│   │   ├── Category.java
│   │   ├── Item.java
│   │   ├── ItemRequest.java
│   │   ├── Location.java
│   │   ├── LoginRequest.java
│   │   ├── MatchResponse.java
│   │   └── RegisterRequest.java
│   ├── network/
│   │   ├── ApiService.java
│   │   └── RetrofitClient.java
│   └── repository/
│       ├── AuthRepository.java
│       ├── ItemRepository.java
│       └── Resource.java
├── ui/
│   ├── AuthViewModel.java
│   ├── DashboardActivity.java
│   ├── ItemAdapter.java
│   ├── ItemListActivity.java
│   ├── ItemViewModel.java
│   ├── LoginActivity.java
│   ├── MatchAdapter.java
│   ├── MatchListActivity.java
│   └── ReportItemActivity.java
└── utils/
    └── SessionManager.java
```

---

## 💻 Part 4: Complete Java Code Implementations

Below is the production-ready code designed to work out-of-the-box with your Spring Boot models and Security configuration.

### 1. Data Layer: Models & DTOs

#### `data/models/Category.java`
```java
package com.example.findit.data.models;

public enum Category {
    ELECTRONICS("Electronics"),
    BOOKS("Books & Notes"),
    CLOTHING("Clothing"),
    ACCESSORIES("Accessories"),
    ID_CARDS("ID Cards"),
    KEYS("Keys"),
    BAGS("Bags & Backpacks"),
    DOCUMENTS("Documents"),
    STATIONERY("Stationery"),
    OTHER("Other");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
```

#### `data/models/Location.java`
```java
package com.example.findit.data.models;

public class Location {
    private String building;
    private String floor;
    private String campus;

    public Location() {}

    public Location(String building, String floor, String campus) {
        this.building = building;
        this.floor = floor;
        this.campus = campus;
    }

    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }

    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }

    public String getCampus() { return campus; }
    public void setCampus(String campus) { this.campus = campus; }

    @Override
    public String toString() {
        return building + ", Floor " + floor + ", " + campus;
    }
}
```

#### `data/models/LoginRequest.java`
```java
package com.example.findit.data.models;

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
```

#### `data/models/RegisterRequest.java`
```java
package com.example.findit.data.models;

public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String rollNumber;
    private String department;

    public RegisterRequest(String fullName, String email, String password, String phone, String rollNumber, String department) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.rollNumber = rollNumber;
        this.department = department;
    }

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}
```

#### `data/models/AuthResponse.java`
```java
package com.example.findit.data.models;

public class AuthResponse {
    private String token;
    private Long userId;
    private String email;
    private String fullName;
    private String role;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
```

#### `data/models/ItemRequest.java`
```java
package com.example.findit.data.models;

public class ItemRequest {
    private String title;
    private String description;
    private Category category;
    private Location location;
    private String incidentDate; // Format: "YYYY-MM-DD"
    private String imageUrl;
    private String type; // "LOST" or "FOUND"

    public ItemRequest(String title, String description, Category category, Location location, String incidentDate, String imageUrl, String type) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.incidentDate = incidentDate;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public String getIncidentDate() { return incidentDate; }
    public void setIncidentDate(String incidentDate) { this.incidentDate = incidentDate; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
```

#### `data/models/Item.java`
```java
package com.example.findit.data.models;

public class Item {
    private Long id;
    private String title;
    private String description;
    private Category category;
    private Location location;
    private String incidentDate;
    private String imageUrl;
    private boolean resolved; // for lost items
    private boolean claimed;  // for found items

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public String getIncidentDate() { return incidentDate; }
    public void setIncidentDate(String incidentDate) { this.incidentDate = incidentDate; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public boolean isResolved() { return resolved; }
    public void setResolved(boolean resolved) { this.resolved = resolved; }
    public boolean isClaimed() { return claimed; }
    public void setClaimed(boolean claimed) { this.claimed = claimed; }
}
```

#### `data/models/MatchResponse.java`
```java
package com.example.findit.data.models;

public class MatchResponse {
    private Long lostItemId;
    private Long foundItemId;
    private String foundTitle;
    private String foundDescription;
    private String foundLocation;
    private double score;
    private String matchLevel; // "STRONG", "MODERATE", "WEAK"

    // Getters and Setters
    public Long getLostItemId() { return lostItemId; }
    public void setLostItemId(Long lostItemId) { this.lostItemId = lostItemId; }
    public Long getFoundItemId() { return foundItemId; }
    public void setFoundItemId(Long foundItemId) { this.foundItemId = foundItemId; }
    public String getFoundTitle() { return foundTitle; }
    public void setFoundTitle(String foundTitle) { this.foundTitle = foundTitle; }
    public String getFoundDescription() { return foundDescription; }
    public void setFoundDescription(String foundDescription) { this.foundDescription = foundDescription; }
    public String getFoundLocation() { return foundLocation; }
    public void setFoundLocation(String foundLocation) { this.foundLocation = foundLocation; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public String getMatchLevel() { return matchLevel; }
    public void setMatchLevel(String matchLevel) { this.matchLevel = matchLevel; }
}
```

---

### 2. Utilities: Local Storage & Session Manager

#### `utils/SessionManager.java`
This singleton manages securely writing and retrieving the JWT Authentication token and authenticated user metadata to local device storage.

```java
package com.example.findit.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "FindItSession";
    private static final String KEY_JWT_TOKEN = "jwt_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_NAME = "user_fullname";
    
    private static SessionManager instance;
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    private SessionManager(Context context) {
        pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    public void saveSession(String token, Long userId, String email, String fullName) {
        editor.putString(KEY_JWT_TOKEN, token);
        editor.putLong(KEY_USER_ID, userId);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_NAME, fullName);
        editor.apply();
    }

    public String getToken() {
        return pref.getString(KEY_JWT_TOKEN, null);
    }

    public Long getUserId() {
        return pref.getLong(KEY_USER_ID, -1);
    }

    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "");
    }

    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, "");
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
```

---

### 3. Network Layer: Retrofit Client & Endpoints

#### `data/network/RetrofitClient.java`
Configures Retrofit. Automatically reads the JWT Token from `SessionManager` and appends it to any requests that require Authentication (excluding the `/api/auth/` routes).

```java
package com.example.findit.data.network;

import android.content.Context;
import com.example.findit.utils.SessionManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // 💡 10.0.2.2 is the loopback alias to connect to the computer's localhost:8080
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static Retrofit retrofit = null;

    public static synchronized Retrofit getClient(Context context) {
        if (retrofit == null) {
            
            // 🪵 Http Logger
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // 🔐 Authorization Interceptor to inject Bearer Token on secure requests
            Interceptor authInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    String path = originalRequest.url().encodedPath();

                    // If the request is for Login or Register, do not attach the JWT
                    if (path.contains("/api/auth/")) {
                        return chain.proceed(originalRequest);
                    }

                    // For secured APIs, append "Authorization: Bearer <token>"
                    String token = SessionManager.getInstance(context).getToken();
                    if (token != null) {
                        Request authenticatedRequest = originalRequest.newBuilder()
                                .header("Authorization", "Bearer " + token)
                                .build();
                        return chain.proceed(authenticatedRequest);
                    }

                    return chain.proceed(originalRequest);
                }
            };

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(authInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
```

#### `data/network/ApiService.java`
```java
package com.example.findit.data.network;

import com.example.findit.data.models.*;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // ==================== AUTH ====================
    @POST("api/auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);

    @POST("api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    // ==================== ITEMS ====================
    @POST("api/items/report/{userId}")
    Call<Item> reportItem(@Path("userId") Long userId, @Body ItemRequest request);

    @GET("api/items/lost")
    Call<List<Item>> getAllLostItems();

    @GET("api/items/found")
    Call<List<Item>> getAllFoundItems();

    @GET("api/items/lost/{id}")
    Call<Item> getLostItem(@Path("id") Long id);

    @GET("api/items/found/{id}")
    Call<Item> getFoundItem(@Path("id") Long id);

    @PUT("api/items/lost/{id}/resolve")
    Call<Item> resolveLostItem(@Path("id") Long id);

    @PUT("api/items/found/{id}/claim")
    Call<Item> claimFoundItem(@Path("id") Long id);

    @DELETE("api/items/lost/{id}")
    Call<Map<String, String>> deleteLostItem(@Path("id") Long id);

    @DELETE("api/items/found/{id}")
    Call<Map<String, String>> deleteFoundItem(@Path("id") Long id);

    // ==================== MATCHES ====================
    @GET("api/matches/lost/{lostId}")
    Call<List<MatchResponse>> getMatchesForLost(
            @Path("lostId") Long lostId,
            @Query("threshold") double threshold
    );
}
```

---

### 4. Repository Layer (Handles Data Operations & Callbacks)

#### `data/repository/Resource.java`
This generic wrapper class safely exposes the status (SUCCESS, ERROR, LOADING) of web actions to your UI controllers.

```java
package com.example.findit.data.repository;

public class Resource<T> {
    public enum Status { SUCCESS, ERROR, LOADING }

    public final Status status;
    public final T data;
    public final String message;

    private Resource(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg) {
        return new Resource<>(Status.ERROR, null, msg);
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(Status.LOADING, null, null);
    }
}
```

#### `data/repository/AuthRepository.java`
```java
package com.example.findit.data.repository;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.findit.data.models.*;
import com.example.findit.data.network.ApiService;
import com.example.findit.data.network.RetrofitClient;
import com.example.findit.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final ApiService apiService;
    private final SessionManager sessionManager;

    public AuthRepository(Context context) {
        apiService = RetrofitClient.getClient(context).create(ApiService.class);
        sessionManager = SessionManager.getInstance(context);
    }

    public LiveData<Resource<AuthResponse>> login(String email, String password) {
        MutableLiveData<Resource<AuthResponse>> loginResult = new MutableLiveData<>();
        loginResult.setValue(Resource.loading());

        apiService.login(new LoginRequest(email, password)).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse auth = response.body();
                    // Save Token locally in SharedPreferences
                    sessionManager.saveSession(auth.getToken(), auth.getUserId(), auth.getEmail(), auth.getFullName());
                    loginResult.setValue(Resource.success(auth));
                } else {
                    loginResult.setValue(Resource.error("Login failed. Check your credentials."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                loginResult.setValue(Resource.error("Network error: " + t.getMessage()));
            }
        });

        return loginResult;
    }

    public void logout() {
        sessionManager.logout();
    }
}
```

#### `data/repository/ItemRepository.java`
```java
package com.example.findit.data.repository;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.findit.data.models.*;
import com.example.findit.data.network.ApiService;
import com.example.findit.data.network.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemRepository {
    private final ApiService apiService;

    public ItemRepository(Context context) {
        apiService = RetrofitClient.getClient(context).create(ApiService.class);
    }

    public LiveData<Resource<Item>> reportItem(Long userId, ItemRequest request) {
        MutableLiveData<Resource<Item>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.reportItem(userId, request).enqueue(new Callback<Item>() {
            @Override
            public void onResponse(@NonNull Call<Item> call, @NonNull Response<Item> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error("Failed to report item."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Item> call, @NonNull Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<List<Item>>> getAllLostItems() {
        MutableLiveData<Resource<List<Item>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.getAllLostItems().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(@NonNull Call<List<Item>> call, @NonNull Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error("Failed to fetch lost items."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Item>> call, @NonNull Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<List<Item>>> getAllFoundItems() {
        MutableLiveData<Resource<List<Item>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.getAllFoundItems().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(@NonNull Call<List<Item>> call, @NonNull Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error("Failed to fetch found items."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Item>> call, @NonNull Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<List<MatchResponse>>> getMatchesForLost(Long lostItemId, double threshold) {
        MutableLiveData<Resource<List<MatchResponse>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.getMatchesForLost(lostItemId, threshold).enqueue(new Callback<List<MatchResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<MatchResponse>> call, @NonNull Response<List<MatchResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error("Failed to fetch matches."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MatchResponse>> call, @NonNull Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage()));
            }
        });

        return result;
    }
}
```

---

### 5. ViewModel Layer (Holds Screen State)

#### `ui/AuthViewModel.java`
```java
package com.example.findit.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.findit.data.models.AuthResponse;
import com.example.findit.data.repository.AuthRepository;
import com.example.findit.data.repository.Resource;

public class AuthViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    public LiveData<Resource<AuthResponse>> login(String email, String password) {
        return authRepository.login(email, password);
    }

    public void logout() {
        authRepository.logout();
    }
}
```

#### `ui/ItemViewModel.java`
```java
package com.example.findit.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.findit.data.models.*;
import com.example.findit.data.repository.ItemRepository;
import com.example.findit.data.repository.Resource;
import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private final ItemRepository itemRepository;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        itemRepository = new ItemRepository(application);
    }

    public LiveData<Resource<Item>> reportItem(Long userId, ItemRequest request) {
        return itemRepository.reportItem(userId, request);
    }

    public LiveData<Resource<List<Item>>> getAllLostItems() {
        return itemRepository.getAllLostItems();
    }

    public LiveData<Resource<List<Item>>> getAllFoundItems() {
        return itemRepository.getAllFoundItems();
    }

    public LiveData<Resource<List<MatchResponse>>> getMatchesForLost(Long lostItemId, double threshold) {
        return itemRepository.getMatchesForLost(lostItemId, threshold);
    }
}
```

---

### 6. UI Layer: Auth - View Layout & Activity Controller

#### Layout: `res/layout/activity_login.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="24dp">

    <TextView
        android:id="@+id/tvTitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="🔍 FindIt Campus"
        android:textSize="32sp"
        android:textStyle="bold"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="64dp" />

    <EditText
        android:id="@+id/etEmail"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Email Address"
        android:inputType="textEmailAddress"
        app:layout_constraintTop_toBottomOf="@id/tvTitle"
        android:layout_marginTop="48dp" />

    <EditText
        android:id="@+id/etPassword"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Password"
        android:inputType="textPassword"
        app:layout_constraintTop_toBottomOf="@id/etEmail"
        android:layout_marginTop="16dp" />

    <Button
        android:id="@+id/btnLogin"
        android:layout_width="match_parent"
        android:layout_height="55dp"
        android:text="LOG IN"
        android:textSize="16sp"
        app:layout_constraintTop_toBottomOf="@id/etPassword"
        android:layout_marginTop="32dp" />

    <ProgressBar
        android:id="@+id/progressBar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:visibility="gone"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

#### Activity: `ui/LoginActivity.java`
```java
package com.example.findit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.findit.R;
import com.example.findit.data.repository.Resource;
import com.example.findit.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 🔄 Bypass Login if already logged in!
        if (SessionManager.getInstance(this).isLoggedIn()) {
            navigateToDashboard();
            return;
        }

        setContentView(R.layout.activity_login);

        // Bind Views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);

        // Instantiate ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Listen for Login Button click
        btnLogin.setOnClickListener(v -> executeLogin());
    }

    private void executeLogin() {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call the ViewModel & observe the live data resource
        authViewModel.login(email, pass).observe(this, resource -> {
            if (resource == null) return;
            
            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    btnLogin.setEnabled(false);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);
                    Toast.makeText(this, "Welcome " + resource.data.getFullName() + "!", Toast.LENGTH_SHORT).show();
                    navigateToDashboard();
                    break;
                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
```

---

### 7. UI Layer: Core Screens - Dashboard Activity

#### Layout: `res/layout/activity_dashboard.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="24dp">

    <TextView
        android:id="@+id/tvWelcome"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Welcome, Student!"
        android:textSize="26sp"
        android:textStyle="bold"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="40dp" />

    <TextView
        android:id="@+id/tvSub"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="What would you like to do today?"
        android:textSize="16sp"
        android:textColor="#555"
        app:layout_constraintTop_toBottomOf="@id/tvWelcome"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="8dp" />

    <androidx.cardview.widget.CardView
        android:id="@+id/cardReport"
        android:layout_width="0dp"
        android:layout_height="130dp"
        app:cardCornerRadius="12dp"
        app:cardElevation="4dp"
        app:layout_constraintTop_toBottomOf="@id/tvSub"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="40dp"
        android:foreground="?android:attr/selectableItemBackground"
        android:clickable="true">
        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:padding="16dp"
            android:background="#2196F3">
            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="📝 Report Lost/Found"
                android:textColor="#fff"
                android:textSize="20sp"
                android:textStyle="bold"
                android:layout_centerVertical="true"/>
        </RelativeLayout>
    </androidx.cardview.widget.CardView>

    <androidx.cardview.widget.CardView
        android:id="@+id/cardLost"
        android:layout_width="0dp"
        android:layout_height="110dp"
        app:cardCornerRadius="12dp"
        app:cardElevation="4dp"
        app:layout_constraintTop_toBottomOf="@id/cardReport"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toStartOf="@id/cardFound"
        android:layout_marginTop="20dp"
        android:layout_marginEnd="10dp"
        android:foreground="?android:attr/selectableItemBackground"
        android:clickable="true">
        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:padding="16dp"
            android:background="#FF9800">
            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="🔍 Browse\nLost Items"
                android:textColor="#fff"
                android:textSize="18sp"
                android:textStyle="bold"
                android:layout_centerInParent="true"/>
        </RelativeLayout>
    </androidx.cardview.widget.CardView>

    <androidx.cardview.widget.CardView
        android:id="@+id/cardFound"
        android:layout_width="0dp"
        android:layout_height="110dp"
        app:cardCornerRadius="12dp"
        app:cardElevation="4dp"
        app:layout_constraintTop_toBottomOf="@id/cardReport"
        app:layout_constraintStart_toEndOf="@id/cardLost"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="20dp"
        android:layout_marginStart="10dp"
        android:foreground="?android:attr/selectableItemBackground"
        android:clickable="true">
        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:padding="16dp"
            android:background="#4CAF50">
            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="✅ Browse\nFound Items"
                android:textColor="#fff"
                android:textSize="18sp"
                android:textStyle="bold"
                android:layout_centerInParent="true"/>
        </RelativeLayout>
    </androidx.cardview.widget.CardView>

    <Button
        android:id="@+id/btnLogout"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="LOGOUT"
        android:backgroundTint="#f44336"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginBottom="32dp" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

#### Activity: `ui/DashboardActivity.java`
```java
package com.example.findit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.findit.R;
import com.example.findit.utils.SessionManager;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        CardView cardReport = findViewById(R.id.cardReport);
        CardView cardLost = findViewById(R.id.cardLost);
        CardView cardFound = findViewById(R.id.cardFound);
        Button btnLogout = findViewById(R.id.btnLogout);

        SessionManager session = SessionManager.getInstance(this);
        tvWelcome.setText("Welcome, " + session.getUserName() + "!");

        // 📝 Report Screen Trigger
        cardReport.setOnClickListener(v -> {
            startActivity(new Intent(this, ReportItemActivity.class));
        });

        // 🔍 Lost Items Browser Trigger
        cardLost.setOnClickListener(v -> {
            Intent intent = new Intent(this, ItemListActivity.class);
            intent.putExtra("TYPE", "LOST");
            startActivity(intent);
        });

        // ✅ Found Items Browser Trigger
        cardFound.setOnClickListener(v -> {
            Intent intent = new Intent(this, ItemListActivity.class);
            intent.putExtra("TYPE", "FOUND");
            startActivity(intent);
        });

        // 🟥 Logout Trigger
        btnLogout.setOnClickListener(v -> {
            session.logout();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
```

---

### 8. UI Layer: Core Screens - Reporting Items

#### Layout: `res/layout/activity_report_item.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fillViewport="true">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="24dp">

        <TextView
            android:id="@+id/tvReportTitle"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="📝 Report Item"
            android:textSize="26sp"
            android:textStyle="bold"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintStart_toStartOf="parent"/>

        <RadioGroup
            android:id="@+id/rgType"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            app:layout_constraintTop_toBottomOf="@id/tvReportTitle"
            android:layout_marginTop="20dp">
            <RadioButton
                android:id="@+id/rbLost"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Lost Item"
                android:checked="true"
                android:textSize="16sp"/>
            <RadioButton
                android:id="@+id/rbFound"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Found Item"
                android:layout_marginStart="30dp"
                android:textSize="16sp"/>
        </RadioGroup>

        <EditText
            android:id="@+id/etTitle"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="Item Title (e.g. Blue Water Bottle)"
            app:layout_constraintTop_toBottomOf="@id/rgType"
            android:layout_marginTop="20dp" />

        <EditText
            android:id="@+id/etDesc"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="Item Description"
            android:inputType="textMultiLine"
            android:minLines="3"
            app:layout_constraintTop_toBottomOf="@id/etTitle"
            android:layout_marginTop="16dp" />

        <TextView
            android:id="@+id/tvCategoryLbl"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Category:"
            android:textStyle="bold"
            android:textSize="16sp"
            app:layout_constraintTop_toBottomOf="@id/etDesc"
            app:layout_constraintStart_toStartOf="parent"
            android:layout_marginTop="20dp"/>

        <Spinner
            android:id="@+id/spinnerCategory"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            app:layout_constraintTop_toTopOf="@id/tvCategoryLbl"
            app:layout_constraintBottom_toBottomOf="@id/tvCategoryLbl"
            app:layout_constraintStart_toEndOf="@id/tvCategoryLbl"
            app:layout_constraintEnd_toEndOf="parent"
            android:layout_marginStart="16dp"/>

        <!-- COMPOSITION Location Fields -->
        <TextView
            android:id="@+id/tvLocationTitle"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="📍 Location Found/Lost"
            android:textSize="18sp"
            android:textStyle="bold"
            app:layout_constraintTop_toBottomOf="@id/tvCategoryLbl"
            app:layout_constraintStart_toStartOf="parent"
            android:layout_marginTop="28dp"/>

        <EditText
            android:id="@+id/etBuilding"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="Building (e.g. Science Block)"
            app:layout_constraintTop_toBottomOf="@id/tvLocationTitle"
            android:layout_marginTop="12dp" />

        <EditText
            android:id="@+id/etFloor"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="Floor (e.g. 2nd Floor)"
            app:layout_constraintTop_toBottomOf="@id/etBuilding"
            android:layout_marginTop="12dp" />

        <EditText
            android:id="@+id/etCampus"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="Campus (e.g. Main Campus)"
            app:layout_constraintTop_toBottomOf="@id/etFloor"
            android:layout_marginTop="12dp" />

        <EditText
            android:id="@+id/etDate"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="Incident Date (YYYY-MM-DD)"
            android:focusable="false"
            app:layout_constraintTop_toBottomOf="@id/etCampus"
            android:layout_marginTop="20dp" />

        <Button
            android:id="@+id/btnSubmit"
            android:layout_width="match_parent"
            android:layout_height="55dp"
            android:text="SUBMIT REPORT"
            android:textSize="16sp"
            app:layout_constraintTop_toBottomOf="@id/etDate"
            android:layout_marginTop="32dp"/>

        <ProgressBar
            android:id="@+id/progressBarReport"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:visibility="gone"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent" />

    </androidx.constraintlayout.widget.ConstraintLayout>
</ScrollView>
```

#### Activity: `ui/ReportItemActivity.java`
```java
package com.example.findit.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.findit.R;
import com.example.findit.data.models.*;
import com.example.findit.data.repository.Resource;
import com.example.findit.utils.SessionManager;
import java.util.Calendar;
import java.util.Locale;

public class ReportItemActivity extends AppCompatActivity {

    private RadioButton rbLost;
    private EditText etTitle, etDesc, etBuilding, etFloor, etCampus, etDate;
    private Spinner spinnerCategory;
    private Button btnSubmit;
    private ProgressBar progressBar;
    private ItemViewModel itemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_item);

        // Bind Views
        rbLost = findViewById(R.id.rbLost);
        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        etBuilding = findViewById(R.id.etBuilding);
        etFloor = findViewById(R.id.etFloor);
        etCampus = findViewById(R.id.etCampus);
        etDate = findViewById(R.id.etDate);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBarReport);

        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        // Setup Spinner with Categories
        Category[] categories = Category.values();
        String[] categoryNames = new String[categories.length];
        for (int i = 0; i < categories.length; i++) {
            categoryNames[i] = categories[i].getDisplayName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryNames);
        spinnerCategory.setAdapter(adapter);

        // Date Picker Setup
        etDate.setOnClickListener(v -> showDatePicker());

        // Submit
        btnSubmit.setOnClickListener(v -> submitItem());
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog picker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            etDate.setText(formattedDate);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        picker.show();
    }

    private void submitItem() {
        String title = etTitle.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();
        String building = etBuilding.getText().toString().trim();
        String floor = etFloor.getText().toString().trim();
        String campus = etCampus.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        Category category = Category.values()[spinnerCategory.getSelectedItemPosition()];
        String type = rbLost.isChecked() ? "LOST" : "FOUND";

        if (title.isEmpty() || desc.isEmpty() || building.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill out required fields (Title, Desc, Building, Date)", Toast.LENGTH_SHORT).show();
            return;
        }

        Location location = new Location(building, floor, campus);
        ItemRequest req = new ItemRequest(title, desc, category, location, date, "", type);
        Long userId = SessionManager.getInstance(this).getUserId();

        itemViewModel.reportItem(userId, req).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    btnSubmit.setEnabled(false);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Reported Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    btnSubmit.setEnabled(true);
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }
}
```

---

### 9. UI Layer: Core Screens - Item Browsing (RecyclerView List)

#### Layout: `res/layout/activity_item_list.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <TextView
        android:id="@+id/tvListTitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Items List"
        android:textSize="24sp"
        android:textStyle="bold"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"/>

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintTop_toBottomOf="@id/tvListTitle"
        app:layout_constraintBottom_toBottomOf="parent"
        android:layout_marginTop="16dp"/>

    <ProgressBar
        android:id="@+id/progressBarList"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:visibility="gone"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

#### Layout: `res/layout/item_row.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginVertical="8dp"
    app:cardCornerRadius="8dp"
    app:cardElevation="2dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">

        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content">
            <TextView
                android:id="@+id/tvRowTitle"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Water Bottle"
                android:textSize="18sp"
                android:textStyle="bold" />
            <TextView
                android:id="@+id/tvRowStatus"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_alignParentEnd="true"
                android:text="ACTIVE"
                android:textSize="12sp"
                android:textStyle="bold"/>
        </RelativeLayout>

        <TextView
            android:id="@+id/tvRowCategory"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Category: Accessories"
            android:layout_marginTop="4dp"
            android:textColor="#777" />

        <TextView
            android:id="@+id/tvRowLocation"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Location: Science Block, Floor 2"
            android:layout_marginTop="4dp" />

        <TextView
            android:id="@+id/tvRowDate"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Date: 2026-07-15"
            android:layout_marginTop="4dp"
            android:textSize="12sp" />
    </LinearLayout>
</androidx.cardview.widget.CardView>
```

#### Adapter: `ui/ItemAdapter.java`
```java
package com.example.findit.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.findit.R;
import com.example.findit.data.models.Item;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final List<Item> items;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public ItemAdapter(List<Item> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle, tvCategory, tvLocation, tvDate, tvStatus;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvRowTitle);
            tvCategory = itemView.findViewById(R.id.tvRowCategory);
            tvLocation = itemView.findViewById(R.id.tvRowLocation);
            tvDate = itemView.findViewById(R.id.tvRowDate);
            tvStatus = itemView.findViewById(R.id.tvRowStatus);
        }

        public void bind(Item item, OnItemClickListener listener) {
            tvTitle.setText(item.getTitle());
            tvCategory.setText(item.getCategory().getDisplayName());
            tvLocation.setText(item.getLocation() != null ? item.getLocation().toString() : "No location");
            tvDate.setText("Date: " + item.getIncidentDate());
            
            boolean active = !item.isResolved() && !item.isClaimed();
            tvStatus.setText(active ? "ACTIVE" : "RESOLVED");
            tvStatus.setTextColor(itemView.getContext().getResources().getColor(
                    active ? android.R.color.holo_orange_dark : android.R.color.holo_green_dark
            ));

            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}
```

#### Activity: `ui/ItemListActivity.java`
```java
package com.example.findit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.findit.R;
import com.example.findit.data.models.Item;
import java.util.ArrayList;
import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvTitle;
    private ItemViewModel itemViewModel;
    private String type; // "LOST" or "FOUND"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        tvTitle = findViewById(R.id.tvListTitle);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBarList);

        type = getIntent().getStringExtra("TYPE");
        tvTitle.setText(type.equals("LOST") ? "Lost Items Directory" : "Found Items Directory");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        fetchData();
    }

    private void fetchData() {
        if (type.equals("LOST")) {
            itemViewModel.getAllLostItems().observe(this, resource -> {
                if (resource == null) return;
                handleResponse(resource.status, resource.data, resource.message);
            });
        } else {
            itemViewModel.getAllFoundItems().observe(this, resource -> {
                if (resource == null) return;
                handleResponse(resource.status, resource.data, resource.message);
            });
        }
    }

    private void handleResponse(com.example.findit.data.repository.Resource.Status status, List<Item> data, String errorMsg) {
        switch (status) {
            case LOADING:
                progressBar.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                progressBar.setVisibility(View.GONE);
                if (data != null) {
                    recyclerView.setAdapter(new ItemAdapter(data, item -> {
                        if (type.equals("LOST")) {
                            // 💡 Trigger smart backend matchmaking automatically on click!
                            Intent intent = new Intent(this, MatchListActivity.class);
                            intent.putExtra("LOST_ID", item.getId());
                            intent.putExtra("LOST_TITLE", item.getTitle());
                            startActivity(intent);
                        } else {
                            // Found item detail logic goes here
                            tvTitle.post(() -> android.widget.Toast.makeText(this, "Found Item ID: " + item.getId(), android.widget.Toast.LENGTH_SHORT).show());
                        }
                    }));
                }
                break;
            case ERROR:
                progressBar.setVisibility(View.GONE);
                android.widget.Toast.makeText(this, errorMsg, android.widget.Toast.LENGTH_LONG).show();
                break;
        }
    }
}
```

---

### 10. UI Layer: Core Screens - Matching Engine Results

#### Layout: `res/layout/activity_match_list.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <TextView
        android:id="@+id/tvMatchHeader"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="🧠 Match Finder Results"
        android:textSize="22sp"
        android:textStyle="bold"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"/>

    <TextView
        android:id="@+id/tvSubHeader"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Matches for: Loading..."
        android:textSize="14sp"
        android:textColor="#666"
        app:layout_constraintTop_toBottomOf="@id/tvMatchHeader"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="4dp" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerMatches"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintTop_toBottomOf="@id/tvSubHeader"
        app:layout_constraintBottom_toBottomOf="parent"
        android:layout_marginTop="16dp"/>

    <ProgressBar
        android:id="@+id/progressBarMatches"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:visibility="gone"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

#### Layout: `res/layout/match_row.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginVertical="8dp"
    app:cardCornerRadius="8dp"
    app:cardElevation="3dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">

        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content">
            <TextView
                android:id="@+id/tvMatchTitle"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Black Wallet"
                android:textSize="18sp"
                android:textStyle="bold" />
            
            <TextView
                android:id="@+id/tvMatchLevel"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_alignParentEnd="true"
                android:text="STRONG"
                android:textStyle="bold"
                android:textSize="14sp"/>
        </RelativeLayout>

        <TextView
            android:id="@+id/tvMatchDescription"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Found wallet containing library card and driver's license."
            android:layout_marginTop="6dp"
            android:textColor="#444" />

        <TextView
            android:id="@+id/tvMatchLocation"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Library, Floor 1, Main Campus"
            android:layout_marginTop="6dp"
            android:textColor="#777"
            android:textSize="13sp"/>

        <TextView
            android:id="@+id/tvMatchScore"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Match Score: 85.0%"
            android:textColor="#3F51B5"
            android:textStyle="bold"
            android:layout_marginTop="8dp"
            android:textSize="13sp"/>
    </LinearLayout>
</androidx.cardview.widget.CardView>
```

#### Adapter: `ui/MatchAdapter.java`
```java
package com.example.findit.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.findit.R;
import com.example.findit.data.models.MatchResponse;
import java.util.List;
import java.util.Locale;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private final List<MatchResponse> matches;

    public MatchAdapter(List<MatchResponse> matches) {
        this.matches = matches;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_row, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        holder.bind(matches.get(position));
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    static class MatchViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle, tvDescription, tvLocation, tvScore, tvLevel;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvMatchTitle);
            tvDescription = itemView.findViewById(R.id.tvMatchDescription);
            tvLocation = itemView.findViewById(R.id.tvMatchLocation);
            tvScore = itemView.findViewById(R.id.tvMatchScore);
            tvLevel = itemView.findViewById(R.id.tvMatchLevel);
        }

        public void bind(MatchResponse match) {
            tvTitle.setText(match.getFoundTitle());
            tvDescription.setText(match.getFoundDescription());
            tvLocation.setText(match.getFoundLocation());
            tvScore.setText(String.format(Locale.getDefault(), "Score: %.1f%%", match.getScore()));
            tvLevel.setText(match.getMatchLevel());

            int colorRes;
            switch (match.getMatchLevel()) {
                case "STRONG":
                    colorRes = android.R.color.holo_green_dark;
                    break;
                case "MODERATE":
                    colorRes = android.R.color.holo_orange_dark;
                    break;
                default:
                    colorRes = android.R.color.holo_red_dark;
                    break;
            }
            tvLevel.setTextColor(itemView.getContext().getResources().getColor(colorRes));
        }
    }
}
```

#### Activity: `ui/MatchListActivity.java`
```java
package com.example.findit.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.findit.R;

public class MatchListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvSub;
    private ItemViewModel itemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);

        tvSub = findViewById(R.id.tvSubHeader);
        recyclerView = findViewById(R.id.recyclerMatches);
        progressBar = findViewById(R.id.progressBarMatches);

        Long lostId = getIntent().getLongExtra("LOST_ID", -1);
        String lostTitle = getIntent().getStringExtra("LOST_TITLE");
        tvSub.setText("Matches for: " + lostTitle);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        // Fetch matches with a default similarity threshold of 40%
        itemViewModel.getMatchesForLost(lostId, 40.0).observe(this, resource -> {
            if (resource == null) return;

            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    if (resource.data != null) {
                        if (resource.data.isEmpty()) {
                            Toast.makeText(this, "No potential matches found for this item.", Toast.LENGTH_LONG).show();
                        } else {
                            recyclerView.setAdapter(new MatchAdapter(resource.data));
                        }
                    }
                    break;
                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }
}
```

---

## 🚀 Part 11: Running & testing your integration

1. **Start the backend locally**: Ensure your Spring Boot backend is active and running on your main system.
   ```bash
   ./mvnw spring-boot:run
   ```
2. **Launch Android Emulator**: Open your project in Android Studio, start your virtual device.
3. **Run your App**: Run the application from Android Studio. Retrofit will connect dynamically to `http://10.0.2.2:8080` (routing through your computer's local loopback to reach the Spring Boot backend) using the custom intercepts we created.
4. **Inspect Calls**: Look at the Android Studio **Logcat** filter with the tag `OkHttp` to inspect HTTP headers, payloads, and responses in real-time.
