package me.trent;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.List;

public class Bot {

    private String name;
    private String username;
    private List<String> windows = new ArrayList<>();
    private String proxy;

    public Bot(String botName, String username) {
        this.name = botName;
        this.username = username;
        //this.proxy = proxy;
    }

    public void run() throws InterruptedException {
        //Utils.log("proxy: "+proxy);
        System.setProperty("webdriver.edge.driver", "C:\\Users\\trent\\Desktop\\Libs\\msedgedriver.exe");

        //Proxy p = new org.openqa.selenium.Proxy();
        //p.setHttpProxy(proxy).setSslProxy(proxy);
//
        //EdgeOptions options = new EdgeOptions();
        //options.setProxy(p);
        //options.setCapability(CapabilityType.PROXY, p);

        WebDriver driver = new EdgeDriver();
        driver.get("https://www.instagram.com/accounts/emailsignup/");
        Thread.sleep(2000);

        try {
            String currentName = getUsername();

            WebElement email = Utils.findElement(driver, Utils.Types.INPUT, "Mobile Number or Email");
            assert email != null;
            email.click();
            email.sendKeys(currentName + "@inboxkitten.com");

            WebElement fullName = Utils.findElement(driver, Utils.Types.INPUT, "Full Name");
            assert fullName != null;
            fullName.click();
            fullName.sendKeys(currentName);

            WebElement username = Utils.findElement(driver, Utils.Types.INPUT, "Username");
            assert username != null;
            username.click();
            username.sendKeys(currentName);

            WebElement password = Utils.findElement(driver, Utils.Types.INPUT, "Password");
            assert password != null;
            password.click();
            password.sendKeys(currentName + "password69420!");

            Thread.sleep(100);

            WebElement signupButton = Utils.findElement(driver, Utils.Types.BUTTON, "Sign up");
            assert signupButton != null;
            Utils.log("Clicking Sign Up...");
            signupButton.click();
            Thread.sleep(500);

            WebElement yearSelect = Utils.findElement(driver, Utils.Types.SELECT, "Year:");
            assert yearSelect != null;
            yearSelect.click();

            WebElement optionSelect = Utils.findElement(driver, Utils.Types.OPTION, "1992");
            assert optionSelect != null;
            optionSelect.click();

            Thread.sleep(200);

            WebElement nextButton = Utils.findElement(driver, Utils.Types.BUTTON, "Next");
            assert nextButton != null;
            Utils.log("Clicking Next...");
            nextButton.click();
            Thread.sleep(1000);


            Utils.createNewTab(driver); // open the new tab
            Thread.sleep(5000);
            windows.addAll(driver.getWindowHandles()); // add all of the current window handles...
            //driver.navigate().to(url);
            driver.switchTo().window(windows.get(1));//switch to the new window
            driver.navigate().to("https://inboxkitten.com/inbox/" + getUsername() + "/list");
            //scan the new page for our code...
            Thread.sleep(20000);
            WebElement loginCode = Utils.findElement(driver, Utils.Types.DIV, "row-subject");
            String codeString = loginCode.getText();

            String goodCode = codeString.split(" ")[0].trim();

            driver.close();

            driver.switchTo().window(windows.get(0));//switch to the original window...
            Thread.sleep(200);



            //driver.switchTo().window(windows.get(0));

            WebElement emailCode = Utils.findElement(driver, Utils.Types.INPUT, "Confirmation Code");
            assert emailCode != null;
            emailCode.click();
            emailCode.sendKeys(goodCode);

            WebElement nextButton2 = Utils.findElement(driver, Utils.Types.BUTTON, "Next");
            assert nextButton2 != null;
            Utils.log("Clicking Next...");
            nextButton2.click();
            Thread.sleep(200);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
}