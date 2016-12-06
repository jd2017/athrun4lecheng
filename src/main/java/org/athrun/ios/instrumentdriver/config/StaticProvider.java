package org.athrun.ios.instrumentdriver.config;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import jxl.read.biff.BiffException;
import org.testng.annotations.DataProvider;
public class StaticProvider{				
	@DataProvider(name = "dp")
	public static Iterator<Object[]> getDataByTestMethodName(Method method) throws BiffException, IOException {
		return new ExcelDataProvider(method.getDeclaringClass().getName(),method.getName());
	}
	

}
