package nvsu.com.vn.service;


import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.micrometer.common.util.StringUtils;
import nvsu.com.vn.util.GoogleAuthorizeUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class GoogleSheetService {

	private static final Logger logger = LoggerFactory.getLogger(GoogleSheetService.class);

    public String getData(String SPREADSHEET_ID, String sheetName, String page) throws IOException, GeneralSecurityException {

    	String RANGE = String.format("%s!B%s", sheetName, page);
        Sheets service = GoogleAuthorizeUtil.getSheetsService();
        ValueRange response = service.spreadsheets().values().get(SPREADSHEET_ID, RANGE).execute();
       
        try {
			return response.getValues().get(0).get(0).toString();
		} catch (Exception e) {
			logger.debug("not found value");
		}
        return "";
    }
    
public JSONArray getAllLines(String SPREADSHEET_ID, String sheetName, String columnName) throws IOException, GeneralSecurityException {
    	// "%s!B%s:B%s",
    	String columnFormat = "%s!" + columnName + "2:" + columnName ;

    	// Tạo RANGE cho nhiều dòng
        String RANGE = String.format(columnFormat, sheetName);
        Sheets service = GoogleAuthorizeUtil.getSheetsService();
        ValueRange response = service.spreadsheets().values().get(SPREADSHEET_ID, RANGE).execute();

        JSONArray result = new JSONArray();
        
        try {
            // Kiểm tra xem response có giá trị hay không
            if (response.getValues() != null) {
                for (List<Object> row : response.getValues()) {
                    // Thêm từng giá trị trong hàng vào danh sách kết quả
                	String cell = row.get(0).toString();
                	if (StringUtils.isNotEmpty(cell)) {
                		try {
							JSONObject jsonObject = new JSONObject(cell);
							result.put(jsonObject);
						} catch (Exception e) {
							 logger.error(e.getMessage());
						}
                	}
                	
                }
            } else {
                logger.debug("No values found in the specified range.");
            }
        } catch (Exception e) {
            logger.debug("Error retrieving values: {}", e.getMessage());
            throw e;
        }

        return result;
    }

    
    public List<String> getDataLines(String SPREADSHEET_ID, String sheetName, String startPage, int numberOfRows) throws IOException, GeneralSecurityException {
    	
    	String columnStart = "B";
    	String columnEnd = "B";
    	
    	// "%s!B%s:B%s",
    	String columnFormat = "%s!" + columnStart + "%s:" + columnEnd+ "%s";
    	
    	// Tạo RANGE cho nhiều dòng
        String RANGE = String.format(columnFormat, sheetName, startPage, Integer.parseInt(startPage) + numberOfRows - 1);
        Sheets service = GoogleAuthorizeUtil.getSheetsService();
        ValueRange response = service.spreadsheets().values().get(SPREADSHEET_ID, RANGE).execute();

        List<String> result = new ArrayList<>();
        
        try {
            // Kiểm tra xem response có giá trị hay không
            if (response.getValues() != null) {
                for (List<Object> row : response.getValues()) {
                    // Thêm từng giá trị trong hàng vào danh sách kết quả
                    result.add(row.get(0).toString());
                }
            } else {
                logger.debug("No values found in the specified range.");
            }
        } catch (Exception e) {
            logger.debug("Error retrieving values: {}", e.getMessage());
        }

        return result;
    }
    
    public void writeData(String SPREADSHEET_ID, String sheetName, String page, String value) throws IOException, GeneralSecurityException {

          String RANGE = String.format("%s!B%s", sheetName, page);
    	  List<List<Object>> values = Collections.singletonList(
                  Collections.singletonList(value)
          );
    	  Sheets service = GoogleAuthorizeUtil.getSheetsService();
    	  ValueRange body = new ValueRange().setValues(values);
          service.spreadsheets().values()
                  .update(SPREADSHEET_ID, RANGE, body)
                  .setValueInputOption("RAW")
                  .execute();
    }
    

    
    public List<File> getGoogleDriveFiles(String folderId) throws IOException, GeneralSecurityException {
        Drive service = GoogleAuthorizeUtil.getDriveService();

        // Truy vấn chỉ lấy file JSON từ folder
//        String query = String.format("'%s' in parents and mimeType='application/json'", folderId);
//        String query = String.format("'%s' in parents and (mimeType contains 'image/' or mimeType contains 'video/')", folderId);
        String query = String.format("'%s' in parents", folderId);
        List<File> jsonFiles = new ArrayList<>();

        FileList result = service.files().list()
                .setQ(query)
                .setFields("files(id, name, description,  modifiedTime, fileExtension)")
                .setOrderBy("modifiedTime desc")
                .execute();

        jsonFiles.addAll(result.getFiles());
        return jsonFiles;
    }
    
    
    public void processJsonFiles(String folderPath, String SPREADSHEET_ID, String sheetName) throws IOException, GeneralSecurityException {
        Path dirPath = Paths.get(folderPath);
        
        // Regular expression to match file names like "1.json", "2.json", etc.
        Pattern pattern = Pattern.compile("(\\d+)\\.json");

        // Loop through each JSON file in the folder
        Files.list(dirPath)
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".json"))
                .forEach(path -> {
                    Matcher matcher = pattern.matcher(path.getFileName().toString());
                    if (matcher.matches()) {
                        // Extract page number from file name
                        String pageNumber = matcher.group(1); // This is the page number as a string
                        try (FileReader reader = new FileReader(path.toFile())) {
                            // Read JSON content and parse into JsonObject
                            JsonElement jsonElement = JsonParser.parseReader(reader);
                            if (jsonElement.isJsonObject()) {
                                JsonObject jsonObject = jsonElement.getAsJsonObject();
                                String value = jsonObject.toString(); // Or choose specific fields if needed

                                // Call writeData to write the value to the Google Sheet at the extracted page number
                                writeData(SPREADSHEET_ID, sheetName, pageNumber, value);
                            }
                        } catch (Exception e) {
                            logger.error("Error processing file {}: {}", path, e.getMessage());
                        }
                    } else {
                        logger.warn("File {} does not match the expected pattern and will be skipped.", path.getFileName());
                    }
                });
    }
}
