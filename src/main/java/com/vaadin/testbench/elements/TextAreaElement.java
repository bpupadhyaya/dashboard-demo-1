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
import org.openqa.selenium.Keys;

@ServerClass("com.vaadin.ui.TextArea")
public class TextAreaElement extends AbstractTextFieldElement {
    /**
     * Return value of the field element
     *
     * @since
     * @return value of the field element
     */
    @Override
    public String getValue() {
        return getAttribute("value");
    }

    @Override
    public void setValue(CharSequence chars) {
        if (isReadOnly()) {
            throw new ReadOnlyException();
        }
        // clears without triggering an event
        clearElementClientSide(this);
        focus();
        sendKeys(chars);
        sendKeys(Keys.TAB);
    }
}
