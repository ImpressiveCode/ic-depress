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

import org.impressivecode.depress.datasource.DataSourceAdapterFileOperation;


public class DataSourceAdapterLoaderClass extends ClassLoader 
{
	public DataSourceAdapterLoaderClass(String pathToFile)
	{
		path = pathToFile;
	}
	
	protected Class<?> findClass(String name) throws ClassNotFoundException
	{
		byte[] byteTable = null;
		File file = new File(path);
		try
		{
			byteTable = DataSourceAdapterFileOperation.getBytesFromFile(file);
		}
		catch(IOException e)
		{
				System.out.println("cLadowaczKlas IO Exceptio"+e);
				throw new ClassNotFoundException(name);
		}
		System.out.println("Pierwsza przeszla"+byteTable.length);
		Class<?> zaladowanaKlasa = defineClass(name,byteTable,0,byteTable.length);
		if(zaladowanaKlasa==null) throw new ClassNotFoundException(name);
		return zaladowanaKlasa;
	}
	
	String path;
}
