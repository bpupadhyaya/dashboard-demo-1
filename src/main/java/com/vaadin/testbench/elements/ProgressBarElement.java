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

import com.vaadin.testbench.elementsbase.ServerClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@ServerClass("com.vaadin.ui.ProgressBar")
public class ProgressBarElement extends AbstractFieldElement {

    /**
     * Retrns the value of the progress bar
     * 
     * @return
     */
    public double getValue() {
        WebElement indicator = findElement(By
                .className("v-progressbar-indicator"));
        String width = getStyleAttribute(indicator, "width");
        if (!width.endsWith("%")) {
            return 0;
        }

        return Double.parseDouble(width.replace("%", "")) / 100.0;
    }

}
