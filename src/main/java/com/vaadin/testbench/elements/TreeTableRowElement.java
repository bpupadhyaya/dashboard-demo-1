package com.vaadin.testbench.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TreeTableRowElement extends TableRowElement {

    /**
     * Either expand collapsed row or collapse expanded row.
     */
    public void toggleExpanded() {
        List<WebElement> expandButtons = getWrappedElement().findElements(
                By.className("v-treetable-treespacer"));
        if (expandButtons.size() > 0) {
            expandButtons.get(0).click();
        }
    }
}
