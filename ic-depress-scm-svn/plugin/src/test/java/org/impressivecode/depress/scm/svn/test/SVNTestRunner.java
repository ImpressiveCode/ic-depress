package org.impressivecode.depress.scm.svn.test;

import org.impressivecode.depress.scm.svn.test.structural.SVNLogLoaderTest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/*
ImpressiveCode Depress Framework
Copyright (C) 2013  ImpressiveCode contributors

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
public class SVNTestRunner {

	private static void end(Class<?> inClass) {
		System.out.println(inClass.getName() + " End");
	}

	public static void main(String[] args) {

		start(SVNTestRunner.class);

		runTestClass(SVNLogLoaderTest.class);

		end(SVNTestRunner.class);
	}

	private static void runTestClass(Class<?> inTestClass) {

		start(inTestClass);

		Result result = JUnitCore.runClasses(inTestClass);

		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}

		end(inTestClass);
	}

	private static void start(Class<?> inClass) {
		System.out.println(inClass.getName() + " Start");
	}
}
