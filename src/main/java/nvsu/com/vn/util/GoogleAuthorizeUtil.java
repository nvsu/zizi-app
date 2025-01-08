package nvsu.com.vn.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;


import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.core.io.ClassPathResource;

import org.springframework.core.io.ClassPathResource;

public class GoogleAuthorizeUtil {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static Sheets sheetsService;

    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        if (sheetsService == null) {
            // Đường dẫn tương đối trong thư mục resources
            String path = "google-sheets-client-secret.json";
            ClassPathResource resource = new ClassPathResource(path);
            
            // Tải tệp từ classpath
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY, new InputStreamReader(resource.getInputStream())
            );

            GoogleCredential credential = GoogleCredential.fromStream(resource.getInputStream())
                    .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
            
            sheetsService = new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential
            ).setApplicationName("Google Sheets Example").build();
        }
        return sheetsService;
    }
    
    
    public static Drive getDriveService() throws GeneralSecurityException, IOException {
    	
        // Đường dẫn tương đối trong thư mục resources
        String path = "google-sheets-client-secret.json";
        ClassPathResource resource = new ClassPathResource(path);
        
        GoogleCredential credential = GoogleCredential.fromStream(resource.getInputStream())
        											.createScoped(Collections.singleton(DriveScopes.DRIVE_READONLY));
        
        
        return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName("getDriveService")
                .build();
    }
    
   
}