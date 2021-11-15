/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.umlgraph.doclet;


/**
 * Properties of node shapes
 *
 * @version $Revision$
 * @author <a href="http://www.spinellis.gr">Diomidis Spinellis</a>
 */
public class Shape {

    /** Shape's UMLGraph name */
    private String name;

    /** Construct a default (class) Shape */
    public Shape() {
	name = "class";
    }

    /** Construct a Shape through the specified UMLGraph name */
    public Shape(String n) {
	name = n;
	if (graphvizAttribute() == null) {
	    System.err.println("Ignoring invalid shape " + n);
	    name = "class";
	}
    }

    /**
     * Return the GraphViz shape name corresponding to the shape
     */
    public String graphvizAttribute() {
	if (name.equals("class"))
	    return "";		// Default; plaintext
	else if (name.equals("note"))
	    return ", shape=note";
	else if (name.equals("node"))
	    return ", shape=box3d";
	else if (name.equals("component"))
	    return ", shape=component";
	else if (name.equals("package"))
	    return ", shape=tab";
	else if (name.equals("collaboration"))
	    return ", shape=ellipse, style=dashed";
	else if (name.equals("usecase"))
	    return ", shape=ellipse";
	else if (name.equals("activeclass"))
	    return "";		// Default; plaintext
	else
	    return null;
    }

    /** Return the shape's GraphViz landing port */
    String landingPort() {
	if (name.equals("class") || name.equals("activeclass"))
	    return ":p";
	else
	    return "";
    }

    /** Return the table border required for the shape */
    String extraColumn(int nRows) {
	return name.equals("activeclass") ? ("<td rowspan=\"" + nRows + "\"></td>") : "";
    }

    /** Return the cell border required for the shape */
    String cellBorder() {
	return (name.equals("class") || name.equals("activeclass")) ? "1" : "0";
    }
}
