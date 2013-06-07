package org.impressivecode.depress.metric.datasource;

import java.io.*;
import java.util.*;


public class DataSourceAdapter_FileOperation 
{
	static public ArrayList<File> getAllFolder(File f)
	{
		ArrayList<File> wynik = new ArrayList<File>();
		wynik.add(f);
		int iterator = 0;
		for(int i = iterator;i<wynik.size();i++)
		{
			File[] fileList = wynik.get(iterator).listFiles();
			if(fileList!=null)
			{
				for(File temp : fileList)
				{
					if(temp.isDirectory())
					{
						wynik.add(temp);
					}
				}
			}
			iterator++;
		}
		return wynik;
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
	       System.out.println("Plik jest za duzy");
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
		    //filePath = filePath.replace("\\",".");
		    wynik.add(filePath);
		}
		return wynik;
	}
}
