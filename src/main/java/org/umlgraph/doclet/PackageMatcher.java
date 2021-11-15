/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.umlgraph.doclet;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;

public class PackageMatcher implements ClassMatcher {
    protected PackageDoc packageDoc;

    public PackageMatcher(PackageDoc packageDoc) {
	super();
	this.packageDoc = packageDoc;
    }

    public boolean matches(ClassDoc cd) {
	return cd.containingPackage().equals(packageDoc);
    }

    public boolean matches(String name) {
	for (ClassDoc cd : packageDoc.allClasses()) {
	    if (cd.qualifiedName().equals(name))
		return true;
	}
	return false;
    }

}
