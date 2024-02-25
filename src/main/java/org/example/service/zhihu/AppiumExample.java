package org.example.service.zhihu;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AppiumExample {

    public static void main(String[] args) {
        // 设置DesiredCapabilities，指定测试设备和应用信息
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "device");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appPackage", "com.zhihu.android");
        capabilities.setCapability("appActivity", "com.zhihu.android.app.ui.activity.MainActivity");

        // 启动Appium服务器，指定Appium服务器地址
        URL appiumServerURL;
        try {
            appiumServerURL = new URL("http://127.0.0.1:4723/wd/hub");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Appium server URL: " + e.getMessage());
        }

        // 创建AndroidDriver实例，连接到设备
        AppiumDriver<MobileElement> driver = new AndroidDriver<>(appiumServerURL, capabilities);

        // 隐式等待，等待元素出现的最长时间
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // 执行一些基本操作，例如点击按钮、输入文本等
        MobileElement button = driver.findElementById("com.example.myapp:id/button");
        button.click();

        MobileElement textField = driver.findElementById("com.example.myapp:id/editText");
        textField.sendKeys("Hello, Appium!");

        // 关闭应用
        driver.quit();
    }
}
