/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.umlgraph.doclet;

import com.sun.javadoc.ClassDoc;

/**
 * A factory class that builds Options object for general use or for a
 * specific class
 */
public interface OptionProvider {
    /**
     * Returns the options for the specified class.
     */
    public Options getOptionsFor(ClassDoc cd);

    /**
     * Returns the options for the specified class.
     */
    public Options getOptionsFor(String name);

    /**
     * Returns the global options (the class independent definition)
     */
    public Options getGlobalOptions();

    /**
     * Gets a base Options and applies the overrides for the specified class
     */
    public void overrideForClass(Options opt, ClassDoc cd);

    /**
     * Gets a base Options and applies the overrides for the specified class
     */
    public void overrideForClass(Options opt, String className);

    /**
     * Returns user displayable name for this option provider.
     * <p>Will be used to provide progress feedback on the console
     */
    public String getDisplayName();
}
