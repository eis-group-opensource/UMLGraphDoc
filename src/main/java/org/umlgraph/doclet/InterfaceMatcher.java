/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.umlgraph.doclet;

import java.util.regex.Pattern;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

/**
 * Matches every class that implements (directly or indirectly) an
 * interfaces matched by regular expression provided.
 */
public class InterfaceMatcher implements ClassMatcher {

    protected RootDoc root;
    protected Pattern pattern;

    public InterfaceMatcher(RootDoc root, Pattern pattern) {
	this.root = root;
	this.pattern = pattern;
    }

    public boolean matches(ClassDoc cd) {
	// if it's the interface we're looking for, match
	if(cd.isInterface() && pattern.matcher(cd.toString()).matches())
	    return true;
	
	// for each interface, recurse, since classes and interfaces 
	// are treated the same in the doclet API
	for (ClassDoc iface : cd.interfaces()) {
	    if(matches(iface))
		return true;
	}
	
	// recurse on supeclass, if available
	if(cd.superclass() != null)
	    return matches(cd.superclass());
	
	return false;
    }

    public boolean matches(String name) {
	ClassDoc cd = root.classNamed(name);
	if(cd == null)
	    return false;
	return matches(cd);
    }

}
