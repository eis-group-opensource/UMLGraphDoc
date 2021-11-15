/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.umlgraph.doclet;

import java.util.*;

/**
 * String utility functions
 * @version $Revision$
 * @author <a href="http://www.spinellis.gr">Diomidis Spinellis</a>
 */
class StringUtil {
    /** Tokenize string s into an array */
    public static String[] tokenize(String s) {
	ArrayList<String> r = new ArrayList<String>();
	String remain = s;
	int n = 0, pos;

	remain = remain.trim();
	while (remain.length() > 0) {
	    if (remain.startsWith("\"")) {
		// Field in quotes
		pos = remain.indexOf('"', 1);
		if (pos == -1)
		    break;
		r.add(remain.substring(1, pos));
		if (pos + 1 < remain.length())
		    pos++;
	    } else {
		// Space-separated field
		pos = remain.indexOf(' ', 0);
		if (pos == -1) {
		    r.add(remain);
		    remain = "";
		} else
		    r.add(remain.substring(0, pos));
	    }
	    remain = remain.substring(pos + 1);
	    remain = remain.trim();
	    // - is used as a placeholder for empy fields
	    if (r.get(n).equals("-"))
		r.set(n, "");
	    n++;
	}
	return r.toArray(new String[0]);
    }

}
