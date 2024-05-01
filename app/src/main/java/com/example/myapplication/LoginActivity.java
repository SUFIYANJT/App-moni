package com.example.myapplication;

import static com.example.myapplication.service.MyForegroundService.no_of_running_service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.myapplication.Support.User;
import com.example.myapplication.Support.UserPreferences;
import com.example.myapplication.network.WebSocketClient;
import com.example.myapplication.service.MyForegroundService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private Button buttonLogin;
    public static String TAG="maintenance";
    Intent serviceIntent;
    MyForegroundService foreground;
    private boolean isBound=false;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        private ComponentName name;
        private IBinder service;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            this.name = name;
            this.service = service;
            Log.d(TAG, "onServiceConnected: service connection started...");
            MyForegroundService.MyBinder binder = (MyForegroundService.MyBinder) service;
            foreground = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            User user = (User) intent.getSerializableExtra("key");
            Log.d(TAG, "onReceive: " + user.getUsername() + " " + user.getPassword() + " " + user.getUsermode());

            if (isBound) {
                unbindService(serviceConnection);
                isBound = false;
            }

            unregisterReceiver(broadcastReceiver);

            if (foreground != null) {
                foreground.stopForeground(true);
                foreground.stopSelf();
            }
            UserPreferences.saveUser(getApplicationContext(),user);
            finish();
            Log.d(TAG, "onReceive: user is gotten "+user.getUsermode());
            if ("A".equals(user.getUsermode())) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else if ("B".equals(user.getUsermode())) {
                startActivity(new Intent(LoginActivity.this, newmainuser.class));
            }
        }
    };
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        askNotificationPermission();
        User user=UserPreferences.getUser(getApplicationContext());
        FirebaseMessaging.getInstance().subscribeToTopic("user")
                .addOnCompleteListener(task -> {
                    String msg = "Subscribed";
                    if (!task.isSuccessful()) {
                        msg = "Subscribe failed";
                    }
                    Log.d(TAG, msg);
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                });
        if(user!=null){
            Log.d(TAG, "onCreate: stopping login because user found ");
            finish();
            if ("A".equals(user.getUsermode())) {
                FirebaseMessaging.getInstance().subscribeToTopic("admin")
                        .addOnCompleteListener(task -> {
                            String msg = "Subscribed";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe failed";
                            }
                            Log.d(TAG, msg);
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        });
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else if ("B".equals(user.getUsermode())) {
                FirebaseMessaging.getInstance().subscribeToTopic("inspector")
                        .addOnCompleteListener(task -> {
                            String msg = "Subscribed";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe failed";
                            }
                            Log.d(TAG, msg);
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        });
                startActivity(new Intent(LoginActivity.this, newmainuser.class));
            }
        }else {
            textInputUsername = findViewById(R.id.text_input_username);
            textInputPassword = findViewById(R.id.text_input_password);
            buttonLogin = findViewById(R.id.button_login);
            buttonLogin.setOnClickListener(v -> {
                // Retrieve input values
                String username = textInputUsername.getEditText().getText().toString().trim();
                String password = textInputPassword.getEditText().getText().toString().trim();

                // Validate input
                if (validateInput(username, password)) {
                    Log.d(TAG, "onCreate: checking auth ");
                    // Perform login operation (replace this with your actual login logic)
                    Snackbar.make(findViewById(R.id.parent), "Authenticating", Snackbar.LENGTH_SHORT).show();
                    WebSocketClient webSocketClient=new WebSocketClient(this);
                    webSocketClient.connect();
                    webSocketClient.Auth(username,password);
                }
            });
        }
    }
    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            // Get new FCM registration token
                            String token = task.getResult();
                            // Log and toast
                            Log.d(TAG, token);
                            Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
                        });

                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {

            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
    public void NextPage(User user){
        Log.d(TAG, "onReceive: " + user.getUsername() + " " + user.getPassword() + " " + user.getUsermode());
        UserPreferences.saveUser(getApplicationContext(),user);
        finish();

        if ("A".equals(user.getUsermode())) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        } else if ("B".equals(user.getUsermode())) {
            startActivity(new Intent(LoginActivity.this, newmainuser.class));
        }
    }
   public void BioMetricAuth(){
       BiometricManager biometricManager = BiometricManager.from(this);
       switch (biometricManager.canAuthenticate()) {
           case BiometricManager.BIOMETRIC_SUCCESS:
               Log.d("MY_APP_TAG", "Biometric authentication can be used.");
               break;
           case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
               Log.e("MY_APP_TAG", "No biometric features available on this device.");
               break;
           case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
               Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
               break;
           case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
               Log.e("MY_APP_TAG", "The user hasn't enrolled any biometrics.");
               break;
       }
       BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
               .setTitle("Biometric login")
               .setSubtitle("Log in using your biometric credential")
               .setNegativeButtonText("Cancel")
               .build();
       Executor executor = Executors.newSingleThreadExecutor();
       BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
               new BiometricPrompt.AuthenticationCallback() {
                   @Override
                   public void onAuthenticationError(int errorCode, CharSequence errString) {
                       super.onAuthenticationError(errorCode, errString);
                       Log.e("MY_APP_TAG", "Authentication error: " + errString);
                   }

                   @Override
                   public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                       super.onAuthenticationSucceeded(result);
                       Intent intent = new Intent(LoginActivity.this, newmainuser.class);
                       startActivity(intent);
                       Log.d("MY_APP_TAG", "Authentication succeeded!");
                       // Proceed with the authenticated operation
                   }

                   @Override
                   public void onAuthenticationFailed() {
                       super.onAuthenticationFailed();
                       Log.e("MY_APP_TAG", "Authentication failed");
                   }
               });

       biometricPrompt.authenticate(promptInfo);
   }
    public boolean isServiceRunning(Class<?> serviceClass) {
        Log.d(TAG, "isServiceRunning: Called to check if a service is running "+no_of_running_service);
        return no_of_running_service == 0;
    }

    // Method to validate input fields
    private boolean validateInput(String username, String password) {
        if (username.isEmpty()) {
            textInputUsername.setError("Username cannot be empty");
            return false;
        } else {
            textInputUsername.setError(null);
        }
        if (password.isEmpty()) {
            textInputPassword.setError("Password cannot be empty");
            return false;
        } else {
            textInputPassword.setError(null);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: login destory ");

    }
}