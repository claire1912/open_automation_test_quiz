package cn.ianzhang.automation.stepdefinitions;



import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.File;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.commons.io.FileUtils;


/**
 * 企业复工申请资料提交表(Cucumber)
 * 请基于Cucumber，以BDD的形式，自行实现所需的Feature和Gherkin，用于以下操作
 * 打开网页 企业复工申请资料提交表 请在第一页填写以下内容 “请选择贵单位情况”的选项组中选择 “连续生产/开工类企事业单位” 将第一页进行截图 点击下一页按钮 请在第二页填写以下内容
 * 栏位内容
 * 申请日期 运行脚本的当年元旦日期
 * 申请人 自动化
 * 联系方式 1388888888
 * 点击下一页按钮 将第二页进行截图 请在第三页填写以下内容
 * 栏位内容
 * 报备单位 测试公司
 * 在岗人数 99
 * 报备日期 执行测试的日期
 * 湖北籍员工、前往湖北以及与湖北人员密切接触的员工（人数） 0
 * 单位负责人 您的姓名
 * 联系方式 13888888888
 * 疫情防控方案 测试内容
 * 将第三页进行截图 点击提交按钮 判断提交后弹框信息 将提交结果页进行截图
 * 测试完成后，应生成相应的HTML格式的测试报告
 */




public class EnterpriseSteps {
    private WebDriver driver;
    private String baseUrl = "https://jinshuju.net/templates/detail/Dv9JPD"; // 替换实际URL

    @Given("打开企业复工申请资料提交表页面")
    public void openApplicationPage() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get(baseUrl);
        driver.manage().window().fullscreen();
        Thread.sleep(10000);
    }

    @When("在第一页选择{string}并点击下一页")
    public void fillFirstPage(String option) throws Exception {

        WebElement iframe = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//iframe[@class='TemplatePreview_iframe__Ep1Or']")));
        driver.switchTo().frame(iframe);
        System.out.printf("---------------iframe---------------");

        WebElement radio = driver.findElement(By.xpath("/html/body/div/div/div/form/div[3]/div/div[2]/div/div/div[2]/div[1]/div/div/div/div/div[2]/div/div/div/label/span[1]/input"));
        radio.click();
        takeScreenshot("page1_screenshot");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[4]/div[1]/button")).click();
    }

    @When("在第二页填写申请信息并点击下一页")
    public void fillSecondPage() throws Exception {
        LocalDate newYear = LocalDate.now().withDayOfYear(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        fillFiedXpath("//*[@id=\"root\"]/div/form/div[2]/div/div[4]/div/div/div[2]/div[1]/div/div/div/div/span/input", newYear.format(formatter));
        fillField("TextInputfield_19", "自动化");
        fillFiedXpath("//*[@id=\"root\"]/div/form/div[2]/div/div[8]/div/div/div[2]/div[1]/div/div/div/div/span/input", "1388888888");

        takeScreenshot("page2_screenshot");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/form/div[4]/div[1]/button")).click();
    }

    @When("在第三页填写备案信息并提交")
    public void fillThirdPage() throws Exception {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        fillField("TextInputfield_23", "测试公司");
        fillFiedXpath("//*[@id=\"root\"]/div/form/div[2]/div/div[6]/div/div/div[2]/div[1]/div/div/div/div/div/div/div[2]/input", "99");
        fillFiedXpath("//*[@id=\"root\"]/div/form/div[2]/div/div[8]/div/div/div[2]/div[1]/div/div/div/div/span/input", today.format(formatter));
        fillFiedXpath("//*[@id=\"root\"]/div/form/div[2]/div/div[10]/div/div/div[2]/div[1]/div/div/div/div/div/div/div[2]/inpu", "0");
        fillField("TextInputfield_27", "张伟"); // 替换为实际姓名
        fillFiedXpath("//*[@id=\"root\"]/div/form/div[2]/div/div[14]/div/div/div[2]/div[1]/div/div/div/div/span/input", "13888888888");
        fillFiedXpath("/html/body/div/div/div/form/div[2]/div/div[16]/div/div/div[2]/div[1]/div/div[2]/div/span/textarea", "测试内容");

        takeScreenshot("page3_screenshot");
        driver.findElement(By.id("submitBtn")).click();
    }

    @Then("验证提交成功提示信息为{string}")
    public void verifySuccessMessage(String expected) {
        Duration timeout = Duration.ofMillis(10);

        WebDriverWait wait = new WebDriverWait(driver, timeout);
        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("alert-message")));
        assert alert.getText().contains(expected);
        driver.quit();
    }

    @Then("{string}截图保存为{string}")
    public void takeScreenshot(String pageName) throws Exception {
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File("target/screenshots/"+pageName+".png"));
    }

    private void fillField(String fieldId, String value) {
        WebElement element = driver.findElement(By.id(fieldId));
        element.clear();
        element.sendKeys(value);
    }


    private void fillFiedXpath(String fieldxpath, String value) {
        WebElement element = driver.findElement(By.xpath(fieldxpath));
        element.clear();
        element.sendKeys(value);
    }
}
