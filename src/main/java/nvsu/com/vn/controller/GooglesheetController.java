package nvsu.com.vn.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nvsu.com.vn.service.GoogleSheetService;

@RestController
@RequestMapping("/api")
public class GooglesheetController {
	
	    @Autowired
	    private GoogleSheetService googleSheetService;

	    @GetMapping("/sheets/data")
	    public List<List<Object>> getData() throws IOException, GeneralSecurityException {
	        return googleSheetService.getSheetData();
	    }
}
