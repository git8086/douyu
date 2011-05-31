package org.douyu.core;

import java.io.File;

/**
 * 
 * @author ZHH
 *
 */
public class ClassResource {
	File sourceFile;
	long sourceFileLastModified = -1;

	File classFile;
	long classFileLastModified = -1;

	public Class<?> loadedClass;

	ClassResource() {
	}

	void free() {
		sourceFile = null;
		classFile = null;
		loadedClass = null;
	}

	public String toString() {
		return "ClassResource(classFile=" + classFile + ", sourceFile=" + sourceFile + ")";
	}

}
