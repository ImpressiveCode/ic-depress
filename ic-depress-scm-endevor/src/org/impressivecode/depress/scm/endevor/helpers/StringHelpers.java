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
package org.impressivecode.depress.scm.endevor.helpers;

import java.util.LinkedList;

import org.impressivecode.depress.scm.endevor.EndevorLogParserException;

/**
 * 
 * @author Alicja Bodys, Wroc³aw University of Technology
 * @author Mateusz Leonowicz, Wroc³aw University of Technology
 * @author Piotr Sas, Wroc³aw University of Technology
 * @author Maciej Sznurowski, Wroc³aw University of Technology
 * 
 */
public class StringHelpers {
	
	public static String[] applyMask(String inputText, String applyMask) throws EndevorLogParserException {
		if (inputText != null && applyMask != null &&  inputText.length() == applyMask.length() && applyMask.contains("-")) {
			LinkedList<String> resultsList = new LinkedList<>();
			
			StringBuilder currentCell = new StringBuilder();
			
			for (int i = 0; i < applyMask.length(); i++) {
				if (applyMask.charAt(i) == '-') {
					currentCell.append(inputText.charAt(i));
				}
				else {
					if (currentCell.length() > 0) {
						resultsList.add(currentCell.toString());
						currentCell = new StringBuilder();
					}
				}
			}
			
			if (currentCell.length() > 0) {
				resultsList.add(currentCell.toString());
			}
			
			String[] toReturn = new String[resultsList.size()];
			return resultsList.toArray(toReturn);
		}
		else {
			throw new EndevorLogParserException(String.format("\nUnable to parse a SOURCE LEVEL INFORMATION row with its content (attached below).\n" +
					"This row line content had no proper dash row above or its lentgh is different than the dash row or the dash row does not exist at all.\n" +
					"The dash row length has to be the same as the row length and the contents of the row should be below its corresponding dashes. The dash row " +
					"and the content row are:\n%s\n%s\nPlease verify the log file and try again.",
					(applyMask == null ? "<EMPTY>" : applyMask), (inputText == null ? "<EMPTY>" : inputText)));
		}
	}
}
