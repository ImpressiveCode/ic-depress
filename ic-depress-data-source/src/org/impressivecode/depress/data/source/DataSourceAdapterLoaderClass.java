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
/**
 * 
 * @author Marcin Strzeszyna
 * @author Wieslaw Rybicki
 * 
 */
import java.io.*; 
import org.impressivecode.depress.data.source.DataSourceAdapterFileOperation;

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
				System.out.println("Loadedclass IO Exceptio"+e);
				throw new ClassNotFoundException(name);
		}
		System.out.println("First go"+byteTable.length);
		Class<?> Loadedclass = defineClass(name,byteTable,0,byteTable.length);
		if(Loadedclass==null) throw new ClassNotFoundException(name);
		return Loadedclass;
	}
	
	String path;
}
