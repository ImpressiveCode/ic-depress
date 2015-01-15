package org.impressivecode.depress.scm.endevor.helpers;

import java.util.LinkedList;

import org.impressivecode.depress.scm.endevor.EndevorLogParserException;

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
