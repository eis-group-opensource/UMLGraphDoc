/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.exigen.docletstarter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.umlgraph.doclet.UmlGraphDoc;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.RootDoc;
import com.sun.tools.javadoc.Main;

/**
 * @author szacharov
 * 
 * Exigen UmlGraphDoc Starter program-doclet
 * 
 * Starter Doclet collects parameters for project and writes them to file {output}\Parameters.txt;
 * Also doclet generates {output}\ClassPathsList.txt which is later used by Starter program to generate Javadoc only for particular classes
 * 
 * Starter Program Starts UmlGraphDoc doclet using {output}\Parameters.txt where changes -sourcepath to particular classes defined in ClassPathsList.txt
 * 
 * public static boolean start() - doclet generates parameters for UmlGraphDoc doclet
 * 
 * main() method executes UmlGraphDoc with generated parameters
 * 
 * parameters for Starter DOCLET:
 * -includeannotated Includes only classes annotated with defined annotations (Annotations separated by colon (":"), e.g. -includeannotated CanExtend:CanInvoke)
 * -fileslistpath - output for generated params, e.g. D:\Javadoc\Starter-output\
 * 
 * tested on EIS-4.10
 * 
 * more info: \\szacharov\Starter\
 */
//TODO refactor - quickly written code
public class Starter extends Doclet {

	private static final String INCLUDE_OPTION = "-includeannotated";
	private static final String OUTPUTPATH_OPT = "-fileslistpath";

	private static final String CLASS_PATHS_FILENAME = "ClassPathsList.txt";
	private static final String PARAMS_FILENAME = "Parameters.txt";

	/**
	 * folder that will be created inside defined by "-filesListPath" to store
	 * generated API
	 */
	private static final String API_OUTPUT = "docs";

	/**
	 * full path defined by option -fileslistpath contains @CLASS_PATHS_FILENAME
	 * & @PARAMS_FILENAME
	 */
	private static String filesListPath;

	/**
	 * final parameters without -doclet, -docletpath, -includeannotated,
	 * -fileslistpath
	 */
	private static String params;

	// for -includeannotated
	private static String[] annotations = null;
	
	/**
	 * Entry point for Starter Doclet
	 */
	public static boolean start(RootDoc root) {
		// List of path to every file
		List<String> classpaths = new ArrayList<String>();
		
		if (null != annotations) {
			ClassDoc[] classes = root.classes();
			for (ClassDoc clasz : classes) {
				if (!findAnnotation(clasz.annotations())) {
					root.printNotice("Excluding " + clasz.qualifiedName());
					continue;
				}
				classpaths.add(clasz.position().file().getPath());
			}
		}

		String options = getOptionsAsString(root.options());
		
		// generate "PARAMS_FILENAME" with all created parameters
		generateFile(params, options);
		
		options = "";
		for (String classPath : classpaths) {
			options += classPath + System.getProperty("line.separator");
		}
		
		// generate "CLASS_PATHS_FILENAME" with all included classes
		generateFile(filesListPath, options);
		return true;
	}

	/**
	 * Let every option be valid.
	 * 
	 * @param options
	 *            the options from the command line
	 * @param reporter
	 *            the error reporter
	 */
	public static boolean validOptions(String[][] options, DocErrorReporter reporter) {
		for (int i = 0; i < options.length; i++) {
			if (options[i][0].equalsIgnoreCase(INCLUDE_OPTION)) {
				readAnnotations(options[i][1]);
				continue;
			}
			if (options[i][0].equalsIgnoreCase(OUTPUTPATH_OPT)) {
				readFilesListOutputPath(options[i][1]);
				continue;
			}
		}
		return true;
	}

	/**
	 * Method required to validate the length of the given option. This is a bit
	 * ugly but the options must be hard coded here. Otherwise, Javadoc will
	 * throw errors when parsing options. We could delegate to the Standard
	 * doclet when computing option lengths, but then this doclet would be
	 * dependent on the version of J2SE used. Prefer to hard code options here
	 * so that this doclet can be used with 1.4.x or 1.5.x .
	 * 
	 * @param option
	 *            the option to compute the length for
	 */
	public static int optionLength(String option) {

		if (option.equalsIgnoreCase(INCLUDE_OPTION) || option.equalsIgnoreCase(OUTPUTPATH_OPT)) {
			return 2;
		}
		/* 1.4 Options Begin Here */

		/* 1.5 Options Begin Here */

		// General options
		if (option.equals("-author") || option.equals("-docfilessubdirs") || option.equals("-keywords")
				|| option.equals("-linksource") || option.equals("-nocomment")
				|| option.equals("-nodeprecated") || option.equals("-nosince")
				|| option.equals("-notimestamp") || option.equals("-quiet") || option.equals("-xnodate")
				|| option.equals("-version")) {
			return 1;
		} else if (option.equals("-d") || option.equals("-docencoding") || option.equals("-encoding")
				|| option.equals("-excludedocfilessubdir") || option.equals("-link")
				|| option.equals("-sourcetab") || option.equals("-noqualifier") || option.equals("-output")
				|| option.equals("-sourcepath") || option.equals("-tag") || option.equals("-taglet")
				|| option.equals("-tagletpath")) {
			return 2;
		} else if (option.equals("-group") || option.equals("-linkoffline")) {
			return 3;
		}

		// Standard doclet options
		option = option.toLowerCase();
		if (option.equals("-nodeprecatedlist") || option.equals("-noindex") || option.equals("-notree")
				|| option.equals("-nohelp") || option.equals("-splitindex") || option.equals("-serialwarn")
				|| option.equals("-use") || option.equals("-nonavbar") || option.equals("-nooverview")
				// TODO: UmlGraphDoc-specific option
				/*
				 * http://www.umlgraph.org/doc/indexw.html
				 * 
				 * add additional options if needed
				 */
				|| option.equals("-compact")) {
			return 1;
		} else if (option.equals("-footer") || option.equals("-header") || option.equals("-packagesheader")
				|| option.equals("-doctitle") || option.equals("-windowtitle") || option.equals("-bottom")
				|| option.equals("-helpfile") || option.equals("-stylesheetfile")
				|| option.equals("-charset") || option.equals("-overview")) {
			return 2;
		} else {
			return 0;
		}
	}

	/**
	 * Annotations splitted by colon
	 */
	private static void readAnnotations(String annotationsLine) {
		annotations = annotationsLine.split(":");
	}

	private static void readFilesListOutputPath(String outputPath) {
		filesListPath = outputPath + System.getProperty("file.separator") + CLASS_PATHS_FILENAME;
		params = outputPath + System.getProperty("file.separator") + PARAMS_FILENAME;
	}
	
	/**
	 * @param options - two-dim array from Starter Doclet
	 * @return plain String of options separated by whitespaces
	 */
	private static String getOptionsAsString(String[][] options) {
		String result = "";
		for (int i = 0; i < options.length; i++) {
			for (int j = 0; j < options[i].length; j++) {
				result += options[i][j] + " ";
			}
		}
		return result;
	}

	private static void generateFile(String filePath, String content) {
		System.out.println("Generating: " + filePath);
		try {
			File file = new File(filePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(content);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean findAnnotation(AnnotationDesc[] annotations) {
		for (AnnotationDesc annotation : annotations) {
			if (isAnnotated(annotation.annotationType().name())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * class annotations contains string, e.g. for class annotation
	 * 
	 * @SuppressWarnings("unchecked") returns true for Suppresswarnings or
	 * unchecked Strings
	 */
	private static boolean isAnnotated(String classAnnotation) {
		for (String annotation : annotations) {
			if (classAnnotation.contains(annotation)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param args
	 *            - path to folder which contains "PARAMS_FILENAME" &
	 *            "CLASS_PATHS_FILENAME"
	 * 
	 *            1) Take params, remove unneeded, replace "-sourcepath xxx" by
	 *            classes from "CLASS_PATHS_FILENAME" 2) add -d outputdir
	 */
	public static void main(String[] args) {
		String name = UmlGraphDoc.class.getName();
		String paramsFileContent = readFile(args[0] + PARAMS_FILENAME);
		List<String> files = readFileLines(args[0] + CLASS_PATHS_FILENAME);
		String[] filesArray = files.toArray(new String[files.size()]);
		String[] params = parseParams(paramsFileContent);
		String[] outputParam = new String[] { "-d",
				args[0] + API_OUTPUT + System.getProperty("file.separator") };

		// concat parameters array from params file and files array
		String[] finalParameters = (String[]) ArrayUtils.addAll(params, filesArray);

		// concat parameters array && -d outputPath
		finalParameters = (String[]) ArrayUtils.addAll(finalParameters, outputParam);

		Main.execute(name, name, finalParameters);
	}

	/**
	 * Reads file and returns its content as String
	 */
	private static String readFile(String filePath) {
		File file = new File(filePath);
		String content = null;
		try {
			content = FileUtils.readFileToString(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}

	/**
	 * Reads file and returns List of String lines
	 */
	private static List<String> readFileLines(String filePath) {
		List<String> result = null;
		File file = new File(filePath);
		try {
			result = FileUtils.readLines(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Takes String of unparsed parameters and removes unneeded
	 * 
	 * @return String array of params (to pass com.sun.tools.javadoc.Main())
	 */
	private static String[] parseParams(String unparsedParams) {
		/*
		 * FIX error org.umlgraph.doclet.UmlGraphDoc: error - Illegal package
		 * name: "Files/Java/jdk1.6.0_37/jre/../jre/lib/plugin.jar; No
		 * whitespaces allowed in -classpath - just replace program files
		 * whitespaces with '%' before splitting all params by whitespaces
		 */
		// TODO: refactor if possible
		unparsedParams = unparsedParams.replace("Program Files", "Program%Files");

		String[] splittedParams = unparsedParams.split(" ");

		/*
		 * remove first 4 params -doclet xxx -docletpath yyy
		 * 
		 * and -sourcepath xxx
		 */
		int deletedOptions = 0;

		for (int i = 0; i < splittedParams.length; i++) {
			if (splittedParams[i].equalsIgnoreCase("-sourcepath")
					|| splittedParams[i].equalsIgnoreCase("-docletpath")
					|| splittedParams[i].equalsIgnoreCase("-doclet")
					|| splittedParams[i].equalsIgnoreCase(INCLUDE_OPTION)
					|| splittedParams[i].equalsIgnoreCase(OUTPUTPATH_OPT)) {
				// delete tag and its value
				splittedParams[i] = "";
				splittedParams[i + 1] = "";
				deletedOptions += 2;
			}
		}
		String[] result = new String[splittedParams.length - deletedOptions];
		int i = 0;
		for (String param : splittedParams) {
			if (StringUtils.isNotEmpty(param)) {
				result[i++] = param;
			}
		}
		return result;
	}
}
