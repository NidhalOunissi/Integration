package tn.esprit.clubconnect.services;

/*import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import java.io.FileInputStream;
import java.io.IOException;*/

public class FCMClient {

  /*  public static void initFCM() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("path/to/serviceAccountKey.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://your-project-id.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
    }

    public static void sendNotification(String token, String title, String body) {
        // Create a message
        Message message = Message.builder()
                .setToken(token)
                .putData("title", title)
                .putData("body", body)
                .build();

        // Send a message to the device corresponding to the provided FCM token
        String response = FirebaseMessaging.getInstance().send(message);
        // Handle the response as needed
    }*/
}

