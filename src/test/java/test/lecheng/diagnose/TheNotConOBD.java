package test.lecheng.diagnose;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.log4j.Logger;
import org.athrun.ios.instrumentdriver.config.StaticProvider;
import org.athrun.ios.instrumentdriver.config.Write2Excel;
import org.athrun.ios.instrumentdriver.test.InstrumentDriverTestCase;
import org.testng.Assert;
import org.testng.annotations.Test;

import carsmart.lecheng.login.Login;
import carsmart.lecheng.utils.DtpUtils;
/**
 * 
 * @author jd.bai
 *02/09/2014
 *诊断页面的相关测试
 */
public class TheNotConOBD extends InstrumentDriverTestCase {
	private static Logger logger=Logger.getLogger("TheNotConOBD");

	@Test(groups = { "diagnose" }, dataProvider = "dp", dataProviderClass = StaticProvider.class)
	public void toNotConOBDTest(Map<String, String> data) throws Exception {

		Method method=getClass().getMethod("toNotConOBDTest",Map.class);
	try{
		data = Write2Excel.ExcelRecord(data, method);
		String num=data.get("案例编号").substring(11, 14);
//		Login.toLogin(target, data.get("用户名"),data.get("密码"));
		Login.toLogin(target, data.get("账户"));
		win.scrollViews()[0].buttons()[4].tap();
		if(num.equals("001")){
			Assert.assertEquals(win.tableViews()[0].getName(), "未检测, 安装乐乘盒子，来个深度检测吧！, 最新检测：无");
		}else if(num.equals("005")){
			Assert.assertNotNull(win.findStaticText("乐乘盒子使用简介"), "乐乘盒子使用简介,文本非空");
		}else if(num.equals("012")){
			win.tableViews()[0].findButton("深度检测").tap();
			app.alert().cancelButton().tap();
			win.findButton("icon back normal").tap();
		}else if(num.equals("032")){
			win.tableViews()[0].findButton("深度检测").tap();
			app.alert().defaultButton().tap();
			Assert.assertNotNull(app.alert().scrollViews()[0].findStaticText("乐乘盒子网络连接不正常，无法进行深度检测！"),"发现的文本信息非空");
			app.alert().cancelButton().tap();
		}else{
			throw new RuntimeException("没找到符合规定的案例编号，请查证后重新运行。。。");
		}
		
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
		}
	}
}
