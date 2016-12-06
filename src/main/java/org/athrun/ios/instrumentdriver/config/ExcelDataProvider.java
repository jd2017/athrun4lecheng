package org.athrun.ios.instrumentdriver.config;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import jxl.*;

public class ExcelDataProvider implements Iterator<Object[]>{
	private Workbook book = null;
	private Sheet sheet = null;
	private int rowNum = 0;
	private int curRowNo = 0;
	private int columnNum = 0;
	private String[] columnnName;
	public ExcelDataProvider(String classname, String methodname) {
		try {
			String wbookFile = System.getProperty("user.dir")+"/src/test/resources/" + classname.replaceAll("\\.", "/") + ".xls";
			wbookFile = wbookFile.replace("\\", "/");
			System.out.println("Excel Path:" + wbookFile);
			this.book = Workbook.getWorkbook(new File(wbookFile));
			this.sheet = book.getSheet(methodname);
			this.rowNum = sheet.getRows();
			
			Cell[] c = sheet.getRow(0);
			this.columnNum = c.length;
			columnnName = new String[c.length];
			for (int i = 0; i < c.length; i++) {
//				 System.out.println( c[i].getContents().toString());  ////输出列名
				columnnName[i] = c[i].getContents().toString();
			}
			this.curRowNo++;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public boolean hasNext() {
		if (this.rowNum == 0 || this.curRowNo >= this.rowNum) {
			try {
				book.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		} else
			return true;
	}

	@Override
	public Object[] next() {
		Cell[] c = sheet.getRow(this.curRowNo);
		Map<String, String> s = new HashMap<String, String>();
		for (int i = 0; i < this.columnNum; i++) {
			String temp = "";
			try {
				temp = c[i].getContents().toString();
			} catch (ArrayIndexOutOfBoundsException ex) {
				temp = "";
			}
			s.put(this.columnnName[i], temp);
		}

		Object r[] = new Object[1];
		r[0] = s;
		this.curRowNo++;
		return r;
	} 

	@Override
	public void remove() {
		throw new UnsupportedOperationException("remove unsupported.");
	}
}
