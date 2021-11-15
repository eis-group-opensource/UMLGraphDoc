/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.umlgraph.doclet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;

/**
 * Doclet API implementation
 * 
 * @depend - - - OptionProvider
 * @depend - - - Options
 * @depend - - - View
 * @depend - - - ClassGraph
 * @depend - - - Version
 * 
 * @version $Revision$
 * @author <a href="http://www.spinellis.gr">Diomidis Spinellis</a>
 */
public class UmlGraph {

	private static final String programName = "UmlGraph";
	private static final String docletName = "org.umlgraph.doclet.UmlGraph";

	/** Options used for commenting nodes */
	private static Options commentOptions;

	/** Entry point through javadoc */
	public static boolean start(RootDoc root) throws IOException {
		System.out.println("STARTED " + UmlGraph.class.getName());
		Options opt = buildOptions(root);
		// root.printNotice("UMLGraph doclet version " + Version.VERSION +
		// " started");

		View[] views = buildViews(opt, root, root);
		if (views == null)
			return false;
		if (views.length == 0)
			buildGraph(root, opt, null);
		else
			for (int i = 0; i < views.length; i++)
				buildGraph(root, views[i], null);
		return true;
	}

	public static void main(String args[]) {
		PrintWriter err = new PrintWriter(System.err);
		com.sun.tools.javadoc.Main.execute(programName, err, err, err, docletName, args);
	}

	public static Options getCommentOptions() {
		return commentOptions;
	}

	/**
	 * Creates the base Options object. This contains both the options specified
	 * on the command line and the ones specified in the UMLOptions class, if
	 * available. Also create the globally accessible commentOptions object.
	 */
	public static Options buildOptions(RootDoc root) {
		commentOptions = new Options();
		commentOptions.setOptions(root.options());
		commentOptions.setOptions(findClass(root, "UMLNoteOptions"));
		commentOptions.shape = new Shape("note");

		Options opt = new Options();
		opt.setOptions(root.options());
		opt.setOptions(findClass(root, "UMLOptions"));
		return opt;
	}

	/** Return the ClassDoc for the specified class; null if not found. */
	private static ClassDoc findClass(RootDoc root, String name) {
		ClassDoc[] classes = root.classes();
		for (ClassDoc cd : classes)
			if (cd.name().equals(name))
				return cd;
		return null;
	}

	/**
	 * Builds and outputs a single graph according to the view overrides
	 */
	public static void buildGraph(RootDoc root, OptionProvider op, Doc contextDoc) throws IOException {
		if (getCommentOptions() == null)
			buildOptions(root);
		Options opt = op.getGlobalOptions();
		root.printNotice("Building " + op.getDisplayName());
		ClassDoc[] classes = root.classes();

		ClassGraph c = new ClassGraph(root, op, contextDoc);
		c.prologue();
		for (int i = 0; i < classes.length; i++)
			c.printClass(classes[i], true);
		for (int i = 0; i < classes.length; i++)
			c.printRelations(classes[i]);
		if (opt.inferRelationships)
			c.printInferredRelations(classes);
		if (opt.inferDependencies)
			c.printInferredDependencies(classes);

		c.printExtraClasses(root);
		c.epilogue();
	}

	/**
	 * Builds the views according to the parameters on the command line
	 * 
	 * @param opt
	 *            The options
	 * @param srcRootDoc
	 *            The RootDoc for the source classes
	 * @param viewRootDoc
	 *            The RootDoc for the view classes (may be different, or may be
	 *            the same as the srcRootDoc)
	 */
	public static View[] buildViews(Options opt, RootDoc srcRootDoc, RootDoc viewRootDoc) {
		if (opt.viewName != null) {
			ClassDoc viewClass = viewRootDoc.classNamed(opt.viewName);
			if (viewClass == null) {
				System.out.println("View " + opt.viewName
						+ " not found! Exiting without generating any output.");
				return null;
			}
			if (viewClass.tags("view").length == 0) {
				System.out.println(viewClass + " is not a view!");
				return null;
			}
			if (viewClass.isAbstract()) {
				System.out.println(viewClass + " is an abstract view, no output will be generated!");
				return null;
			}
			return new View[] { buildView(srcRootDoc, viewClass, opt) };
		} else if (opt.findViews) {
			List<View> views = new ArrayList<View>();
			ClassDoc[] classes = viewRootDoc.classes();

			// find view classes
			for (int i = 0; i < classes.length; i++)
				if (classes[i].tags("view").length > 0 && !classes[i].isAbstract())
					views.add(buildView(srcRootDoc, classes[i], opt));

			return views.toArray(new View[views.size()]);
		} else
			return new View[0];
	}

	/**
	 * Builds a view along with its parent views, recursively
	 */
	private static View buildView(RootDoc root, ClassDoc viewClass, OptionProvider provider) {
		ClassDoc superClass = viewClass.superclass();
		if (superClass == null || superClass.tags("view").length == 0)
			return new View(root, viewClass, provider);

		return new View(root, viewClass, buildView(root, superClass, provider));
	}

	/** Option checking */
	public static int optionLength(String option) {
		return Options.optionLength(option);
	}

	/** Indicate the language version we support */
	public static LanguageVersion languageVersion() {
		return LanguageVersion.JAVA_1_5;
	}
}
