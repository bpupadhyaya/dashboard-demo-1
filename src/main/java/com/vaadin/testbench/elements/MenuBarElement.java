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
import com.vaadin.testbench.exceptions.LowVaadinVersionException;
import com.vaadin.testbench.util.VersionUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.NoSuchElementException;

@ServerClass("com.vaadin.ui.MenuBar")
public class MenuBarElement extends AbstractComponentElement {

    private Point lastItemLocationMovedTo = null;

    /**
     * Clicks on a visible item.<br>
     * If the item is a top level menu, the submenu is opened if it was closed,
     * or closed if it was opened.<br>
     * If the item is another submenu, that submenu is opened.<br>
     * If the item is not a submenu, it will be clicked and trigger any actions
     * associated to it.
     * 
     * @param item
     *            name of the item to click
     * @throws NullPointerException
     *             if item does not exist or is not visible
     */
    private void clickItem(String item) {
        WebElement webElement = getVisibleItem("#" + item);
        if (webElement == null) {
            throw new NoSuchElementException("Menu item " + item
                    + " is not available.");
        }
        activateOrOpenSubmenu(webElement, true);
    }

    /**
     * Clicks the item specified by a full path given as variable arguments.<br>
     * Fails if path given is not full (ie: last submenu is already opened, and
     * path given is last item only).
     * <p>
     * Example:
     * </p>
     *
     * <pre>
     * // clicks on &quot;File&quot; item
     * menuBarElement.click(&quot;File&quot;);
     * // clicks on &quot;Copy&quot; item in &quot;File&quot; top level menu.
     * menuBarElement.click(&quot;File&quot;, &quot;Copy&quot;);
     * </pre>
     *
     * @param path
     *            Array of items to click through
     */
    public void clickItem(String... path) {
        if (!isMenuBarApiSupported()) {
            throw new LowVaadinVersionException(String.format(
                    "Vaadin version required: %d.%d.%d",
                    vaadinMajorVersionRequired, vaadinMinorVersionRequired,
                    vaadinRevisionRequired));
        }

        if (path.length > 1) {
            closeAll();
        }
        for (String itemName : path) {
            clickItem(itemName);
        }
    }

    /**
     * Closes all submenus, if any is open.<br>
     * This is done by clicking on the currently selected top level item.
     */
    private void closeAll() {
        if (!isMenuBarApiSupported()) {
            throw new LowVaadinVersionException(String.format(
                    "Vaadin version required: %d.%d.%d",
                    vaadinMajorVersionRequired, vaadinMinorVersionRequired,
                    vaadinRevisionRequired));
        }

        lastItemLocationMovedTo = null;
        WebElement selectedItem = getSelectedTopLevelItem();
        if (selectedItem != null) {
            activateOrOpenSubmenu(selectedItem, true);
        }
    }

    private WebElement getSelectedTopLevelItem() {
        List<WebElement> selectedItems = findElements(By
                .className("v-menubar-menuitem-selected"));
        if (selectedItems.size() == 0) {
            return null;
        }
        return selectedItems.get(0);
    }

    private WebElement getVisibleItem(String item) {
        return findElement(com.vaadin.testbench.By.vaadin(item));
    }

    private void activateOrOpenSubmenu(WebElement item, boolean alwaysClick) {

        if (lastItemLocationMovedTo == null || !isAnySubmenuVisible()) {
            item.click();
            if (hasSubmenu(item)) {
                lastItemLocationMovedTo = item.getLocation();
            }
            return;
        }

        // Assumes mouse is still at position of last clicked element
        Actions action = new Actions(getDriver());
        action.moveToElement(item);
        action.build().perform();

        if (isLeaf(item) || isSelectedTopLevelItem(item)) {
            lastItemLocationMovedTo = null;
        } else {
            lastItemLocationMovedTo = item.getLocation();
        }

        if (alwaysClick || isLeaf(item) || !isAnySubmenuVisible()) {
            action = new Actions(getDriver());
            action.click();
            action.build().perform();
        }
    }

    private boolean isSelectedTopLevelItem(WebElement item) {
        WebElement selectedItem = getSelectedTopLevelItem();
        if (selectedItem == null) {
            return false;
        }

        String itemCaption = item
                .findElements(By.className("v-menubar-menuitem-caption"))
                .get(0).getAttribute("innerHTML");
        String selectedItemCaption = selectedItem
                .findElements(By.className("v-menubar-menuitem-caption"))
                .get(0).getAttribute("innerHTML");
        return itemCaption.equals(selectedItemCaption);
    }

    private boolean isAnySubmenuVisible() {
        WebElement selectedItem = getSelectedTopLevelItem();
        if (selectedItem == null) {
            return false;
        }
        return hasSubmenu(selectedItem);
    }

    private boolean hasSubmenu(WebElement item) {
        List<WebElement> submenuIndicatorElements = item.findElements(By
                .className("v-menubar-submenu-indicator"));
        return submenuIndicatorElements.size() != 0;
    }

    private boolean isLeaf(WebElement item) {
        return !hasSubmenu(item);
    }

    private final int vaadinMajorVersionRequired = 7;
    private final int vaadinMinorVersionRequired = 3;
    private final int vaadinRevisionRequired = 4;

    /**
     * 
     * @return true if Vaadin version is high enough to have VMenuBar update
     *         needed in order to use this API. False otherwise.
     */
    private boolean isMenuBarApiSupported() {

        return VersionUtil
                .isAtLeast(vaadinMajorVersionRequired,
                        vaadinMinorVersionRequired, vaadinRevisionRequired,
                        getDriver());
    }
}
