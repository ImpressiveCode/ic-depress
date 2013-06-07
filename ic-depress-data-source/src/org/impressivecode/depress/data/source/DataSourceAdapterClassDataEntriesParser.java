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

package org.impressivecode.depress.data.source;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
/**
 * 
 * @author Marcin Strzeszyna
 * @author Wieslaw Rybicki
 * 
 */
public class DataSourceAdapterClassDataEntriesParser {

	public ArrayList<DataSourceAdapterClassDataEntry> parseEntries(String path) throws IOException{
		File chosenFile = new File(path);
		ArrayList<File> folders = DataSourceAdapterFileOperation.getAllFolder(chosenFile);
		ArrayList<File> classFile = DataSourceAdapterFileOperation.getAllClass(folders);
		ArrayList<String> classFileName = DataSourceAdapterFileOperation.getFormatedClassName(classFile, chosenFile);
		String urlPath = "file:\\".concat(chosenFile.getPath()).concat("\\");
		
		ArrayList<DataSourceAdapterClassDataEntry> output = new ArrayList<DataSourceAdapterClassDataEntry>();
		
		for(String temp : classFileName)
		{
			try
			{
				String s = "\\";
				URL url = new URL(urlPath);
				
				if ( temp.contains(s)){
					String classStr = temp.subSequence(temp.lastIndexOf(s) + 1, temp.length()).toString();
					String addPath = temp.subSequence(0, temp.lastIndexOf(s)).toString();
					temp = classStr;
					url = new URL(urlPath.concat(addPath.concat("\\")));
				}
				URLClassLoader pLoad = new URLClassLoader(new URL[]{url});
				System.out.println(url);
				Class<?> tempClass = pLoad.loadClass(temp);	
				dodajDoTabeliMetod(output, tempClass, url.toString());
			}
			catch(ClassNotFoundException e)
			{
				System.out.println("ClassNotFound"+e);
			}
			catch(MalformedURLException m)
			{
				System.out.println("BUNT2"+m);
			}
		}
		return output;
	}
	
	private void dodajDoTabeliMetod(ArrayList<DataSourceAdapterClassDataEntry> output, Class<?> cl, String path)
	{
		
		Method[] m = cl.getDeclaredMethods();
		for(Method temp : m)
		{
	        int modifier = temp.getModifiers();
			DataSourceAdapterClassDataEntry entity = new DataSourceAdapterClassDataEntry();
			entity.setLocation(path);
			entity.setClassName(cl.getName());
			entity.setMethodName(temp.getName());
			entity.setIsPublic(Modifier.isPublic(modifier));
			entity.setIsProtected(Modifier.isProtected(modifier));
			entity.setIsPrivate(Modifier.isPrivate(modifier));
			entity.setIsStatic(Modifier.isStatic(modifier));
			entity.setIsFinal(Modifier.isFinal(modifier));
			entity.setIsAbstract(Modifier.isAbstract(modifier));
			output.add(entity);
		}
	}

	
}
