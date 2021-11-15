/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.umlgraph.doclet;

/**
 * The type of relation that links two entities
 * @author wolf
 * 
 */
public enum RelationType {
    ASSOC, NAVASSOC, HAS, NAVHAS, COMPOSED, NAVCOMPOSED, DEPEND, EXTENDS, IMPLEMENTS;
}
