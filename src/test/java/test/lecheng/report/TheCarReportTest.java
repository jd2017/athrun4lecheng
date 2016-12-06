package test.lecheng.report;

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
 * @author jd.bai
 * 首页|行程报告
 */
public class TheCarReportTest extends InstrumentDriverTestCase {
	private static Logger logger=Logger.getLogger("TheCarReportTest");
	@Test(groups = { "report" }, dataProvider = "dp", dataProviderClass = StaticProvider.class)
	public void toCarReportTest(Map<String, String> data) throws Exception {
		Method method=getClass().getMethod("toCarReportTest",Map.class);
		try{
			data = Write2Excel.ExcelRecord(data, method);
			String num=data.get("案例编号").substring(11, 14);
			Login.toLogin(target, data.get("账户"));
			if(win.findElementByText("尚无行程信息")!=null) throw new RuntimeException("账户不满足 条件");		//无行程轨迹列表
			if(num.equals("001")||num.equals("004")||num.equals("008")||num.equals("009")||num.equals("010")||num.equals("014")||
					num.equals("015")||num.equals("017")||num.equals("019")||num.equals("022")||num.equals("023")||num.equals("025")){
				win.scrollViews()[0].images()[1].tapWithOptions("{tapOffset:{x:0.50, y:0.85}}");			//首页->行程报告
				if(num.equals("001")||num.equals("008")){
					Assert.assertNotNull(win.findStaticText("行程报告"), "判断是否 进入行程报告页面");
				}else if(num.equals("004")||num.equals("009")){
					win.findButton("icon back normal").tap();
				}else if(num.equals("010")){
					win.findButton("历史行程").tap();
					Assert.assertNotNull(win.findStaticText("个人空间"),"判断是否进入 个人空间页面" );
				}else if(num.equals("014")||num.equals("015")){
//					win.tableViews()[0].cells()[1].scrollToVisible();					//向上滑动	
					target.captureScreenWithName(DtpUtils.getDateTime());
				}else if(num.equals("017")||num.equals("019")||num.equals("022")||num.equals("023")){
					win.tableViews()[0].tapWithOptions("{tapOffset:{x:0.59, y:0.52}}");					//进入 轨迹详情页面
					if(num.equals("017")){
						Assert.assertNotNull(win.findStaticText("轨迹详情"), "判断是否 进入轨迹详情页面");
					}else if(num.equals("019")){
						target.captureScreenWithName(DtpUtils.getDateTime());
					}else if(num.equals("022")){
						win.findButton("track switch play nomral").tap();
						target.captureScreenWithName(DtpUtils.getDateTime());
						win.findButton("track btn play").tap();
						Thread.sleep(1000);
						target.captureScreenWithName(DtpUtils.getDateTime());
					}else if(num.equals("023")){
						win.findButton("track switch play nomral").tap();
						target.captureScreenWithName(DtpUtils.getDateTime());
						win.sliders()[0].dragToValue(0.13);
						target.captureScreenWithName(DtpUtils.getDateTime());
						win.sliders()[0].dragToValue(0.34);
						target.captureScreenWithName(DtpUtils.getDateTime());
						win.sliders()[0].dragToValue(1.00);
						target.captureScreenWithName(DtpUtils.getDateTime());
					}else{
						throw new RuntimeException("未找到对应的案例编号");
					}
				}else if(num.equals("025")){
					target.captureScreenWithName(DtpUtils.getDateTime());
					win.tableViews()[0].cells()[1].scrollToVisible();
					target.captureScreenWithName(DtpUtils.getDateTime());
				}else{
					throw new RuntimeException("未找到案例编号，查找后重新尝试");
				}
			}else if(num.equals("003")){
				win.buttons()[3].tap();
				win.tableViews()[0].cells()[1].tap();
				if(win.tableViews()[0].cells().length<=0) 
					throw new RuntimeException("无行程列表");
				win.tableViews()[0].cells()[0].scrollToVisible();
				win.tableViews()[0].cells()[0].tap();
				Assert.assertNotNull(win.findStaticText("行程报告"), "判断是否 进入行程报告页面");
			}else{
				throw new RuntimeException("未找到对应的案例编号");
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
