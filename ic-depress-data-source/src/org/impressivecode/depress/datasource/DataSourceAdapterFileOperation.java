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

package org.impressivecode.depress.datasource;

import java.io.*;
import java.util.*;


public class DataSourceAdapterFileOperation 
{
	static public ArrayList<File> getAllFolder(File f)
	{
		ArrayList<File> score = new ArrayList<File>();
		score.add(f);
		int iterator = 0;
		for(int i = iterator;i<score.size();i++)
		{
			File[] fileList = score.get(iterator).listFiles();
			if(fileList!=null)
			{
				for(File temp : fileList)
				{
					if(temp.isDirectory())
					{
						score.add(temp);
					}
				}
			}
			iterator++;
		}
		return score;
	}
	
	static public ArrayList<File> getAllClass(ArrayList<File> file)
	{
		ArrayList<File> wynik = new ArrayList<File>();
		for(File temp : file)
		{
			File[] fileTable = temp.listFiles(new FileFilter()
					{
						public boolean accept(File dir)
						{
							String name = dir.getName();
							if(name.endsWith(".class"))
							{
								return true;
							}
							else
							{
								return false;
							}
						}
					});
			for(File tempp : fileTable)
			{
				wynik.add(tempp);
			}
		}
		return wynik;
	}
	
	public static byte[] getBytesFromFile(File file) throws IOException 
	{
	    InputStream in = new FileInputStream(file);
	    long length = file.length();

	    if (length > Integer.MAX_VALUE) 
	    {
	       System.out.println("file is to big");
	    }
	    byte[] bytes = new byte[(int)length];
	    int byt;
	    int i = 0;
	    while((byt = in.read())!=-1)
	    {
	    	bytes[i] = (byte) byt;
	    }
	    in.close();
	    return bytes;
	}
	
	static public ArrayList<String> getFormatedClassName(ArrayList<File> file,File parent)
	{
		String pathParent = parent.getPath();
		ArrayList<String> wynik = new ArrayList<String>();
		int parentLength = pathParent.length()+1;
		for(File temp : file)
		{
			String filePath = temp.getPath();
		    filePath = filePath.substring(parentLength,filePath.length()-6);
		    wynik.add(filePath);
		}
		return wynik;
	}
}
