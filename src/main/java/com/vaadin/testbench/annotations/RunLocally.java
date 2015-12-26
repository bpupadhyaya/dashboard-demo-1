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
package com.vaadin.testbench.annotations;

import com.vaadin.testbench.parallel.Browser;
import com.vaadin.testbench.parallel.ParallelTest;
import org.openqa.selenium.WebDriver;

import java.lang.annotation.*;

/**
 * <p>
 * Marks this testcase to run locally.<br>
 * Can receive a parameter to define which browser to run test on.<br>
 * </p>
 * 
 * <p>
 * This parameter will be used to instantiate the {@link WebDriver}.<br>
 * The value can be obtained through {@link ParallelTest#getRunLocallyBrowser()}
 * and {@link ParallelTest#getRunLocallyBrowserVersion()}
 * </p>
 * 
 * <p>
 * Usage:<br>
 * {@code @RunLocally(Browser.CHROME)}
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface RunLocally {
    public Browser value() default Browser.FIREFOX;

    public String version() default "";
}
