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
package org.impressivecode.depress.scm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author Maciej Borkowski, Capgemini Poland
*/
public class SCMExtensionsParser {

	public static ArrayList<String> parseExtensions(final String extensions){
  		String[] ext = extensions.split("\\s*,\\s*");
  		ArrayList<String> arr = new ArrayList<String>();
  		for(String word : ext){
  			if(word.equals("*")){
  				arr = new ArrayList<String>();
  				arr.add("*");
  				return arr;
  			}
  			arr.add(word);
  		}
  		return arr;
  	}
	
	public static boolean extensionFits(final String str, final List<String> extensions){
		for (String extension : extensions) {
			int idx = str.lastIndexOf(".");
			if(idx == -1 && extension.equals(""))
				return true;
			if(idx == -1 || extension.equals(""))
				continue;
			if((extension.equals("*") || str.endsWith(extension)))
				return true;
			
    		Pattern pattern = Pattern.compile(extension, Pattern.CASE_INSENSITIVE);
    	    Matcher matcher = pattern.matcher(str.substring(idx));

    		if(matcher.matches())
    			return 	true;
    	}
		return false;
	}

}