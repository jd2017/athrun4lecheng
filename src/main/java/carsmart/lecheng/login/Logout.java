package carsmart.lecheng.login;
import org.apache.log4j.Logger;
import org.athrun.ios.instrumentdriver.UIAApplication;
import org.athrun.ios.instrumentdriver.UIAImage;
import org.athrun.ios.instrumentdriver.UIATarget;
import org.athrun.ios.instrumentdriver.UIAWindow;
import org.athrun.ios.instrumentdriver.test.InstrumentDriverTestCase;

import carsmart.lecheng.utils.DtpUtils;
/***
 * @author
 */
public class Logout extends InstrumentDriverTestCase{
	private static Logger logger = Logger.getLogger("Logout");
	public static void toLogout(UIATarget target) throws Exception {
		UIAApplication app = target.frontMostApp();
		UIAWindow win = app.mainWindow();
		
		win.buttons()[3].tap();
		win.tableViews()[0].findElementByText("乐乘设置").tap();
		win.tableViews()[0].findButton("退出账号").tap();
		win.images()[0].findButton("确认").tap();
	}
	//add method by jd.bai20140628
	public  static void toLogout2(UIATarget target) throws Exception {
		UIAApplication app = target.frontMostApp();
		UIAWindow win = app.mainWindow();
		int times=0;
		boolean bo=true;
		
		if (app.mainWindow().findElementByText("warm_image.png", UIAImage.class)!=null){
			app.mainWindow().findElementByText("确").tap();
			if(app.mainWindow().findButton("确")!=null)
				app.mainWindow().findButton("确").tap();
		}else 
			logger.info("when go out,not find alert message, go on next step");
		
		ok:while(bo){						
			if(win.findButton("details Back Button Begin 1")!=null)
				win.findButton("details Back Button Begin 1").tap();
			else if(win.findButton("Collect Back Normal Btn")!=null)
				win.findButton("Collect Back Normal Btn").tap();
			else{
				bo=false; System.err.println("not find goback btn，go on next step to logout username");
				break ok;
			}
			times++;
			if(times>=7){ 
				logger.info("\n\n请检查运行脚本正确性，程序返回次数为："+times);
				if(DtpUtils.AssertMessageBox(target, "退出程序错误，检查脚本或者环境是否畅通。。。")){}
				throw new RuntimeException("脚本返回异常，请检查脚本");
			}
		}
		if(win.findButton("left Nav Item Begin")!=null){
			win.findButton("left Nav Item Begin").tap();
			win.findButton("but1").tap();
			DtpUtils.WaitForDisappear(win, "加载中...");
		}else if(win.findButton("我的")!=null){
		}else{
			logger.info("\n\n没有找到退出 入口，检查脚本错误或者重新执行。。。");
			throw new RuntimeException("没有找到退出 入口，检查脚本错误或者重新执行。。。");
		}
		win.findButton("我的").tap();			//进入登陆页面
		DtpUtils.WaitForLoading(win);
		if(win.findButton("登 录")!=null){
			win.findButton("details Back Button Begin 1").tap();
		}else{
			//win.scrollViews()[0].tableViews()[0].findButton("安全退出").tap();
			win.findButton("安全退出").tap();
			win.buttons()[9].tap(); //确认
		}
		DtpUtils.WaitForDisappear(win, "加载中...");
	}
}
