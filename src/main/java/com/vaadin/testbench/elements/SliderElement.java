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
package com.vaadin.testbench.elements;

import com.vaadin.testbench.By;
import com.vaadin.testbench.elementsbase.ServerClass;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;

import java.util.List;

@ServerClass("com.vaadin.ui.Slider")
public class SliderElement extends AbstractFieldElement {
    /**
     * Get value of the slider
     * 
     * Warning! This method cause slider popup to appear on the screen. To hide
     * this popup just focus any other element on the page.
     */
    public String getValue() {
        List<WebElement> popupElems = findElements(By.vaadin("#popup"));
        // SubPartAware was implemented after 7.2.6, not sure in which release
        // it will be included
        if (popupElems.isEmpty()) {
            throw new UnsupportedOperationException(
                    "Current version of vaadin doesn't support geting values from sliderElement");

        }
        WebElement popupElem = popupElems.get(0);

        if (BrowserType.IE.equals(getCapabilities().getBrowserName())
                && "8".equals(getCapabilities().getVersion())) {
            return popupElem.getAttribute("innerText");
        } else {
            return popupElem.getAttribute("textContent");
        }

    }

    public WebElement getHandle() {
        return findElement(By.className("v-slider-handle"));
    }
}
