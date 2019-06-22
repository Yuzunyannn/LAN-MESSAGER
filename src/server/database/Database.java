package server.database;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

	/** 数据库地址 */
	static public final String CONNECT_URL = "jdbc:mysql://localhost:3306/LANMSG?serverTimezone=UTC";
	/** 数据库登录信息 */
	static private Properties loginInfo = null;
	/** 数据库驱动 */
	static public Driver driver;
	/** 数据库驱动类加载器 */
	static private URLClassLoader classLoader;

	/**
	 * 初始化并启动数据库驱动
	 * 
	 * @throws MalformedURLException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static void launch()
			throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		// 数据库驱动链接库地址，请确保libs文件夹中有mysql-connector-java-8.0.13.jar这个文件！需自行下载
		URL url = new URL("file:./libs/mysql-connector-java-8.0.13.jar");
		// 数据库驱动类加载器
		classLoader = new URLClassLoader(new URL[] { url });
		// 数据库驱动类
		Class<?> clazz = classLoader.loadClass("com.mysql.cj.jdbc.Driver");
		// 驱动实例化
		driver = (Driver) clazz.newInstance();
		// 设置登录信息
		loginInfo = new Properties();
		loginInfo.setProperty("user", "root");
		loginInfo.setProperty("password", "123456");
	}

	/**
	 * 连接到数据库，相当于网页时候的DriverManager.getConnection(CONNECT_URL, "root", "123456")
	 * 
	 * @throws SQLException
	 */

	static Connection connect() throws SQLException {
		return driver.connect(CONNECT_URL, loginInfo);
	}
}
