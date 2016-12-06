package org.athrun.ios.instrumentdriver.config;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
/**
 * @author bingxin
 * @author taichan
 */
public class Config {
	private static Logger logger = Logger.getLogger("Config");
	
	private static Properties prop;
	public static String get(String key) {
		if (prop == null) {
			prop = new Properties();
			try {
				String file = new Config().getClass()
						.getResource("/athrun.properties").toURI().getPath();
				logger.trace("Config Path: "+file);
				System.out.println("Config Path: "+file);
				
				prop.load(new FileInputStream(file));
			} catch (Exception e) {
				throw new Error("未找到athrun.properties文件，请将config目录添加到build path的source中。");
			}
		}
		String value = prop.getProperty(key);
		if (value == null||value.trim()=="")
			return null;
		return value.trim();
	}
}
