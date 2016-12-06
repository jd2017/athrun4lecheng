package carsmart.lecheng.login;

import java.io.FileInputStream;
import java.io.InputStream;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;
import org.athrun.ios.instrumentdriver.UIAApplication;
import org.athrun.ios.instrumentdriver.UIATarget;
import org.athrun.ios.instrumentdriver.UIAWindow;
import org.athrun.ios.instrumentdriver.test.InstrumentDriverTestCase;

import carsmart.lecheng.utils.DtpUtils;

public class Login extends InstrumentDriverTestCase {
	private static Logger logger = Logger.getLogger("Login");
	private static String accountFilePath = "./src/test/resources/account.xls";

	/**
	 * @param target
	 * @param user
	 * @param password
	 * @return
	 * @throws Exception
	 * jd.bai 已登录，先退出登录后进行登录。
	 */
	public static UIATarget toLogin(UIATarget target, String user, String password) throws Exception {
		
		UIAApplication app = target.frontMostApp();
		UIAWindow win = app.mainWindow();
		target.delay(1);
			//登陆前，退出操作
			if(win.findButton("登")==null){
				win.buttons()[3].tap();
//				win.elements()[2].tap();
				win.tableViews()[0].findElementByText("乐乘设置").tap();
				win.tableViews()[0].findButton("退出账号").tap();
				win.images()[0].findButton("确认").tap();
				DtpUtils.WaitForDisappear(win, "退出登录中...");
			}
			win.textFields()[0].tap();
			win.textFields()[0].setValue(user);
			win.secureTextFields()[0].tap();
			win.secureTextFields()[0].setValue(password);
//			DtpUtils.setPassword(target, password);
			win.findButton("登").tap();
			
			/**
			 * 判断不同登陆环境出现的的登陆错误提示信息的处理
			 */
			if (app.mainWindow().staticTexts().length<=1) {
				return target;
			}else {
				String value1=app.mainWindow().staticTexts()[1].getVal();			//针对提示快速消失信息的棘手处理操作步骤
				String value=app.mainWindow().staticTexts()[0].getVal();
				System.err.println(value+"\n==="+value1);
			if(value.equals("车辆安防")||value1.equals("请稍候，加载中...")||value1.equals("车辆安防")){			//针对登陆加载的处理
					DtpUtils.WaitForDisappear(win, "请稍候，加载中...");
					value1=app.mainWindow().staticTexts()[1].getVal();
					System.err.println(value+"\n-=-="+value1);
					if(value1.equals("登录失败，请稍候重试！")){throw new RuntimeException(value1);}
					return target;
			}else
					throw new RuntimeException(value1);		
			}


	}
	//读取账户管理 的信息
	public static UIATarget toLogin(UIATarget target, String account) throws Exception {

		//读取excel文件：account.xls
		InputStream in = new FileInputStream(accountFilePath);
		Workbook wb = Workbook.getWorkbook(in);
		Sheet st = (Sheet) wb.getSheet(0);
		//遍历excel文件第一列查找账号
		int  rows = st.getRows();
		int i,row = 0;
		for(i=1; i<rows; i++){
			if(account.equals(st.getCell(0, i).getContents())){
				row = i;
				break;
			}
		}
		if(i>=rows){
			throw new RuntimeException("Didn't find the account!");
		}else{
			//读取第二列和第三列数据登录
			String username = st.getCell(1,row).getContents();
			String password = st.getCell(2,row).getContents();
			return toLogin(target,username,password);
		}
	}

	/***
	 * 要求用户每次必须登录进入，如果用户记住密码已经登录，则先退出；
	 * @param target
	 * @param user
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static UIATarget toReLogin(UIATarget target, String user, String password) throws Exception {
		
		UIAApplication app = target.frontMostApp();
		UIAWindow win = app.mainWindow();

		win.findButton("我的").tap();
		if(win.findButton("登 录") == null){
			win.scrollViews()[0].tableViews()[0].findButton("安全退出").tap();
			win.buttons()[4].tap();
			DtpUtils.WaitForDisappear(win, "加载中...");
			Login.toLogin(target, user, password);
		}
		return target;
	}
}
