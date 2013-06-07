package org.impressivecode.depress.metric.datasource;
import java.io.*; 

import org.impressivecode.depress.metric.datasource.DataSourceAdapter_FileOperation;


public class DataSourceAdapter_LoaderClass extends ClassLoader 
{
	public DataSourceAdapter_LoaderClass(String pathToFile)
	{
		path = pathToFile;
	}
	
	protected Class<?> findClass(String name) throws ClassNotFoundException
	{
		byte[] byteTable = null;
		File file = new File(path);
		try
		{
			byteTable = DataSourceAdapter_FileOperation.getBytesFromFile(file);
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
