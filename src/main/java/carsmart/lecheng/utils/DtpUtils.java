package carsmart.lecheng.utils;

import java.util.Properties;
import java.util.regex.*;

import org.apache.log4j.Logger;
import org.athrun.ios.instrumentdriver.UIAApplication;
import org.athrun.ios.instrumentdriver.UIAImage;
import org.athrun.ios.instrumentdriver.UIATarget;
import org.athrun.ios.instrumentdriver.UIAWindow;
import org.athrun.ios.instrumentdriver.config.Config;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Thread;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DtpUtils{
	private static Logger logger = Logger.getLogger("DtpUtils");

	/**
	 *  密码控件输入密码，目前只支持大小写字母，数字支持，因为数字键盘每次打开顺序会变；
	 * @param target
	 * @param password
	 * @throws Exception
	 */
	public static void setPassword(UIATarget target, String password) throws Exception {
		UIAWindow wininex = target.frontMostApp().windows()[1];
		
		
		for (int i = 0; i < password.length(); i++) {
			String keybutton = password.substring(i, i + 1);
			// 字符是否大写字母 
			Pattern pattern = Pattern.compile("[A-Z]");
			Matcher matcher = pattern.matcher(keybutton);
			if (matcher.find()) {
				wininex.findButton("shift").touchAndHold();
				wininex.findButton(keybutton).tap();
				wininex.findButton("shift").touchAndHold();
				wininex.findButton("shift").touchAndHold();
			}
			else {
				wininex.findButton(keybutton).tap();
			}
		}
	}

	/**
	 * 获取当前用户得短信验证码；
	 * @param username
	 * @return
	 */
	/*public static String getCheckCode(String username) {
		try {
			Class<?> forName = Class.forName("oracle.jdbc.driver.OracleDriver");
			// Connection conn = DriverManager.getConnection(
			// "jdbc:oracle:thin:@22.188.20.34:1521:orau11g", "pds01",
			// "pds01");

			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@22.188.20.33:1521:orau11g", "pds", "pds");

			// String sql = "select msgcod from pdfmsgpf where msgdid = '"
			// + username + "'";
			String sql = "select a.msgcod from pdfmsgpf a ,( select msgdid, max(msgtim) msgtim from pdfmsgpf where msgdid='" + username
					+ "' group by msgdid) t where a.msgdid=t.msgdid and a.msgtim=t.msgtim";
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			if (rs.next()) {
				return rs.getString(1);
			}
			rs.close();
			stm.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void InitCard(String carno) {
		try {
			// Class<?> forName =
			// Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@22.188.20.10:1521:oraz11g", "sip01", "sip01");
			Statement stm = conn.createStatement();
			stm.addBatch("delete  SIACCPF where acccad in ('" + carno + "')");
			stm.addBatch("delete  SICPSPF where cpscad in ('" + carno + "')");
			logger.info("解除绑定卡：［" + carno + "]");
			stm.executeBatch();
			stm.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * @return 返回当前系统时间，格式为：yyyy-mm-dd HH:mm:ss.nnn；
	 */
	public static String getDateTime() {
		return new java.sql.Timestamp(System.currentTimeMillis()).toString();
	}

	/***
	 * 验证弹出提示窗口的信息和预期结果一致，然后点击［确定］按钮；不一致则失败；
	 * @param 旧的提示框失效。请用新的方法。
	 */
	@Deprecated
	public static boolean AssertMessageBox(UIATarget target, String title, String content) throws Exception {
		UIAApplication app = target.frontMostApp();
		boolean isContinue = false;

		if (app.alert().getType().equals("UIAElementNil")) { // 若未找到Alert，则不用验证Message
			isContinue = !isContinue;
		}
		else {
			logger.info("＃＃＃＃#弹出消息窗口： 准备验证提示框信息。。。＃＃＃＃＃");
			target.captureScreenWithName(DtpUtils.getDateTime());
			
			try {
				if(app.alert().scrollViews()[0].staticTexts().length>=1){	//modified by jd.bai
					String actMsgTitle = app.alert().scrollViews()[0].staticTexts()[0].getLabel();
					Assert.assertEquals(actMsgTitle, title);
					logger.info("＃＃＃＃＃验证点通过：消息窗口提示信息：【" + actMsgTitle + "】");			
				}
				if(app.alert().scrollViews()[0].staticTexts().length>1){	//modified by jd.bai
					String actMsgContent = app.alert().scrollViews()[0].staticTexts()[1].getLabel();
					Assert.assertEquals(actMsgContent, content);
					logger.info("＃＃＃＃＃验证点通过：消息窗口提示信息：【" + actMsgContent + "】");
				}
			}
			catch (Exception ex) {
				target.captureScreenWithName("验证失败" + DtpUtils.getDateTime());
				logger.error("＃＃＃＃＃测试案例执行【失败】：MessageBox验证点不通过！\n【" + ex + "】");
				throw ex;
			}
			finally {
				// if (app.alert().findButton("确定") != null) {
				// app.alert().findButton("确定").tap();
				// }
				// else if (app.alert().findButton("OK") != null) {
				// app.alert().findButton("OK").tap();
				// }
				// else {
				// app.alert().defaultButton().tap();
				// }
				app.alert().buttons()[0].tap();
				logger.info("＃＃＃＃＃消息窗口：点击［确定］按钮，关闭Alert提示窗口");			
			}
		}
		return isContinue;

	}
	/***
	 * @author jd.bai20140628 验证弹出提示窗口的信息和预期结果一致，然后点击［确定］按钮；不一致则失败；
	 * @param 提示框文本信息
	 * 说明：就得判断方法失效，重载 提示框方法。
	 * Edit  ss.lv 20140708 弹出框的文本staticTexts序号在不同页面并不一致，增加index作为param定位弹出框信息
	 */
	public static boolean AssertMessageBox(UIATarget target, String title) throws Exception {
		UIAApplication app = target.frontMostApp();
		boolean isContinue = false;
	
		if (app.mainWindow().findElementByText("warm_image.png", UIAImage.class)==null) {
			isContinue = !isContinue;
		}else {
			logger.info("＃＃＃＃#弹出消息窗口： 准备验证提示框信息。。。＃＃＃＃＃");
			try {
				if(app.mainWindow().staticTexts().length>0){
					String actMsgTitle = app.mainWindow().staticTexts()[1].getLabel();
					Assert.assertEquals(actMsgTitle, title);
				}
			}
			catch (Exception ex) {
				target.captureScreenWithName("验证失败" + DtpUtils.getDateTime());
				logger.error("＃＃＃＃＃测试案例执行【失败】：MessageBox验证点不通过！\n【" + ex + "】");
				throw ex;
			}finally {
				logger.info("＃＃＃＃＃消息窗口：点击［确定］按钮，关闭提示窗口");			
				app.mainWindow().findElementByText("确").tap();
			}
		}
		return isContinue;
	}
	/***
	 * @author ss.lv 20140708 不同页面信息框中文本序号可能不同，增加index参数适用不同弹出框定位staticText
	 * @param target
	 * @param title 提示框文本信息
	 * @param index 提示框文本信息的实际序号
	 * @return
	 * @throws Exception
	 */
	public static boolean AssertMessageBox(UIATarget target, String title, int index) throws Exception {
		UIAApplication app = target.frontMostApp();
		boolean isContinue = false;
	
		if (app.mainWindow().findElementByText("warm_image.png", UIAImage.class)==null) {
			isContinue = !isContinue;
		}else {
			logger.info("＃＃＃＃#弹出消息窗口： 准备验证提示框信息。。。＃＃＃＃＃");
			target.captureScreenWithName(DtpUtils.getDateTime());
			try {
				if(app.mainWindow().staticTexts().length>0){
					String actMsgTitle = app.mainWindow().staticTexts()[index].getLabel();
					Assert.assertEquals(actMsgTitle, title);
				}
			}
			catch (Exception ex) {
				target.captureScreenWithName("验证失败" + DtpUtils.getDateTime());
				logger.error("＃＃＃＃＃测试案例执行【失败】：MessageBox验证点不通过！\n【" + ex + "】");
				throw ex;
			}finally {
				logger.info("＃＃＃＃＃消息窗口：点击［确定］按钮，关闭提示窗口");	
				//app.mainWindow().findButton("确  认").tap();
				if(app.mainWindow().findButton("确")!=null)
					app.mainWindow().findButton("确").tap();
				else
					app.mainWindow().findElementByText("确").tap();
			}
		}
		return isContinue;
	}
	

	/***
	 * 验证必须弹出窗口，且提示的正确信息，然后继续脚本；
	 * @param target
	 * @param title
	 * @param content
	 * @throws Exception
	 */
	@Deprecated
	public static boolean VerifyMessageBox(UIATarget target, String title, String content) throws Exception {
		UIAApplication app = target.frontMostApp();
		boolean isContinue = true;

		target.captureScreenWithName(DtpUtils.getDateTime());
		if (app.alert().getType().equals("UIAElementNil")) { // 若未找到Alert，则不用验证Message
			isContinue = !isContinue;
			Assert.fail("未找到Alert,此处必须有提示框显示提示信息");
		}
		else {
			logger.info("＃＃＃＃#弹出Alert窗口： 验证提示框信息＃＃＃＃＃");
			try {
//				Assert.assertEquals(actMsgContent, content);
//				logger.info("＃＃＃＃＃验证点通过：找到Alert窗口提示信息：［" + actMsgContent + "]");
//				Assert.assertEquals(actMsgTitle, title);
//				logger.info("＃＃＃＃＃验证点通过：找到Alert窗口提示信息：［" + actMsgTitle + "]");

				if(app.alert().scrollViews()[0].staticTexts().length>=1){	//modified by jd.bai  20140506
					String actMsgTitle = app.alert().scrollViews()[0].staticTexts()[0].getLabel();
					Assert.assertEquals(actMsgTitle, title);
					logger.info("＃＃＃＃＃验证点通过：消息窗口提示信息：【" + actMsgTitle + "】");			
				}
				if(app.alert().scrollViews()[0].staticTexts().length>1){	//modified by jd.bai
					String actMsgContent = app.alert().scrollViews()[0].staticTexts()[1].getLabel();
					Assert.assertEquals(actMsgContent, content);
					logger.info("＃＃＃＃＃验证点通过：消息窗口提示信息：【" + actMsgContent + "】");
				}				
			}
			catch (Exception ex) {
				logger.error("＃＃＃＃＃测试案例执行［失败］：MessageBox验证点不通过！\n[" + ex + "]");
				throw ex;
			}
			finally {
				
				app.alert().cancelButton().tap();
				logger.info("＃＃＃＃＃消息窗口：点击［确定］按钮，关闭Alert提示窗口");
				target.delay(3);
			}
		}
		return isContinue;
	}

	public static void WaitForDisappear(UIAWindow win, String text) throws Exception {
		boolean isRun = true;
		int timeout = Integer.parseInt(Config.get("timeout"));
		int i = 0;
		logger.debug("＃＃＃＃＃WaitForUIAElementDisappear＃＃＃＃＃");

		while (isRun && i < timeout) {
			if (win.findElementByText(text) == null) {
				logger.debug("＋＋＋＋＋＋没有找到该对象");
				isRun = false;
			}else {
				Thread.sleep(1000);
				i = i + 1;
				if(i==timeout){			//TODO jd.bai	20140701  防止加载超时,继续加载 如果后续需求中限制加载次数后终止，可以略加修改。
					i=0;System.err.println("\n when Runtime out, reloading again...");
				}
			}

		}
	}

	public static void WaitForShow(UIAWindow win, String text) throws Exception {
		boolean isRun = true;
		int timeout = Integer.parseInt(Config.get("timeout"));
		int i = 0;
		logger.debug("＃＃＃＃＃Wait For UIAElement Display ＃＃＃＃＃");

		while (isRun && i < timeout) {
			if (win.findElementByText(text) != null) {
				logger.debug("＋＋＋＋＋＋找到该对象");
				isRun = false;
			}
			else {
				Thread.sleep(5000);
				logger.info("＋＋＋＋＋＋对象加载中，延迟5秒继续等待");
				i = i + 5;
			}

		}
		// if (i >= timeout) {
		// Assert.fail("［WaitForShow］未找到该元素，超时异常：Timeout!");
		// }

	}
	
	public static void WaitForLoading(UIAWindow win) throws Exception {
		DtpUtils.WaitForDisappear(win,"请稍候，加载中...");
	}
	/**
	 * modified by jd.bai
	 */
	private static Properties prop;
	public static String getParameter(String key) {
		if (prop == null) {
			prop = new Properties();
			try {
				String file = new DtpUtils().getClass().getResource("/btn.properties").toURI().getPath();
				logger.trace("Path: "+file);
				prop.load(new FileInputStream(file));
			} catch (Exception e) {
				throw new Error("Not find btn.properties file，please add it to build path source。");
			}
		}
		try{
			String value = new String(prop.getProperty(key));		//.getBytes("ISO-8859-1"),"UTF-8");
			if (value == null||value.trim()=="")
				return null;
			return value.trim();
		}catch(Exception e){
			throw new Error("Not find label,please check out it again");
		}
	}
}
