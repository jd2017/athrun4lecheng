package test.lecheng.security;

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
 *首页|安防页面
 */
public class TheSecurityTest extends InstrumentDriverTestCase {
	private static Logger logger=Logger.getLogger("TheSecurityTest");
	@Test(groups = { "security" }, dataProvider = "dp", dataProviderClass = StaticProvider.class)
	public void toSwitchTest(Map<String, String> data) throws Exception {
		Method method=getClass().getMethod("toSwitchTest",Map.class);
		try{
			data = Write2Excel.ExcelRecord(data, method);
			String num=data.get("案例编号").substring(11, 14);
			if(num.equals("006")){
				if(win.findButton("登")==null){
					win.buttons()[3].tap();
					win.tableViews()[0].findElementByText("乐乘设置").tap();
					win.tableViews()[0].findButton("退出账号").tap();
					win.images()[0].findButton("确认").tap();
				}
				win.findButton("首页").tap();
				win.scrollViews()[0].buttons()[0].tap();
				Assert.assertNotNull(win.findButton("忘记密码"), "检测跳到 到首页登陆页面");
				return ;
			}
			Login.toLogin(target, data.get("账户"));
			if(num.equals("001")){
				Assert.assertEquals(win.scrollViews()[0].staticTexts()[1].getName(),"OFF");
			}else if(num.equals("003")|num.equals("004")){
				win.buttons()[3].tap();
				win.tableViews()[0].findElementByText("乐乘设置").tap();
				win.tableViews()[0].findElementByText("安防设置").tap();
				String value=win.scrollViews()[0].staticTexts()[2].getName();
				if(value.equals("ON")){
					win.scrollViews()[0].staticTexts()[2].tap();		//关闭安防开关
					Assert.assertNotNull(app.mainWindow().findStaticText("安防开关设置成功"), "安防开关设置成功");
				}else{
					win.scrollViews()[0].staticTexts()[2].tap();		//开启安防开关
					Assert.assertNotNull(app.mainWindow().findStaticText("安防开关设置成功"), "安防开关设置成功");
				}
			}else if(num.equals("005")){
				win.scrollViews()[0].buttons()[0].tap();
				Assert.assertNotNull(win.findStaticText("乐乘盒子使用简介"), "进入 ‘乐乘盒子使用简介’");
			}else if(num.equals("007")||num.equals("008")){
				if(win.scrollViews()[0].staticTexts()[1].getName().equals("ON"))
					win.scrollViews()[0].staticTexts()[1].tap();
				win.scrollViews()[0].buttons()[0].tap();
				Assert.assertNotNull(win.findStaticText("车辆安防"));
			}else if(num.equals("010")||num.equals("012")||num.equals("013")||num.equals("015")||num.equals("016")||num.equals("017")||
					num.equals("018")||num.equals("019")||num.equals("020")||num.equals("021")||num.equals("024")){
				win.scrollViews()[0].buttons()[0].tap();
				if(num.equals("010")){
					Assert.assertNotNull(win.findStaticText("车辆安防"),"判断进入车辆安防 页面");
				}else if(num.equals("012")){
					win.buttons()[4].tap();	//返回 
				}else if(num.equals("013")||num.equals("015")||num.equals("016")||num.equals("017")||num.equals("018")||num.equals("019")||num.equals("020")||num.equals("021")||num.equals("024")){
					win.findButton("安防设置").tap();
					if(!win.scrollViews()[0].staticTexts()[2].getName().equals("ON"))
						win.scrollViews()[0].staticTexts()[2].tap(); //开启安防开关
					if(num.equals("013")||num.equals("015")){
						if(num.equals("015"))
							Assert.assertNotNull(win.scrollViews()[0].findButton("07:00-21:00"), "查看默认时段 07:00-21:00");	//015
						else
							Assert.assertNotNull(win.scrollViews()[0].findStaticText("安防开关"), "监测进入 安防设置 页面");
					}else if(num.equals("016")||num.equals("017")){
						if(num.equals("016"))
							Assert.assertEquals(win.scrollViews()[0].staticTexts()[5].getVal(),"OFF");	//016 判断默认时勿扰开关 为关闭状态
						else
							Assert.assertNotNull(win.scrollViews()[0].findStaticText("勿扰时段内将不推送告警消息，请谨慎设置。"),"判断页面的文本信息");//017
					}else if(num.equals("018")){
						if(!win.scrollViews()[0].staticTexts()[5].getVal().equals("OFF"))
							win.scrollViews()[0].staticTexts()[5].tap();		//关闭勿扰设置。
						win.scrollViews()[0].staticTexts()[5].tap();		//开启勿扰设置。
						Assert.assertNotNull(win.findStaticText("勿扰功能开启"), "未获取开启勿扰开关文本");
					}else if(num.equals("019")){
						if(win.scrollViews()[0].staticTexts()[5].getVal().equals("OFF"))
							win.scrollViews()[0].staticTexts()[5].tap();	//开启勿扰设置。
						win.scrollViews()[0].staticTexts()[5].tap();	//关闭勿扰设置。
						Assert.assertNotNull(win.findStaticText("勿扰功能关闭"), "未获取关闭勿扰开关文本");
					}else if(num.equals("020")||num.equals("021")){
						if(win.scrollViews()[0].staticTexts()[5].getVal().equals("OFF"))
							win.scrollViews()[0].staticTexts()[5].tap();   //打开勿扰开关
						if(num.equals("020")){
							win.scrollViews()[0].findButton("07:00-21:00").tap();
							win.images()[0].pickers()[0].wheels()[0].tapWithOptions("{tapOffset:{x:0.39, y:0.66}}");//"07. 8 of 24"
							win.images()[0].pickers()[0].wheels()[2].tapWithOptions("{tapOffset:{x:0.61, y:0.35}}");//"21. 22 of 24"
							win.images()[0].findButton("确认").tap();
						}else{
							win.scrollViews()[0].findButton("08:00-20:00").tap();
							win.images()[0].pickers()[0].wheels()[0].tapWithOptions("{tapOffset:{x:0.42, y:0.35}}");//"08. 9 of 24"
							win.images()[0].pickers()[0].wheels()[2].tapWithOptions("{tapOffset:{x:0.50, y:0.66}}");//"20. 21 of 24"
							win.images()[0].findButton("确认").tap();
							win.scrollViews()[0].staticTexts()[5].tap(); 
							Assert.assertEquals(win.scrollViews()[0].staticTexts()[5].getVal(),"OFF");//判断关闭勿扰开关成功
						}
					}else if(num.equals("024")){
						win.scrollViews()[0].sliders()[0].dragToValue(1.00);
						Assert.assertEquals(win.scrollViews()[0].sliders()[0].getVal(),"100%");
						win.scrollViews()[0].sliders()[0].dragToValue(0.00);
						Assert.assertEquals(win.scrollViews()[0].sliders()[0].getVal(),"0%");
						win.scrollViews()[0].sliders()[0].dragToValue(0.50);
						Assert.assertEquals(win.scrollViews()[0].sliders()[0].getVal(),"50%");
					}
				}
			}else if(num.equals("014")){
				win.buttons()[3].tap();
				win.findElementByText("乐乘设置").tap();
				win.findElementByText("安防设置").tap();
				Assert.assertNotNull(win.scrollViews()[0].findStaticText("安防开关"), "监测进入 安防设置 页面");
			}else if(num.equals("029")){
				win.scrollViews()[0].buttons()[0].tap();
				if(!win.scrollViews()[0].staticTexts()[2].getVal().equals("ON"))
					win.scrollViews()[0].staticTexts()[2].tap();				//初始状态 开启安防	
				 for(int i=0;i<10;i++){
					 win.scrollViews()[0].staticTexts()[2].tap();
					 Assert.assertNotNull(win.findStaticText("安防已关闭"), "判断 安防开关已关闭信息");
					 win.scrollViews()[0].staticTexts()[2].tap();
					 Assert.assertNotNull(win.findStaticText("安防已开启"), "判断 安防开关已关闭信息");
				 }
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
