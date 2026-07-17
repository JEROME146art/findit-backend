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
        
        <activity
            android:name=".ui.LoginActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        
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
│       └── ItemRepository.java
├── ui/
│   ├── LoginActivity.java
│   └── ItemViewModel.java (and AuthViewModel.java)
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

#### Helper Class: `data/repository/Resource.java`
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

---

### 6. UI Layer: View Layout & Activity Controller

#### Layout: `res/layout/activity_login.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
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

</ConstraintLayout>
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
        // Replace 'DashboardActivity.class' with your actual main app landing screen
        // Intent intent = new Intent(this, DashboardActivity.class);
        // startActivity(intent);
        // finish();
        Toast.makeText(this, "Redirecting to Main Dashboard...", Toast.LENGTH_SHORT).show();
    }
}
```

---

## 🚀 Part 5: Running & testing your integration

1. **Start the backend locally**: Ensure your Spring Boot backend is active and running on your main system.
   ```bash
   ./mvnw spring-boot:run
   ```
2. **Launch Android Emulator**: Open your project in Android Studio, start your virtual device.
3. **Run your App**: Run the application from Android Studio. Retrofit will connect dynamically to `http://10.0.2.2:8080` (routing through your computer's local loopback to reach the Spring Boot backend) using the custom intercepts we created.
4. **Inspect Calls**: Look at the Android Studio **Logcat** filter with the tag `OkHttp` to inspect HTTP headers, payloads, and responses in real-time.
