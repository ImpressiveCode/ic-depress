package org.impressivecode.depress.scm.endevor.helpers;

import java.util.LinkedList;

import org.impressivecode.depress.scm.endevor.EndevorLogParserException;

public class StringHelpers {
	
	public static String[] applyMask(String inputText, String applyMask) throws EndevorLogParserException {
		if (inputText.length() == applyMask.length() && applyMask.contains("-")) {
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
			
			String[] toReturn = new String[resultsList.size()];
			return resultsList.toArray(toReturn);
		}
		else {
			throw new EndevorLogParserException("StringHelper.applyMask: input string lentgh is different than applyMask or applyMask doesn't contain any dashes.");
		}
	}
}
