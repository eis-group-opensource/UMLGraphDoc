/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.umlgraph.doclet;

import java.util.HashMap;
import java.util.Map;

/**
 * Class's dot-compatible alias name (for fully qualified class names)
 * and printed information
 * @version $Revision$
 * @author <a href="http://www.spinellis.gr">Diomidis Spinellis</a>
 */
class ClassInfo {
    private static int classNumber;
    /** Alias name for the class */
    String name;
    /** True if the class class node has been printed */
    boolean nodePrinted;
    /** True if the class class node is hidden */
    boolean hidden;
    /** 
     * The list of classes that share a relation with this one. Contains
     * all the classes linked with a bi-directional relation , and the ones 
     * referred by a directed relation 
     */
    Map<String, RelationPattern> relatedClasses = new HashMap<String, RelationPattern>();

    ClassInfo(boolean p, boolean h) {
	nodePrinted = p;
	hidden = h;
	name = "c" + (new Integer(classNumber)).toString();
	classNumber++;
    }
    
    public void addRelation(String dest, RelationType rt, RelationDirection d) {
	RelationPattern ri = relatedClasses.get(dest);
	if(ri == null) {
	    ri = new RelationPattern(RelationDirection.NONE);
	    relatedClasses.put(dest, ri);
	}
	ri.addRelation(rt, d);
    }
    
    public RelationPattern getRelation(String dest) {
	return relatedClasses.get(dest);
    }

    /** Start numbering from zero. */
    public static void reset() {
	classNumber = 0;
    }

    
}

