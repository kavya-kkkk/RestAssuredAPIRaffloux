package com.Dataprovider.ExcelReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class DataProviderSheetExcelReader {
	private static final String FILE_NAME = "C:\\Users\\desktop\\eclipse\\PracticeRestAPi\\src\\test\\resources\\Api-ValidationPN\\LoginApiSheets.xlsx";
	private static final String[] SHEET_NAMES = { "factoA", "factoB", "factoC" };

	@DataProvider(name = "RestApi")

	public static Object[][] readExcelData() throws IOException {
		List<Object[]> data = new ArrayList<>();
		File file = new File(FILE_NAME);
		FileInputStream inputStream = new FileInputStream(file);
		Workbook workbook = WorkbookFactory.create(inputStream);

		for (String sheetName : SHEET_NAMES) {
			Sheet sheet = workbook.getSheet(sheetName);
			int rowCount = sheet.getLastRowNum();

			for (int i = 1; i <= rowCount; i++) { // start from second row
				Row row = sheet.getRow(i);
		 		Object[] rowValues = new Object[row.getLastCellNum()];

				boolean hasNullValues = false; // Flag to track null values

				for (int j = 0; j < row.getLastCellNum(); j++) {
					Cell cell = row.getCell(j, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

					if (cell != null) {
						rowValues[j] = getCellValue(cell);
					} else {
						rowValues[j] = null;
						hasNullValues = true; // Set the flag if a null value is encountered
					}
				}

				if (!hasNullValues) { // Add the row only if it does not have null values
					data.add(rowValues);
				}
			}
		}

		return data.toArray(new Object[0][0]);
	}

	private static Object getCellValue(Cell cell) {
		CellType cellType = cell.getCellType();
		switch (cellType) {
		case BOOLEAN:
			return cell.getBooleanCellValue();
		case NUMERIC:
			return cell.getNumericCellValue();
		case STRING:
			return cell.getStringCellValue();
		default:
			return null;
		}
	}

	@Test(dataProvider = "RestApi")

	public void myTest(Object[] rowData) {
		String param1 = (rowData[0] != null) ? ((String) rowData[0]).trim() : null;
		String param2 = (rowData[1] != null) ? ((String) rowData[1]).trim() : null;
		String param3 = (rowData[2] != null) ? ((String) rowData[2]).trim() : null;
		Double param4 = (rowData[3] != null) ? (Double) rowData[3] : null;
		String param5 = (rowData[4] != null) ? ((String) rowData[4]).trim() : null;
	 	String param6 = (rowData[5] != null) ? ((String) rowData[5]).trim() : null;

		RestApiLoginValidations(param1, param2, param3, param4, param5, param6);
	}
 
	public void RestApiLoginValidations(String Method, String EndPoint, String Payload, double ExpectedStatusCodeDouble,
			String ExpectedResponseBody, String ExpectedHeaderContentType)
 
	{

		Response response = RestAssured

				.given()

				.baseUri("https://91fjgvixl9.execute-api.ap-south-1.amazonaws.com/testing")
				.contentType(ContentType.JSON).body(Payload)

				.when()

				.post(EndPoint)

				.then()

				.extract()

				.response();
		// To ActualStatusCode from response body and To Print
		int ActualStatusCode = response.getStatusCode();

		// To ActualResponseBody from response body and To print
		String ActualResponseBody = response.getBody().asString();

		// To ActualHeaderContentType from response body and To print
		String ActualHeaderContentType = response.header("Content-Type");

		// To convert from double to integer (while fetching will get integer as double)
		// and to print
		int ExpectedStatusCode = (int) ExpectedStatusCodeDouble;

		// To print and To validate both (exp and act status code)
		Reporter.log("Actual status code is " + "=>" + ActualStatusCode + "=>" + "-AND-" + "expected status code" + "=>"
				+ ExpectedStatusCode);
		Assert.assertEquals(ActualStatusCode, ExpectedStatusCode, "Expected status code is  Incorrect");

		// To print response body
		Reporter.log("Actual Response body" + " =>" + ActualResponseBody + "=>" + "Expected Response Body is" + "=>"
				+ ExpectedResponseBody);
		
		Assert.assertEquals(ActualResponseBody.contains(ExpectedResponseBody), true, "Incorrect Expeted body");

		// To print headers ActualHeaderContentType
		Reporter.log("Actual Response body" + " =>" + ActualHeaderContentType + "=>" + "Expected Response Body is"
				+ "=>" + ExpectedHeaderContentType);
		Assert.assertEquals(ActualHeaderContentType.contains(ExpectedHeaderContentType), true,
				"Incorrect  Expected Header ContentType");
		System.out.println(" status code is " + ActualStatusCode);

	}
}
