/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.umlgraph.doclet;

import com.sun.javadoc.ProgramElementDoc;

/**
 * Enumerates the possible visibilities in a Java program. For brevity, package
 * private visibility is referred as PACKAGE.
 * @author wolf
 * 
 */
public enum Visibility {
    PRIVATE, PACKAGE, PROTECTED, PUBLIC;

    public static Visibility get(ProgramElementDoc doc) {
	if (doc.isPrivate())
	    return PRIVATE;
	else if (doc.isPackagePrivate())
	    return PACKAGE;
	else if (doc.isProtected())
	    return PROTECTED;
	else
	    return PUBLIC;

    }
}