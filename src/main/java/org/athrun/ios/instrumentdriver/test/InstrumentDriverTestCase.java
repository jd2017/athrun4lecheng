package org.athrun.ios.instrumentdriver.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;
import org.athrun.ios.instrumentdriver.MySocket;
import org.athrun.ios.instrumentdriver.RunType;
import org.athrun.ios.instrumentdriver.UIAApplication;
import org.athrun.ios.instrumentdriver.UIATarget;
import org.athrun.ios.instrumentdriver.UIAWindow;
import org.athrun.ios.instrumentdriver.config.DriverUtil;
import org.athrun.ios.instrumentdriver.config.ResourceManager;
import org.athrun.ios.instrumentdriver.config.Write2Excel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import carsmart.lecheng.utils.DtpUtils;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.BeforeClass;
/**
 * @author ziyu.hch
 * @author taichan
 */
public class InstrumentDriverTestCase extends ThreadGroup {
	private static Logger logger = Logger.getLogger("InstrumentDriverTestCase");
	public InstrumentDriverTestCase() {
		super("InstrumentDriverTestCase");
	}

	private static final String instrumentTraceFileFolder = "./instrumentsDriver.trace";
	public UIATarget target;
	public UIAApplication app;
	public UIAWindow win;
	public ArrayList<Map<String,String>> testResult = new ArrayList<Map<String,String>>();
	
	private String appPath = DriverUtil.getApp();
	private Boolean isDebug = DriverUtil.isDebug();
	private Boolean isSimulator = DriverUtil.isSimulator();
	private String udid = DriverUtil.getUDID();
	private String[] cmd = {};
	Process proc = null;
	@BeforeSuite
	public static void prepareResource() throws Exception {
		logger.info("\n==================InstrumentDriverTestCase  Start TestSuite: ======================");
		ResourceManager.updateResource();
		
		logger.trace("Run: /runTests.sh");
		String[] cmdrun = { "chmod", "777", String.format("%s", ResourceManager.getRunShell()) };
		Runtime.getRuntime().exec(cmdrun);
		logger.trace("Run: /TcpSocket.sh");
		String[] cmdtcp = { "chmod", "777", String.format("%s", ResourceManager.getTcpShell()) };
		Runtime.getRuntime().exec(cmdtcp);

		DriverUtil.delFolder(instrumentTraceFileFolder);
		MySocket.startSocket();
	}

	@AfterSuite
	public void aftersuite() throws Exception {
		MySocket.tearDownSocket();
		logger.info("- End TestSuite: -");
	}
	public void WriteReport(){
//		Write2Excel.ExcelReport(testResult);
//		testResult.clear();
		System.out.println("打印测试报表＋＋＋＋＋＋＋＋＋");
	}

	// @BeforeClass
	// public static void prepareResource() throws Exception {
	// ResourceManager.updateResource();
	// logger.trace("Run: /runTests.sh");
	//
	// String[] cmdrun = { "chmod", "777", String.format("%s",
	// ResourceManager.getRunShell()) };
	// Runtime.getRuntime().exec(cmdrun);
	// logger.trace("Run: /TcpSocket.sh");
	// String[] cmdtcp = { "chmod", "777", String.format("%s",
	// ResourceManager.getTcpShell()) };
	// Runtime.getRuntime().exec(cmdtcp);
	// logger.info("\n=============== Start TestCase: ======================");
	// }
	
	@BeforeMethod
	public void setUp() throws Exception {
		try {
			
			killInstruments();
			 DriverUtil.delFolder(instrumentTraceFileFolder); //取消注释 2014.04.25
//			 MySocket.startSocket();
			
			this.target = UIATarget.localTarget();
			this.app = target.frontMostApp();
			this.win = app.mainWindow();
			

			RunType.DEBUG = this.isDebug;
			String shellCmd = String.format("%s %s %s %s %s", ResourceManager.getRunShell(), appPath, ResourceManager.getInstrumentRoot(),
				isSimulator, udid);

			this.cmd = new String[] { "/bin/sh", "-c", shellCmd };

			logger.trace("shellCmd:\t " + shellCmd);
			proc = Runtime.getRuntime().exec(cmd);
			
			//instruments -t /Applications/Xcode.app/Contents/Developer/../Applications/Instruments.app/Contents/PlugIns/AutomationInstrument.bundle/Contents/Resources/Automation.tracetemplate -D instrumentsDriver /Users/iosTest/Desktop/APP/CarsmartiOSCarShop.app -e UIASCRIPT /Users/iosTest/InstrumentDriver/CSRunner.js -e UIARESULTSPATH /Users/iosTest/InstrumentDriver/log/ -v
			new Thread(new ReadInstrumentsOutPut(), "instruments").start();
			
		}
		catch (Exception ex) {
			logger.error(ex);
		}
	}
	@AfterMethod
	public void tearDown() throws Exception {
		
		WriteReport();
//		target.delay(2);			//jd.bai	80292014 delete
		
		MySocket.tearDownSocket();		
//		MySocket.sendExit();		//jd.bai	80292014  delete

		proc.waitFor();
		proc.destroy();
		killInstruments();

//		 String[] cmd = { "/bin/sh", "-c", "rm -rf *.trace " };
//		 Process pro = Runtime.getRuntime().exec(cmd);
//		 pro.waitFor();
//		 pro.destroy();

	}
	class ReadInstrumentsOutPut implements Runnable {
		
		@Override
		public void run() {
			try {
				InputStream input = proc.getInputStream();
				InputStreamReader inputReader;
				inputReader = new InputStreamReader(input, "UTF-8");
				BufferedReader bufferR = new BufferedReader(inputReader);
				String str = null;
				
				while((str = bufferR.readLine()) != null) {
					System.err.println("some commands from BufferReader,send to Mysocket\n" + str.replace(" +0000", ""));
				}
			}
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * 开始执行测试用例和测试用例执行完清理instruments进程
	 * <p>
	 * 测试用例批量执行时发现有instruments睡死现象
	 * 
	 * @throws Exception
	 */
	private void killInstruments() throws Exception {
		Runtime.getRuntime().exec("killall -9 instruments");
		Runtime.getRuntime().exec("killall -9 'instruments'");
		Runtime.getRuntime().exec("killall -9 'Instruments'");
		Runtime.getRuntime().exec("killall -9 Instruments");
	}
	
}
