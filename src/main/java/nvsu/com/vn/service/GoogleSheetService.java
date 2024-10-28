package nvsu.com.vn.service;


import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import nvsu.com.vn.util.GoogleAuthorizeUtil;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class GoogleSheetService {

    private static final String SPREADSHEET_ID = "1vji93GLC6VRsWHvfkdF_8ScojCVkR26h0RJtDSK7Wdw";
    private static final String RANGE = "Sheet7!A1:D10"; // Adjust range as needed

    public List<List<Object>> getSheetData() throws IOException, GeneralSecurityException {
        Sheets service = GoogleAuthorizeUtil.getSheetsService();
        ValueRange response = service.spreadsheets().values()
                .get(SPREADSHEET_ID, RANGE)
                .execute();
        return response.getValues();
    }
}
