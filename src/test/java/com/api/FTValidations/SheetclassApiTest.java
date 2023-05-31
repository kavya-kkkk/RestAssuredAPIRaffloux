package com.api.FTValidations;

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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class SheetclassApiTest {
	
	private static final String FILE_NAME = "C:\\Users\\desktop\\eclipse\\RestAssured\\Api-ValidationPN\\RR-Raffloux-RestApi.xlsx";
	private static final String[] SHEET_NAMES = { "factoA", "factoB", "factoC" };
	private ExtentReports extent;
	private ExtentTest test;
	public String jwtToken;

	@BeforeSuite

	public void setUp() {
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(
				"C:\\Users\\desktop\\eclipse\\RestAssured\\RafflouxAPI-HTML-Report\\ApiTestreport2.html");
	 	extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
	}

	@BeforeMethod
	public void beforeMethod() {
		test = extent.createTest("Raffloux-ReDev Rest API", "API Testing both Positive and negative");
	}

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
	 
		RestApiLoginValidations(param1, param2, param3, param4, param5);
	}
 
	public void RestApiLoginValidations(String Method, String EndPoint, String Payload, double ExpectedStatusCodeDouble,
			String ExpectedResponseBody)
 
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


		try {
			Assert.assertEquals(ActualStatusCode, ExpectedStatusCode, "Expected status code is  Incorrect");
			test.log(Status.PASS, "Assertion passed");

		} catch (AssertionError e) {

			test.log(Status.FAIL, "Assertion failed: " + e.getMessage());
			System.err.println("Test failed: Assertion failed");
		}

	

	
	
	
	try {
		// To print response body
	Assert.assertEquals(ActualResponseBody.contains(ExpectedResponseBody), true, "Incorrect Expected body");

		test.log(Status.PASS, "Assertion passed");

	} catch (AssertionError e) {

		test.log(Status.FAIL, "Assertion failed: " + e.getMessage());
		System.err.println("Test failed: Assertion failed");
	}

	test.log(Status.PASS, Method+"  " +"Row data Executed successfully");

}

	
	
	@AfterMethod
	public void afterMethod() {
		extent.flush();
	}

	@AfterSuite
	public void tearDown() {
		extent.flush();
	}

	
}


