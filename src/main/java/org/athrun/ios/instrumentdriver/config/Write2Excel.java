package org.athrun.ios.instrumentdriver.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;

public class Write2Excel {
//	 private static Logger logger = Logger.getLogger("Write2Excel");

	private static String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	private static String sPath = System.getProperty("user.dir") + "/target/";
	private static String _ReportXls = sPath + "移动端测试IOS-乐乘" + date + ".xls";

	public static void ExcelCreate(String excelFilePath, String SheetName) {
		HSSFWorkbook wbook;
		HSSFSheet sheet;
		String[] columnnName = { "案例编号","编号备注", "预期输出", "类名", "方法名", "测试结果", "实际输出", "输入项", "测试时间", };
		int columnNum = 0;
		try {
			File file = new File(excelFilePath);
			if (!file.exists()) {
				wbook = new HSSFWorkbook();
				sheet = wbook.createSheet(SheetName);
				sheet.createFreezePane(0, 1);
				sheet.setColumnWidth(0, 5000);
				sheet.setColumnWidth(1, 3000);
				sheet.setColumnWidth(2, 5500);
				sheet.setColumnWidth(3, 7000);
				sheet.setColumnWidth(4, 3000);
				sheet.setColumnWidth(5, 2000);
				sheet.setColumnWidth(6, 5500);
				sheet.setColumnWidth(7, 5500);
				sheet.setColumnWidth(8, 5500);
				columnNum = columnnName.length;
				HSSFRow row = sheet.createRow(0);
				for (int i = 0; i < columnNum; i++) {
					HSSFCell nCell = row.createCell(i);
					nCell.setCellStyle(setTitleStytle(wbook));
					nCell.setCellValue(columnnName[i]);
				}
				// 设置首行自动筛选
				CellRangeAddress m = new CellRangeAddress(0, 1, 0, 10);
				sheet.setAutoFilter(m);
				FileOutputStream fileOut = new FileOutputStream(excelFilePath);
				wbook.write(fileOut);
				fileOut.flush();
				fileOut.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * @author yan.yan 设置Excel Report首行格式
	 * @param wbook
	 * @return Excel 首行风格
	 */
	public static HSSFCellStyle setTitleStytle(HSSFWorkbook wbook) {
		HSSFPalette customPalette = wbook.getCustomPalette();
		// 设置为绿色背景色
		customPalette.setColorAtIndex(HSSFColor.GREEN.index, (byte) 204, (byte) 255, (byte) 255);

		Font font = wbook.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);

		HSSFCellStyle cStyle = wbook.createCellStyle();
		cStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cStyle.setFillForegroundColor(HSSFColor.GREEN.index);
		cStyle.setAlignment(CellStyle.ALIGN_CENTER);

		cStyle.setBorderBottom((short) 1);
		cStyle.setBorderLeft((short) 1);
		cStyle.setBorderRight((short) 1);
		cStyle.setBorderTop((short) 1);
		cStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		cStyle.setRightBorderColor(HSSFColor.BLACK.index);
		cStyle.setTopBorderColor(HSSFColor.BLACK.index);
		cStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		cStyle.setFont(font);
		return cStyle;

	}

	public static HSSFCellStyle setContentStytle(HSSFWorkbook wbook) {
		HSSFPalette customPalette = wbook.getCustomPalette();
		// 设置为黄色背景
		customPalette.setColorAtIndex(HSSFColor.YELLOW.index, (byte) 255, (byte) 255, (byte) 204);

		Font font = wbook.createFont();
		font.setFontHeightInPoints((short) 9);
		font.setBoldweight(Font.BOLDWEIGHT_NORMAL);

		HSSFCellStyle cStyle = wbook.createCellStyle();
		cStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		cStyle.setAlignment(CellStyle.ALIGN_LEFT);
		// 添加边框
		cStyle.setBorderBottom((short) 1);
		cStyle.setBorderLeft((short) 1);
		cStyle.setBorderRight((short) 1);
		cStyle.setBorderTop((short) 1);

		cStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		cStyle.setRightBorderColor(HSSFColor.BLACK.index);
		cStyle.setTopBorderColor(HSSFColor.BLACK.index);
		cStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		cStyle.setFont(font);
		// // Set the default column width;
		// HSSFSheet sheet = wbook.getSheetAt(0);
		// sheet.autoSizeColumn(2);
		// sheet.autoSizeColumn(3);
		// sheet.autoSizeColumn(4);
		// sheet.autoSizeColumn(5);
		// sheet.autoSizeColumn(9);
		return cStyle;

	}

	public static void ExcelReport(ArrayList<Map<String, String>> testResult) {
		ExcelReport(testResult, _ReportXls);

	}

	public static void ExcelReport(ArrayList<Map<String, String>> testResult, String sPath) {
		if (testResult.size() > 0) {

			String sheetname = "TestResult";
			int columnNum = 0;

			try {
				ExcelCreate(sPath, sheetname);

				// 获取文件流,并创建一个EXCEL文件对象
				FileInputStream is = new FileInputStream(sPath);
				HSSFWorkbook wbook = new HSSFWorkbook(is);
				HSSFSheet sheet = wbook.getSheet(sheetname);
				HSSFRow titleRow = sheet.getRow(0);
				columnNum = titleRow.getPhysicalNumberOfCells();
				for (Map<String, String> map : testResult) {
					HSSFRow curRow = sheet.createRow((short) (sheet.getLastRowNum() + 1));
					for (int i = 0; i < columnNum; i++) {
						String colName = titleRow.getCell(i).getStringCellValue().toString();
						// System.out.println("#################" + colName);
						HSSFCell curCol = curRow.createCell(i);
						curCol.setCellStyle(setContentStytle(wbook));
						if (map.containsKey(colName)) {
							// System.out.println("#################" +
							// map.get(colName));
							curCol.setCellValue(map.get(colName));

						}

					}

				}
				FileOutputStream fileOut = new FileOutputStream(sPath);
				wbook.write(fileOut);
				fileOut.flush();
				fileOut.close();

			} catch (Exception e) {

			}
		}

	}

	public static void ExcelReport(Map<String, String> map) {
		ExcelReport(map, _ReportXls);
	}

	public static void ExcelReport(Map<String, String> map, String sPath) {
		if (map.size() > 0) {
			String sheetname = "TestResult";
			int columnNum = 0;
			try {
				ExcelCreate(sPath, sheetname);

				// 获取文件流,并创建一个EXCEL文件对象
				FileInputStream is = new FileInputStream(sPath);
				HSSFWorkbook wbook = new HSSFWorkbook(is);
				HSSFSheet sheet = wbook.getSheet(sheetname);
				HSSFRow titleRow = sheet.getRow(0);
				columnNum = titleRow.getPhysicalNumberOfCells();
				HSSFRow curRow = sheet.createRow((short) (sheet.getLastRowNum() + 1));
				// map.put("输入项", getAllFromMap(map));
				for (int i = 0; i < columnNum; i++) {
					String colName = titleRow.getCell(i).getStringCellValue().toString();
					HSSFCell curCol = curRow.createCell(i);
					curCol.setCellStyle(setContentStytle(wbook));
					if (map.containsKey(colName)) {
						System.out.println("#################" + map.get(colName));
						curCol.setCellValue(map.get(colName));

					}

				}// end for
				FileOutputStream fileOut = new FileOutputStream(sPath);
				wbook.write(fileOut);
				fileOut.flush();
				fileOut.close();

			} catch (Exception e) {

			}
		}
	}

	public static String getAllFromMap(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");

		Set<String> key = map.keySet();
		for (Iterator<String> it = key.iterator(); it.hasNext();) {
			String s = (String) it.next();
			if (s != "") {
				sb.append(s).append("=").append(map.get(s));
				sb.append(",");
				// System.out.println(map.get(s));
			}
		}
		sb.append("]");
		// System.out.println(sb.toString());
		return sb.toString();

	}

	public static Map<String, String> ExcelRecord(Map<String, String> map, Method method) {
		map.put("输入项", getAllFromMap(map));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.put("测试时间", format.format(new Date()));
		map.put("类名", method.getDeclaringClass().getName());
		map.put("方法名", method.getName());
		map.put("测试结果", "PASS");

		return map;

	}
	/**
	 * 写一条测试结果到excel中
	 * 20140714
	 */
	public static void toExcelOneReport(Method method, Map<String, String> data) {
		String cell = null;
		HSSFCell cellContent;
		String filePath = "";
//		String className = method.getDeclaringClass().getName();
		String sheetName = method.getName();
		sheetName = "TestResult";

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(new Date());
		filePath = System.getProperty("user.dir") + "/target/"+"移动端测试IOS-乐城"  + date + ".xls";
		try {

			File exfile = new File(filePath);
			if (!exfile.exists()) {
				ExcelCreate(filePath, sheetName);
			}

			FileInputStream fs = new FileInputStream(filePath);
			POIFSFileSystem ps = new POIFSFileSystem(fs);
			HSSFWorkbook wb = new HSSFWorkbook(ps);
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFSheet sheet1 = wb.getSheet(sheetName);
			HSSFRow titleRow = sheet.getRow(0);
			if (sheet1 == null) {

			}

			HSSFPalette customPalette = wb.getCustomPalette();
			customPalette.setColorAtIndex(HSSFColor.YELLOW.index, (byte) 255,(byte) 255, (byte) 204);

			Font font = wb.createFont();
			font.setFontHeightInPoints((short) 10);

			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
			cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		
			cellStyle.setBorderBottom((short) 1);
			cellStyle.setBorderLeft((short) 1);
			cellStyle.setBorderRight((short) 1);
			cellStyle.setBorderTop((short) 1);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyle.setFont(font);

			Font fontFail = wb.createFont();
			fontFail.setFontHeightInPoints((short) 10);
			fontFail.setBoldweight(Font.BOLDWEIGHT_BOLD);
			fontFail.setColor(HSSFColor.RED.index);

			HSSFCellStyle cellStyleFail = wb.createCellStyle();
			cellStyleFail.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cellStyleFail.setFillForegroundColor(HSSFColor.YELLOW.index);

			cellStyleFail.setBorderBottom((short) 1);
			cellStyleFail.setBorderLeft((short) 1);
			cellStyleFail.setBorderRight((short) 1);
			cellStyleFail.setBorderTop((short) 1);
			cellStyleFail.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleFail.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleFail.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleFail.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleFail.setFont(fontFail);

			FileOutputStream fileOut = new FileOutputStream(filePath);

			HSSFRow curRow = sheet
					.createRow((short) (sheet.getLastRowNum() + 1));
			int columnNum = titleRow.getPhysicalNumberOfCells();
			for (int i = 0; i < columnNum; i++) {
				String colName = titleRow.getCell(i).getStringCellValue()
						.toString();
				cellContent = curRow.createCell(i);
					if (data.containsKey(colName)) {
						cell = data.get(colName);
						if ("FAIL".equals(StrUtils.getString(cell))) {
							cellContent.setCellStyle(cellStyleFail);
							cellContent.setCellValue(cell);
						}else {
							cellContent.setCellStyle(cellStyle);
							cellContent.setCellValue(cell);
						}
					}else{
						cellContent.setCellStyle(cellStyle);	//jd.bai 08292014
					}
			}
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	 public static void main(String[] args) {
//	 ArrayList<Map<String, String>> result = new ArrayList<Map<String,
//	 String>>();
//	 Map<String, String> map = new HashMap<String, String>();
//	
//	 map.put("案例编号", "asdf001");
//	 map.put("测试结果", "Pass");
//	 result.add(map);
//	 Map<String, String> map2 = new HashMap<String, String>();
//	 map2.put("案例编号", "asdf002");
//	
//	 result.add(map2);
//	
//	 ExcelReport(result);
//	 }

}
