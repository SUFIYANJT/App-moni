package com.example.myapplication.network;

import static com.example.myapplication.LoginActivity.TAG;

import android.content.Context;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.ReportTransfer;
import com.example.myapplication.Support.Activity;
import com.example.myapplication.Support.Machine;
import com.example.myapplication.Support.SubmitHolder;
import com.example.myapplication.Support.Task;
import com.example.myapplication.Support.User;
import com.example.myapplication.Support.UserPreferences;
import com.example.myapplication.model.ItemModel;
import com.example.myapplication.service.MyForegroundService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketClient extends WebSocketListener {
    WebSocket webSocket;
    ReportTransfer newWindow;
    ItemModel model;
    Context context;
    MyForegroundService foregroundService;
    private Timer timer;
    private final int maxRetries = 5;
    private int retryCount = 0;
    int getActivityCallCount = 0;

    private final String ipAddress= "ws://192.168.43.174:8000/chat/";
    public WebSocketClient(Context context, MyForegroundService foregroundService){
        this.context=context;
        this.foregroundService=foregroundService;
        connect();
    }
    public WebSocketClient(Context context){
        String userid="userid/";
        User user=UserPreferences.getUser(context.getApplicationContext());
        if(user!=null){
            userid=user.getUser_id()+"/";
        }
        Log.d(TAG, "WebSocketClient: userid is "+userid);
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(ipAddress+userid)
                .build();
        webSocket=okHttpClient.newWebSocket(request,this);
        this.context=context;
    }

    public void connect(){
        Log.d(TAG, "connect: is used to connect...");
        String userid="userid/";
        User user=UserPreferences.getUser(context.getApplicationContext());
        if(user!=null){
            userid=user.getUser_id()+"/";
        }
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(ipAddress+userid)
                .build();
        webSocket=okHttpClient.newWebSocket(request,this);
    }

    public void getExistingActivity(){
        getActivityCallCount++;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject=new JSONObject();
        User user=UserPreferences.getUser(context.getApplicationContext());
        if(user!=null) {
            try {
                jsonObject.put("activity", "existing");
                jsonObject.put("username", user.getUsername());
                jsonObject.put("password", user.getPassword());
                jsonObject.put("usermode",user.getUsermode());
            } catch (JSONException e) {
                Log.d(TAG, "Auth: ", e);
                ;
            }
        }else{
            Log.d(TAG, "getExistingActivity: both user is not logged in and service started ");
        }
        Log.d(TAG, "getExistingActivity: sending activity to server ");
        webSocket.send(jsonObject.toString());
    }

    public boolean Auth(String user,String password){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("username",user);
            jsonObject.put("password",password);
            jsonObject.put("auth","auth");
        } catch (JSONException e) {
            Log.d(TAG, "Auth: ",e);;
        }
        Log.d(TAG, "Auth: json converted data "+ jsonObject +" "+password+" "+user);
            return webSocket.send(jsonObject.toString());
    }
    public void getSchedules(){
        User user=UserPreferences.getUser(context.getApplicationContext());
        JSONObject jsonObject=new JSONObject();
        try {
            if (user != null) {
                jsonObject.put("username",user.getUsername());
                jsonObject.put("password",user.getPassword());
                jsonObject.put("usermode",user.getUsermode());
                jsonObject.put("schedule","schedule");
                webSocket.send(jsonObject.toString());
            }else{
                Log.d(TAG, "getSchedules: User not defined yet ");
            }
        } catch (JSONException e) {
            Log.d(TAG, "Auth: ",e);;
        }
    }
    public void CreateActivity(Activity activity){
        User user=UserPreferences.getUser(context.getApplicationContext());
        JSONObject jsonObject=new JSONObject();
        try {
            if (user != null) {
                Log.d(TAG, "CreateActivity: activity id "+activity.activityId);
                jsonObject.put("username",user.getUsername());
                jsonObject.put("password",user.getPassword());
                jsonObject.put("usermode",user.getUsermode());
                jsonObject.put("create","create");
                JSONObject activityData=new JSONObject();
                activityData.put("activity_description",activity.activityDescription);
                activityData.put("activity_name",activity.activityName);
                activityData.put("activity_issued_date",activity.issued_date);
                activityData.put("activity_machine_id",activity.machineId);
                activityData.put("activity_component_id",activity.componentId);
                activityData.put("activity_schedule_id",activity.componentId);
                activityData.put("activity_status_id",activity.activity_satuts_id);
                activityData.put("assigned_to_user",activity.assigned_to_user);
                jsonObject.put("activity",activityData);
                webSocket.send(jsonObject.toString());
            }else{
                Log.d(TAG, "getSchedules: User not defined yet ");
            }
        } catch (JSONException e) {
            Log.d(TAG, "Auth: ",e);;
        }
    }
    public void deleteActivity(Activity activity){
        User user=UserPreferences.getUser(context.getApplicationContext());
        JSONObject jsonObject=new JSONObject();
        try {
            if (user != null) {
                Log.d(TAG, "updateActivity: activity id before sending is "+activity.activityId);
                jsonObject.put("username",user.getUsername());
                jsonObject.put("password",user.getPassword());
                jsonObject.put("usermode",user.getUsermode());
                jsonObject.put("delete","delete");
                JSONObject activityData=new JSONObject();
                activityData.put("activity_id",activity.activityId);
                Log.d(TAG, "updateActivity: activityData "+activityData.getString("activity_id"));
                activityData.put("activity_description",activity.activityDescription);
                activityData.put("activity_name",activity.activityName);
                activityData.put("activity_issued_date",activity.issued_date);
                activityData.put("activity_machine_id",activity.machineId);
                activityData.put("activity_component_id",activity.componentId);
                activityData.put("activity_schedule_id",activity.componentId);
                activityData.put("activity_status_id",activity.activity_satuts_id);
                activityData.put("activity_assigned_to",activity.assigned_to);
                activityData.put("assigned_to_user",activity.assigned_to_user);
                activityData.put("change","delete");
                jsonObject.put("activity",activityData);
                webSocket.send(jsonObject.toString());
            }else{
                Log.d(TAG, "getSchedules: User not defined yet ");
            }
        } catch (JSONException e) {
            Log.d(TAG, "Auth: ",e);;
        }
    }
    public void updateActivity(Activity activity){
        User user=UserPreferences.getUser(context.getApplicationContext());
        JSONObject jsonObject=new JSONObject();
        try {
            if (user != null) {
                Log.d(TAG, "updateActivity: activity id before sending is "+activity.activityId);
                jsonObject.put("username",user.getUsername());
                jsonObject.put("password",user.getPassword());
                jsonObject.put("usermode",user.getUsermode());
                jsonObject.put("update","update");
                JSONObject activityData=new JSONObject();
                activityData.put("activity_id",activity.activityId);
                Log.d(TAG, "updateActivity: activityData "+activityData.getString("activity_id"));
                activityData.put("activity_description",activity.activityDescription);
                activityData.put("activity_name",activity.activityName);
                activityData.put("activity_issued_date",activity.issued_date);
                activityData.put("activity_machine_id",activity.machineId);
                activityData.put("activity_component_id",activity.componentId);
                activityData.put("activity_schedule_id",activity.componentId);
                activityData.put("activity_status_id",activity.activity_satuts_id);
                activityData.put("activity_assigned_to",activity.assigned_to);
                activityData.put("assigned_to_user",activity.assigned_to_user);
                jsonObject.put("activity",activityData);
                webSocket.send(jsonObject.toString());
            }else{
                Log.d(TAG, "getSchedules: User not defined yet ");
            }
        } catch (JSONException e) {
            Log.d(TAG, "Auth: ",e);;
        }
    }

    public void getComponents(){
        User user=UserPreferences.getUser(context.getApplicationContext());
        JSONObject jsonObject=new JSONObject();
        try {
            if (user != null) {
                jsonObject.put("username",user.getUsername());
                jsonObject.put("password",user.getPassword());
                jsonObject.put("usermode",user.getUsermode());
                jsonObject.put("component","component");
                webSocket.send(jsonObject.toString());
            }else{
                Log.d(TAG, "getSchedules: User not defined yet ");
            }
        } catch (JSONException e) {
            Log.d(TAG, "Auth: ",e);;
        }
    }

    public void getMachines(){
        Log.d(TAG, "getMachines: sending request from machine ");
        User user=UserPreferences.getUser(context.getApplicationContext());
        JSONObject jsonObject=new JSONObject();
        try {
            if (user != null) {
                jsonObject.put("username",user.getUsername());
                jsonObject.put("password",user.getPassword());
                jsonObject.put("usermode",user.getUsermode());
                jsonObject.put("machine","machine");
                webSocket.send(jsonObject.toString());
            }else{
                Log.d(TAG, "getSchedules: User not defined yet ");
            }
        } catch (JSONException e) {
            Log.d(TAG, "Auth: ",e);;
        }
    }

    @Override
    public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
        User user=UserPreferences.getUser(context.getApplicationContext());
        if(user!=null){
            Log.d(TAG, "onOpen: no need for authentication already signed in ");
                getExistingActivity();
        }else{
            if(foregroundService!=null) {
                Log.d(TAG, "onOpen: no need for authentication stopping service ");
                foregroundService.stopForeground(true);
                foregroundService.stopSelf();
            }
        }
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
        byte[] data=bytes.toByteArray();
        foregroundService.SendFileData(data);
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
        Log.d(TAG, "onMessage: "+text);
        if(text.contains("username")&&text.contains("password")){
            Log.d(TAG, "onMessage: edited "+text);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(text);
                String key1 = jsonObject.getString("username");
                String key2 = jsonObject.getString("password");
                int key3 = jsonObject.getInt("user_id");
                String usermode=jsonObject.getString("usermode");
                Log.d(TAG, "onMessage: the converted values are "+key1+" "+key2+" "+usermode);
                User user=new User(key1,key2,usermode);
                user.setUser_id(key3);
                Log.d(TAG, "onMessage: functioning ");
                LoginActivity loginActivity=(LoginActivity)context;
                loginActivity.NextPage(user);
                Log.d(TAG, "onMessage: updating " );
            } catch (JSONException e) {
                Log.d(TAG, "onMessage: exception occuered while converting to json ");
                e.printStackTrace();
            }
        }else if(text.contains("activity_id")&&!text.contains("message")){
            if(text.contains("task_id")){
                Log.d(TAG, "onMessage: edited " + text);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(text);
                    int activityId = jsonObject.getInt("activity_id");
                    int task_id = jsonObject.getInt("task_id");
                    String assignedBy = jsonObject.getString("activity_assigned_by");
                    String activityName = jsonObject.getString("activity_name");
                    String activityDescription = jsonObject.getString("activity_description");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date=sdf.parse(jsonObject.getString("activity_issued"));
                    Task task=new Task();
                    task.setActivity_id(activityId);
                    task.setTask_id(task_id);
                    task.setActivityCreator(assignedBy);
                    task.setActivityName(activityName);
                    task.setIssuedDate(date);
                    task.setActivityDescrption(activityDescription);
                    foregroundService.setTask(task);
                } catch (JSONException e) {
                    Log.d(TAG, "onMessage: exception occuered while converting to json ");
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else {
                Log.d(TAG, "onMessage: edited " + text);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(text);
                    int activityId = jsonObject.getInt("activity_id");
                    String activityDescrption = jsonObject.getString("activity_descrption");
                    int actvity_status_id = jsonObject.getInt("actvity_status_id");
                    int componentId = jsonObject.getInt("activity_component_id");
                    int machineId = jsonObject.getInt("activity_machine_id");
                    int scheduleId = jsonObject.getInt("activity_schedule_id");
                    String activityName = jsonObject.getString("activity_name");
                    String issued_date = jsonObject.getString("activity_issued_date");
                    int activityAssignedId = jsonObject.getInt("assigned_to");
                    String activityAssignUser = jsonObject.getString("assigned_to_user");
                    String ui_activityChange = jsonObject.getString("ui_change");
                    String activityChange = jsonObject.getString("change");
                    String activityCreator = jsonObject.getString("activity_creator");
                    Activity activity = new Activity();
                    activity.activityId = activityId;
                    activity.activityDescription = activityDescrption;
                    activity.activity_satuts_id = actvity_status_id;
                    activity.componentId = componentId;
                    activity.machineId = machineId;
                    activity.scheduleId = scheduleId;
                    activity.activityName = activityName;
                    activity.issued_date = issued_date;
                    activity.assigned_to = activityAssignedId;
                    activity.assigned_to_user = activityAssignUser;
                    activity.activityCreator = activityCreator;
                    activity.change = activityChange;
                    activity.uiChange = ui_activityChange;
                    Log.d(TAG, "onMessage: functioning activity received " + activityName + " " + activityAssignedId);
                    Log.d(TAG, "onMessage: activity changes " + ui_activityChange + " " + activityChange);
                    Log.d(TAG, "onMessage: activity changes " + ui_activityChange + " " + activityChange);
                    Log.d(TAG, "onMessage: foregroundService instance is.... "+foregroundService);
                    foregroundService.setExistingActivity(activity);
                    Log.d(TAG, "onMessage: updating ");
                } catch (JSONException e) {
                    Log.d(TAG, "onMessage: exception occuered while converting to json ");
                    e.printStackTrace();
                }
            }
        }else if(text.contains("message") && text.contains("activity_id") && text.contains("create")||text.contains("update")){
            try{
            // Parse the JSON string into a JSONObject
                JSONObject jsonObject = new JSONObject(text);
                // Extract values from the JSONObject
                String type = jsonObject.getString("type");
                String message = jsonObject.getString("message");
                Log.d(TAG, "onMessage: message part of activity create"+message);
                // Parse the message JSON string into another JSONObject
                JSONObject messageObject = new JSONObject(message);
                // Extract values from the message JSONObject
                int activityId = messageObject.getInt("activity_id");
                String activityDescription = messageObject.getString("activity_descrption");
                int activityStatusId = messageObject.getInt("actvity_status_id");
                int activityComponentId = messageObject.getInt("activity_component_id");
                int activityMachineId = messageObject.getInt("activity_machine_id");
                int activityScheduleId = messageObject.getInt("activity_schedule_id");
                String activityName = messageObject.getString("activity_name");
                String activityIssuedDate = messageObject.getString("activity_issued_date");
                String activityChange = messageObject.getString("change");
                String activityAssigned= messageObject.getString("assigned_to_user");
                String ui_activityChange = messageObject.getString("ui_change");
                int activityAssignedId = messageObject.getInt("assigned_to_id");
                String activityCreator = messageObject.getString("activity_creator");
                Activity activity=new Activity();
                activity.activityName=activityName;
                activity.activityDescription=activityDescription;
                activity.activity_satuts_id=activityStatusId;
                activity.componentId=activityComponentId;
                activity.machineId=activityMachineId;
                activity.scheduleId=activityScheduleId;
                activity.issued_date=activityIssuedDate;
                activity.assigned_to=activityAssignedId;
                activity.assigned_to_user=activityAssigned;
                activity.activityCreator=activityCreator;
                activity.change=activityChange;
                activity.uiChange=ui_activityChange;
                activity.activityId=activityId;
                Log.d(TAG, "onMessage: activity changes "+activityAssignedId+" "+activityChange+" "+activity.activityId);
                foregroundService.setExistingActivity(activity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (text.contains("type")) {
            try {
                JSONObject jsonObject=new JSONObject(text);
                int id=jsonObject.getInt("id");
                String type=jsonObject.getString("type");
                String name=jsonObject.getString("name");
                if(type.equals("machine")){
                    Machine machine=new Machine();
                    machine.setName(name);
                    machine.setId(id);
                    foregroundService.setMachine(machine,false);
                } else if (type.equals("component")) {
                    Machine component=new Machine();
                    component.setId(id);
                    component.setName(name);
                    foregroundService.setComponent(component,false);
                } else if (type.equals("schedule")) {
                    Log.d(TAG, "onMessage: schedule object got from the server ");
                    Machine schedule=new Machine();
                    schedule.setId(id);
                    schedule.setName(name);
                    schedule.setValue(jsonObject.getInt("value"));
                    schedule.setCurrentvalue(jsonObject.getInt("current"));
                    foregroundService.setSchedule(schedule,false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (text.contains("username")&&text.contains("userid")) {
            try {
                JSONObject jsonObject=new JSONObject(text);
                String username=jsonObject.getString("username");
                String key=jsonObject.getString("key");
                int user_id=jsonObject.getInt("userid");
                User user=new User(username,"password","B");
                user.setUser_id(user_id);
                Log.d(TAG, "onMessage: "+key+" "+user.getUsername());
                foregroundService.setUsers(user,key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (text.contains("callback")&& text.contains("model")) {
            try {
                JSONObject jsonObject=new JSONObject(text);
                String callback=jsonObject.getString("callback");
                String model=jsonObject.getString("model");
                Log.d(TAG, "onMessage: callback and model "+callback+" "+model);
                if(callback.equals("created")){
                    Log.d(TAG, "onMessage: callback is "+callback);
                    foregroundService.updateUi(callback,model);
                } else if (callback.equals("update")) {
                    foregroundService.updateUi(callback,model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (text.contains("report")) {
            JSONObject jsonObject= null;
            try {
                jsonObject = new JSONObject(text);
                String reportFrom=jsonObject.getString("report_from");
                int reportUserId=jsonObject.getInt("report_user_id");
                JSONObject reportsObject = jsonObject.getJSONObject("reports");
                JSONArray textArray = reportsObject.getJSONArray("text");
                JSONArray audioArray = reportsObject.getJSONArray("audio");
                JSONArray imageArray = reportsObject.getJSONArray("image");
                String[] images = new String[imageArray.length()];
                String[] audios=new String[audioArray.length()];
                String[] texts=new String[textArray.length()];
                for (int i = 0; i < imageArray.length(); i++) {
                    String imagePath = imageArray.getString(i);
                    images[i]=imagePath;
                    Log.d(TAG, "onMessage: imageArray..."+imagePath.replace("\"",""));
                }
                for (int i = 0; i < audioArray.length(); i++) {
                    String audioPath = imageArray.getString(i);
                    audios[i]=audioPath;
                    Log.d(TAG, "onMessage: imageArray..."+audioPath.replace("\"",""));
                }
                for (int i = 0; i < textArray.length(); i++) {
                    String textPath = imageArray.getString(i);
                    texts[i]=textPath;
                    Log.d(TAG, "onMessage: imageArray..."+textPath.replace("\"",""));
                }
                Log.d(TAG, "onMessage: reportTransfer is..."+newWindow);
                newWindow.setAudio(audios);
                newWindow.setImage(images);
                newWindow.setText(texts);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (text.contains("file-size")) {
            try {
                JSONObject jsonObject=new JSONObject(text);
                int size=jsonObject.getInt("file-size");
                Log.d(TAG, "onMessage: file size is ..."+size);
                foregroundService.fileSize(size);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void getUsers(CharSequence sequence){
        User user=UserPreferences.getUser(context.getApplicationContext());
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("username",user.getUsername());
            jsonObject.put("password",user.getPassword());
            jsonObject.put("usermode",user.getUsermode());
            jsonObject.put("users",sequence);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webSocket.send(jsonObject.toString());
    }
    @Override
    public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
        Log.d(TAG, "onClosed: websocket closed connection ");
    }

    @Override
    public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
        Log.e(TAG, "onFailure: ", t);
        scheduleReconnect();
    }
    private void scheduleReconnect() {
        if (retryCount < maxRetries) {
            retryCount++;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.d(TAG, "Retrying connection...");
                    connect();
                }
            }, 5000); // Retry after 5 seconds
        } else {
            Log.e(TAG, "Max retries reached. Unable to connect to server.Stopping service so it may initialed on restart of app");
            // Perform any actions needed after reaching maximum retries
            foregroundService.stopForeground(true);
            foregroundService.stopSelf();
        }
    }

    public void getTask() {
        Log.d(TAG, "getTask: getting task from the server...");
        User user=UserPreferences.getUser(context.getApplicationContext());
        JSONObject jsonObject=new JSONObject();
        try {
            if (user != null) {
                jsonObject.put("username",user.getUsername());
                jsonObject.put("password",user.getPassword());
                jsonObject.put("usermode",user.getUsermode());
                jsonObject.put("user_id",user.getUser_id());
                jsonObject.put("task","task");
                webSocket.send(jsonObject.toString());
            }else{
                Log.d(TAG, "getSchedules: User not defined yet ");
            }
        } catch (JSONException e) {
            Log.d(TAG, "Auth: ",e);;
        }
    }

    public void sendReport(ArrayList<SubmitHolder> submitHolders, Context context, int taskId) {
        if(submitHolders.size()>0){
            int i=0;
            for (SubmitHolder s :
                    submitHolders) {
               JSONObject jsonObject=new JSONObject();
                try {
                    if(s.getImageFile()==null&&s.getFile()==null) {
                        byte[] bytes=s.getTextView().getBytes();
                        JSONObject textData = new JSONObject();
                        textData.put("id", i);
                        textData.put("type", "text");
                        String base64Data = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
                        textData.put("uploaded", 0);
                        textData.put("size",bytes.length);
                        textData.put("content", base64Data);
                        jsonObject.put("report_id",taskId);
                        jsonObject.put("task_id",taskId);
                        jsonObject.put("total_item",submitHolders.size()-1);
                        Log.d(TAG, "sendReport: uploaded size and total size "+base64Data.length()+" "+base64Data.length());
                        User user=UserPreferences.getUser(context.getApplicationContext());
                        if (user != null) {
                            jsonObject.put("user_id",user.getUser_id());
                        }else {
                            Log.d(TAG, "sendReport: user is null...");
                        }
                        jsonObject.put("report", textData);
                        webSocket.send(jsonObject.toString());
                    } else if (s.getTextView() == null && s.getFile() == null) {
                        int len;
                        float upload=0;
                        FileInputStream fileInputStream= (FileInputStream) context.getContentResolver().openInputStream(s.getImageFile());
                        byte[] b =new byte[1024];
                        ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(s.getImageFile(), "r");
                        long size = parcelFileDescriptor.getStatSize();
                        parcelFileDescriptor.close();
                        if (fileInputStream != null) {
                            while ((len=fileInputStream.read(b))!=-1) {
                                String base64Data = android.util.Base64.encodeToString(b,0,len, android.util.Base64.DEFAULT);
                                Log.d(TAG, "sendReport: string size :  "+len+" "+b.length);
                                JSONObject imageData = new JSONObject();
                                imageData.put("id",i);
                                imageData.put("type", "image");
                                imageData.put("content",base64Data);
                                imageData.put("uploaded",upload);
                                imageData.put("size",size);
                                jsonObject.put("report", imageData);
                                jsonObject.put("report_id",taskId);
                                jsonObject.put("total_item",submitHolders.size()-1);
                                jsonObject.put("task_id",taskId);
                                User user=UserPreferences.getUser(context.getApplicationContext());
                                if (user != null) {
                                    jsonObject.put("user_id",user.getUser_id());
                                }else {
                                    Log.d(TAG, "sendReport: user is null...");
                                }
                                webSocket.send(jsonObject.toString());
                                upload=upload+len;
                            }
                            fileInputStream.close();
                        }
                    } else if (s.getTextView() == null && s.getImageFile() == null) {
                        File file=s.getFile();
                        if(file!=null) {
                            FileInputStream fileInputStream = new FileInputStream(file);
                            int len = 0;
                            float upload=0;
                            long size=file.length();
                            byte[] b=new byte[1024];
                            while((len=fileInputStream.read(b))!=-1){
                                String base64Data = android.util.Base64.encodeToString(b,0,len, android.util.Base64.DEFAULT);
                                Log.d(TAG, "sendReport: string size :  "+len+" "+b.length);
                                JSONObject audioData = new JSONObject();
                                audioData.put("id",i);
                                audioData.put("type", "audio");
                                audioData.put("content",base64Data);
                                audioData.put("uploaded",upload);
                                audioData.put("size",size);
                                jsonObject.put("report", audioData);
                                jsonObject.put("report_id",taskId);
                                jsonObject.put("total_item",submitHolders.size()-1);
                                jsonObject.put("task_id",taskId);
                                User user=UserPreferences.getUser(context.getApplicationContext());
                                if (user != null) {
                                    jsonObject.put("user_id",user.getUser_id());
                                }else {
                                    Log.d(TAG, "sendReport: user is null...");
                                }
                                webSocket.send(jsonObject.toString());
                                upload=upload+len;
                            }
                        }
                    }

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }
    }
    public static byte[] readBytesFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileBytes = new byte[(int) file.length()];
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(fileBytes);
        }
        return fileBytes;
    }
    public static String encodeBytesToBase64(byte[] bytes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(bytes);
        }
        return null;
    }

    public void getReport(ReportTransfer newWindow, int activityId) {
        this.newWindow=newWindow;
        User user=UserPreferences.getUser(context.getApplicationContext());
        JSONObject jsonObject=new JSONObject();
        try {
            if (user != null) {
                jsonObject.put("user_id",user.getUser_id());
                jsonObject.put("username",user.getUsername());
                jsonObject.put("report-get",activityId);
                webSocket.send(jsonObject.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getFile(String file, int position) {
        User user=UserPreferences.getUser(context.getApplicationContext());
        JSONObject jsonObject=new JSONObject();
        try {
            if (user != null) {
                jsonObject.put("user_id",user.getUser_id());
                jsonObject.put("username",user.getUsername());
                jsonObject.put("file-get",file);
                webSocket.send(jsonObject.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webSocket.send(jsonObject.toString());
    }
}
