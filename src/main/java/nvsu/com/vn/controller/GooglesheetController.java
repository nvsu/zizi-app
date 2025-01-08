package nvsu.com.vn.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mortbay.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;

import nvsu.com.vn.service.GoogleSheetService;

@RestController
@RequestMapping("/api")
public class GooglesheetController {

	private static final Logger logger = LoggerFactory.getLogger(GooglesheetController.class);

	@Autowired
	private GoogleSheetService googleSheetService;

	private static final String SPREADSHEET_ID = "1B_KkCNUt4YxA4QSJZjhiVR9mHPCjJLtsH5DZ-AN9CEs";

//	@PostMapping("/sheets/data")
//	public ResponseEntity<String> getSheetsData(@RequestBody Map<String, Object> data)
//			throws IOException, GeneralSecurityException {
//		
//		// Get parameters value
//		String ret  ="OK";
//		String sheet_name = (String) data.get("sheet_name");
//		String page = (String) data.get("page");
//		logger.debug("S::{}-{}", sheet_name, page);
//		if (null == data.get("json")) {
//			ret = googleSheetService.getData(SPREADSHEET_ID, sheet_name, page);
//		} else {
//			String json = (String) data.get("json");
//			logger.debug("Write::{}-{}", sheet_name, page);
//			googleSheetService.writeData(SPREADSHEET_ID, sheet_name, page, json);
//		}
//		//logger.debug("E::{}-{}", sheet_name, page);
//		return ResponseEntity.ok(ret);
//	}
	
	@GetMapping("/sheets/store")
	public ResponseEntity<String> getDataLinesWithDefault()
			throws IOException, GeneralSecurityException {
		
		// Get parameters value
		String sheet_name = "store";
		String column_name = "J";
		
		logger.debug("S::{}-{}", sheet_name, column_name);
		JSONArray result = googleSheetService.getAllLines(SPREADSHEET_ID, sheet_name, column_name);
		
		//logger.debug("E::{}-{}", sheet_name, page);
		return ResponseEntity.ok(result.toString());
	}
	
	
	@PostMapping("/sheets/lines")
	public ResponseEntity<List<String>> getDataLines(@RequestBody Map<String, Object> data)
			throws IOException, GeneralSecurityException {
		
		// Get parameters value
		String sheet_name = (String) data.get("sheet_name");
		String page = (String) data.get("page");
		logger.debug("S::{}-{}", sheet_name, page);
		List<String> result = new ArrayList<>();
		if (null == data.get("json")) {
			result = googleSheetService.getDataLines(SPREADSHEET_ID, sheet_name, page, 5);
		} else {
			String json = (String) data.get("json");
			logger.debug("Write::{}-{}", sheet_name, page);
			logger.debug("{}", json);
			googleSheetService.writeData(SPREADSHEET_ID, sheet_name, page, json);
		}
		
		//logger.debug("E::{}-{}", sheet_name, page);
		return ResponseEntity.ok(result);
	}
	
	
	@GetMapping("/sheets/test/update")
	public ResponseEntity<String> updateTestData() throws IOException, GeneralSecurityException {
		
//		String path ="D:\\Working\\Study\\KHoi\\zizi\\english27\\assets\\books\\27\\%s\\data\\";
//		String sheetName = "";
//		String folderPath   = "";
		
//		sheetName = "student";
//		String folderPath = String.format(path, sheetName);
//		googleSheetService.processJsonFiles(folderPath, SPREADSHEET_ID, sheetName);
		
//		sheetName = "work";
//		folderPath = String.format(path, sheetName);
//		googleSheetService.processJsonFiles(folderPath, SPREADSHEET_ID, sheetName);
		
		
//		sheetName = "dict";
//		folderPath = String.format(path, sheetName);
//		googleSheetService.processJsonFiles(folderPath, SPREADSHEET_ID, sheetName);
		
		logger.debug("Start::");
		return ResponseEntity.ok("OK");
	}
	
	@RequestMapping("/drive/get")
	public ResponseEntity<String> getGoogleDriveFiles() throws IOException, GeneralSecurityException {
		String folderId = "1eAm7KqecF4B3ibWC51B6_YZaW6nUXfWX"; // folder cha
		String bai_hoc_id = "1Q2EwRbpW0TDs8NeWMkapjen6HxlAkiY9"; // bai hoc
		googleSheetService.getGoogleDriveFiles(bai_hoc_id);
		return ResponseEntity.ok("OK");
	}
	
	

//	    @PostMapping("/sheets/write")
//	    public ResponseEntity<String>  setData(@RequestBody Map<String, Object> data) throws IOException, GeneralSecurityException {
//
//	    	String sheet_name = (String) data.get("sheet_name");
//	    	String page = (String) data.get("page");
//	    	String json = (String) data.get("json");
//	    	
//	    	googleSheetService.writeData(SPREADSHEET_ID, sheet_name, page, json);
//	        return   ResponseEntity.ok("OK");
//	    }

}
