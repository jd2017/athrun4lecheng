package test.lecheng.login;
import java.lang.reflect.Method;
import java.util.Map;
import org.apache.log4j.Logger;
import org.athrun.ios.instrumentdriver.config.StaticProvider;
import org.athrun.ios.instrumentdriver.config.Write2Excel;
import org.athrun.ios.instrumentdriver.test.InstrumentDriverTestCase;
import org.testng.annotations.Test;

import carsmart.lecheng.login.Login;


/**
 * @author jd.bai
 *	
 */
public class TheLoginTest extends InstrumentDriverTestCase {
	private static Logger logger=Logger.getLogger("TheLoginTest");
	
	@Test(groups = { "login" }, dataProvider = "dp", dataProviderClass = StaticProvider.class)
	public void toLoginTest(Map<String, String> data) throws Exception {
			Method method=getClass().getMethod("toLoginTest",Map.class);
		try {
			data = Write2Excel.ExcelRecord(data, method);
			logger.info("\n============Start Run：" + data.get("案例编号") + "===================");	
			Login.toLogin(target, data.get("用户名"),data.get("密码"));
//			Login.toLogin(target, data.get("账户"));
		}catch (AssertionError ae) {
			logger.error("［验证点］不通过，案例执行失败！");
			data.put("测试结果", "FAIL");
			data.put("实际输出", "［验证点］不通过，案例执行失败！"+ae.toString());
			throw ae;
		} catch (Exception ex) {
			logger.error("［测试脚本]出现异常，案例执行失败！");
			data.put("测试结果", "FAIL");
			data.put("实际输出", "［测试脚本]出现异常，案例执行失败！"+ex.toString());
			throw ex;
		}finally{
			Write2Excel.toExcelOneReport(method, data);
//			Logout.toLogout(target);
		}
	}
	
}
