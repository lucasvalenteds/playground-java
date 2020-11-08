package com.playground.java.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.assertj.core.api.Assertions.assertThat;

@DisabledIfEnvironmentVariable(named = "CI_SERVER", matches = "yes")
class MainTest extends ServerStub {

    private final ChromeOptions capabilities = new ChromeOptions()
        .setHeadless(true);

    private final WebDriver browser = new ChromeDriver(capabilities);

    @BeforeEach
    void beforeEach() {
        browser.get(serverUrl);

        assertThat(browser.getCurrentUrl()).startsWith(serverUrl);
    }

    @AfterEach
    void afterEach() {
        browser.close();
    }

    @DisplayName("It can find elements by class, id and tag name")
    @Test
    void testFindingThings() {
        var advice = browser.findElement(By.id("advice"));
        var lorem = browser.findElement(By.className("some-text"));
        var h1 = browser.findElement(By.tagName("h1"));

        assertThat(advice.getText()).isNotEmpty();
        assertThat(lorem.getText()).isNotEmpty();
        assertThat(h1.getText()).isEqualTo("Hello World");
    }

    @DisplayName("It's possible to type, click a button and wait a redirect")
    @Test
    void testInteracting() {
        var wait = new WebDriverWait(browser, 10);
        var elementThatWillDisappear = browser.findElement(By.name("changer"));

        browser.findElement(By.tagName("input")).sendKeys("Selenium is awesome");
        browser.findElement(By.tagName("button")).click();
        wait.until(driver -> ExpectedConditions.stalenessOf(elementThatWillDisappear));

        assertThat(browser.getTitle()).isNotEqualTo("Sample page");
    }

    @DisplayName("Page URL and source code is available for inspection")
    @Test
    void testPageInfo() {
        assertThat(browser.getTitle()).isEqualTo("Sample page");
        assertThat(browser.getPageSource()).contains("<h1>Hello World</h1>");
    }

    @DisplayName("It's possible to click the back button")
    @Test
    void testNavigationBack() {
        browser.findElement(By.tagName("button")).click();

        browser.navigate().back();

        assertThat(browser.getCurrentUrl()).startsWith(serverUrl);
    }

    @DisplayName("It's possible to click the forward button")
    @Test
    void testNavigationForward() {
        browser.findElement(By.tagName("button")).click();

        browser.navigate().back();
        browser.navigate().forward();

        assertThat(browser.getCurrentUrl()).startsWith("http://localhost:8081/");
    }

    @DisplayName("Page refresh can be intercepted")
    @Test
    void testPageRefresh() {
        var wait = new WebDriverWait(browser, 1);

        browser.navigate().refresh();
        wait.until(webDriver -> ExpectedConditions.visibilityOf(webDriver.findElement(By.tagName("html"))));

        assertThat(browser.getTitle()).isEqualTo("Sample page");
    }

    @DisplayName("It's possible to resize a window to a specific width and height")
    @Test
    void testWindowSize() {
        var currentWindow = browser.manage().window();
        var originalSize = currentWindow.getSize();
        var changedSize = new Dimension(512, 512);

        currentWindow.setSize(changedSize);

        assertThat(originalSize).isNotEqualTo(changedSize);
        assertThat(currentWindow.getSize()).isEqualTo(changedSize);
    }
}
