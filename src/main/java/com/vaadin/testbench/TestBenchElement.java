/**
 * Copyright (C) 2012 Vaadin Ltd
 *
 * This program is available under Commercial Vaadin Add-On License 3.0
 * (CVALv3).
 *
 * See the file licensing.txt distributed with this software for more
 * information about licensing.
 *
 * You should have received a copy of the license along with this program.
 * If not, see <http://vaadin.com/license/cval-3>.
 */
package com.vaadin.testbench;

import com.vaadin.testbench.By.ByVaadin;
import com.vaadin.testbench.commands.CanWaitForVaadin;
import com.vaadin.testbench.commands.TestBenchCommandExecutor;
import com.vaadin.testbench.commands.TestBenchElementCommands;
import com.vaadin.testbench.elementsbase.AbstractElement;
import org.openqa.selenium.By;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * TestBenchElement is a WebElement wrapper. It provides Vaadin specific helper
 * functionality. TestBenchElements are created when you search for elements
 * from TestBenchTestCase or a context relative search from TestBenchElement.
 */
public class TestBenchElement extends AbstractHasTestBenchCommandExecutor
        implements WrapsElement, WebElement, TestBenchElementCommands,
        CanWaitForVaadin, HasDriver {

    private WebElement actualElement = null;
    private TestBenchCommandExecutor tbCommandExecutor = null;

    protected TestBenchElement() {

    }

    protected TestBenchElement(WebElement webElement,
            TestBenchCommandExecutor tbCommandExecutor) {
        init(webElement, tbCommandExecutor);
    }

    /**
     * TestBenchElement initialization function. If a subclass of
     * TestBenchElement needs to run some initialization code, it should
     * override init(), not this function.
     *
     * @param element
     *            WebElement to wrap
     * @param tbCommandExecutor
     *            TestBenchCommandExecutor instance
     */
    protected void init(WebElement element,
            TestBenchCommandExecutor tbCommandExecutor) {
        if (null == this.tbCommandExecutor) {
            setCommandExecutor(tbCommandExecutor);
            actualElement = element;
            init();
        }
    }

    public boolean isPhantomJS() {
        return BrowserType.PHANTOMJS.equals(getCapabilities().getBrowserName());
    }

    public boolean isChrome() {
        return BrowserType.CHROME.equals(getCapabilities().getBrowserName());
    }

    /**
     * Returns information about current browser used
     *
     * @see Capabilities
     * @return information about current browser used
     */
    public Capabilities getCapabilities() {
        WebDriver driver;
        if (getDriver() instanceof TestBenchDriverProxy) {
            driver = ((TestBenchDriverProxy) getDriver()).getActualDriver();
        } else {
            driver = getDriver();
        }

        if (driver instanceof RemoteWebDriver) {
            return ((RemoteWebDriver) driver).getCapabilities();
        } else if (driver instanceof HtmlUnitDriver) {
            return ((HtmlUnitDriver) driver).getCapabilities();
        } else {
            return null;
        }
    }

    /**
     * This is run after initializing a TestBenchElement. This can be overridden
     * in subclasses of TestBenchElement to run some initialization code.
     */
    protected void init() {

    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.internal.WrapsElement#getWrappedElement()
     */
    @Override
    public WebElement getWrappedElement() {
        return actualElement;
    }

    @Override
    public void waitForVaadin() {
        getCommandExecutor().waitForVaadin();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.testbench.commands.TestBenchElementCommands#showTooltip()
     */
    @Override
    public void showTooltip() {
        new Actions(getCommandExecutor().getWrappedDriver()).moveToElement(
                actualElement).perform();
        // Wait for a small moment for the tooltip to appear
        try {
            Thread.sleep(1000); // VTooltip.OPEN_DELAY = 750;
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Sets the number of pixels that an element's content is scrolled from the
     * top.
     *
     * @param scrollTop
     *            value set to Element.scroll property
     * @see TestBenchElementCommands#scroll(int)
     */
    @Override
    public void scroll(int scrollTop) {
        JavascriptExecutor js = getCommandExecutor();
        js.executeScript("arguments[0].scrollTop = " + scrollTop, actualElement);
    }

    /**
     * Sets the number of pixels that an element's content is scrolled to the
     * left.
     *
     * @param scrollLeft
     *            value set to Element.scrollLeft property
     * @see TestBenchElementCommands#scrollLeft(int)
     */
    @Override
    public void scrollLeft(int scrollLeft) {
        JavascriptExecutor js = getCommandExecutor();
        js.executeScript("arguments[0].scrollLeft = " + scrollLeft,
                actualElement);
    }

    @Override
    public void click() {
        actualElement.click();
    }

    @Override
    public void submit() {
        actualElement.click();
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        actualElement.sendKeys(keysToSend);
    }

    @Override
    public void clear() {
        actualElement.clear();
    }

    @Override
    public String getTagName() {
        return actualElement.getTagName();
    }

    @Override
    public String getAttribute(String name) {
        return actualElement.getAttribute(name);
    }

    @Override
    public boolean isSelected() {
        return actualElement.isSelected();
    }

    /**
     * Returns whether the Vaadin component, that this element represents, is
     * enabled or not.
     *
     * @return true if the component is enabled.
     */
    @Override
    public boolean isEnabled() {
        return !actualElement.getAttribute("class").contains("v-disabled")
                && actualElement.isEnabled();
    }

    @Override
    public String getText() {
        return actualElement.getText();
    }

    @Override
    public List<WebElement> findElements(By by) {
        List<WebElement> elements = new ArrayList<WebElement>();
        if (by instanceof ByVaadin) {
            elements.addAll(wrapElements(by.findElements(this),
                    getCommandExecutor()));
        } else {
            elements.addAll(wrapElements(actualElement.findElements(by),
                    getCommandExecutor()));
        }
        return elements;
    }

    @Override
    public WebElement findElement(By by) {
        if (by instanceof ByVaadin) {
            return wrapElement(by.findElement(this), getCommandExecutor());
        }
        return wrapElement(actualElement.findElement(by), getCommandExecutor());
    }

    @Override
    public boolean isDisplayed() {
        return actualElement.isDisplayed();
    }

    @Override
    public Point getLocation() {
        return actualElement.getLocation();
    }

    @Override
    public Dimension getSize() {
        return actualElement.getSize();
    }

    @Override
    public String getCssValue(String propertyName) {
        return actualElement.getCssValue(propertyName);
    }

    @Override
    public void click(int x, int y, Keys... modifiers) {
        Actions actions = new Actions(getCommandExecutor().getWrappedDriver());
        actions.moveToElement(actualElement, x, y);
        // Press any modifier keys
        for (Keys modifier : modifiers) {
            actions.keyDown(modifier);
        }
        actions.click();
        // Release any modifier keys
        for (Keys modifier : modifiers) {
            actions.keyUp(modifier);
        }
        actions.build().perform();
    }

    public void doubleClick() {
        new Actions(getDriver()).doubleClick(actualElement).build().perform();
        // Wait till vaadin component will process the event. Without it may
        // cause problems with phantomjs
        waitForVaadin();
    }

    public void contextClick() {
        new Actions(getDriver()).contextClick(actualElement).build().perform();
        // Wait till vaadin component will process the event. Without it may
        // cause problems with phantomjs
        waitForVaadin();
    }

    @Override
    public <T extends AbstractElement> T wrap(Class<T> elementType) {
        return TestBench.createElement(elementType, getWrappedElement(),
                getCommandExecutor());
    }

    @Override
    public TestBenchCommandExecutor getTestBenchCommandExecutor() {
        return getCommandExecutor();
    }

    @Override
    public WebDriver getDriver() {
        return getCommandExecutor().getWrappedDriver();
    }

    /**
     * Returns this TestBenchElement cast to a SearchContext. Method provided
     * for compatibility and consistency.
     */
    @Override
    public SearchContext getContext() {
        return this;
    }

    /**
     * Move browser focus to this Element
     */
    @Override
    public void focus() {
        getCommandExecutor().focusElement(this);
    }

    protected static List<TestBenchElement> wrapElements(
            List<WebElement> elements,
            TestBenchCommandExecutor tbCommandExecutor) {
        List<TestBenchElement> wrappedList = new ArrayList<TestBenchElement>();

        for (WebElement e : elements) {
            wrappedList.add(wrapElement(e, tbCommandExecutor));
        }

        return wrappedList;
    }

    protected static TestBenchElement wrapElement(WebElement element,
            TestBenchCommandExecutor tbCommandExecutor) {
        if (element instanceof TestBenchElement) {
            return (TestBenchElement) element;
        } else {
            return TestBench.createElement(element, tbCommandExecutor);
        }
    }

    public TestBenchCommandExecutor getCommandExecutor() {
        return tbCommandExecutor;
    }

    private void setCommandExecutor(TestBenchCommandExecutor tbCommandExecutor) {
        this.tbCommandExecutor = tbCommandExecutor;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target)
            throws WebDriverException {
        return actualElement.getScreenshotAs(target);
    }

}
