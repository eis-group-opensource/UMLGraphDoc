/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.umlgraph.doclet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.javadoc.ClassDoc;

/**
 * Matches classes performing a regular expression match on the qualified class
 * name
 * @author wolf
 */
public class PatternMatcher implements ClassMatcher {
    Pattern pattern;

    public PatternMatcher(Pattern pattern) {
	this.pattern = pattern;
    }

    public boolean matches(ClassDoc cd) {
	return matches(cd.toString());
    }

    public boolean matches(String name) {
	Matcher matcher = pattern.matcher(name);
	return matcher.matches();
    }

}
