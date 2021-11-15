/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.umlgraph.doclet;

import com.sun.javadoc.ClassDoc;

/**
 * A ClassMatcher is used to check if a class definition matches a
 * specific condition. The nature of the condition is dependent on
 * the kind of matcher 
 * @author wolf
 */
public interface ClassMatcher {
    /**
     * Returns the options for the specified class. 
     */
    public boolean matches(ClassDoc cd);
    
    /**
     * Returns the options for the specified class. 
     */
    public boolean matches(String name);
}
