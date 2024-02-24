package com.example.myapplication;

import static com.example.myapplication.service.MyForegroundService.foregroundService;
import static com.example.myapplication.service.MyForegroundService.no_of_running_service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Support.User;
import com.example.myapplication.Support.UserPreferences;
import com.example.myapplication.model.ItemModel;
import com.example.myapplication.network.WebSocketClient;
import com.example.myapplication.service.MyForegroundService;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
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

            if ("A".equals(user.getUsermode())) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else if ("B".equals(user.getUsermode())) {
                startActivity(new Intent(LoginActivity.this, newmainuser.class));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        User user=UserPreferences.getUser(getApplicationContext());
        if(user!=null){
            Log.d(TAG, "onCreate: stopping login because user found ");
            finish();
            if ("A".equals(user.getUsermode())) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else if ("B".equals(user.getUsermode())) {
                startActivity(new Intent(LoginActivity.this, newmainuser.class));
            }
        }else {
           /* serviceIntent = new Intent(this, MyForegroundService.class);
            IntentFilter filter = new IntentFilter("com.example.ACTION_SEND_DATA");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                registerReceiver(broadcastReceiver, filter);
            }*/
            textInputUsername = findViewById(R.id.text_input_username);
            textInputPassword = findViewById(R.id.text_input_password);
            buttonLogin = findViewById(R.id.button_login);
           /* if (isServiceRunning(MyForegroundService.class)) {
                ContextCompat.startForegroundService(LoginActivity.this, serviceIntent);
                bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

                Log.d(TAG, "onCreate: starting the service because no service found ");
            } else {
                Log.d(TAG, "onCreate: service is not running ");
                ConstraintLayout root = findViewById(R.id.parent);
                Snackbar.make(root, "service already running ", Snackbar.LENGTH_SHORT).show();
            }*/
            buttonLogin.setOnClickListener(v -> {
                // Retrieve input values
                String username = textInputUsername.getEditText().getText().toString().trim();
                String password = textInputPassword.getEditText().getText().toString().trim();

                // Validate input
                if (validateInput(username, password)) {
                    Log.d(TAG, "onCreate: checking auth ");
                    // Perform login operation (replace this with your actual login logic)
                    Snackbar.make(findViewById(R.id.parent), "Authenticating", Snackbar.LENGTH_SHORT).show();
                    WebSocketClient webSocketClient=new WebSocketClient(this,new MyForegroundService());
                    webSocketClient.connect();
                    webSocketClient.Auth(username,password);
                }
            });
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