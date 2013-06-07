package org.impressivecode.depress.metric.datasource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class ClassDataEntriesParser {

	public ArrayList<ClassDataEntry> parseEntries(String path) throws IOException{
		File chosenFile = new File(path);
		ArrayList<File> folders = DataSourceAdapter_FileOperation.getAllFolder(chosenFile);
		ArrayList<File> classFile = DataSourceAdapter_FileOperation.getAllClass(folders);
		ArrayList<String> classFileName = DataSourceAdapter_FileOperation.getFormatedClassName(classFile, chosenFile);
		String urlPath = "file:\\".concat(chosenFile.getPath()).concat("\\");
		
		ArrayList<ClassDataEntry> output = new ArrayList<ClassDataEntry>();
		
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
	
	private void dodajDoTabeliMetod(ArrayList<ClassDataEntry> output, Class<?> cl, String path)
	{
		
		Method[] m = cl.getDeclaredMethods();
		for(Method temp : m)
		{
	        int modifier = temp.getModifiers();
			ClassDataEntry entity = new ClassDataEntry();
			entity.setClassName(cl.getName());
			entity.setMethodName(temp.getName());
			entity.setIsPublic(Modifier.isPublic(modifier));
			entity.setIsProtected(Modifier.isProtected(modifier));
			entity.setIsPrivate(Modifier.isPrivate(modifier));
			entity.setIsStatic(Modifier.isStatic(modifier));
			entity.setIsFinal(Modifier.isFinal(modifier));
			entity.setIsAbstract(Modifier.isAbstract(modifier));
			entity.setLocation(path);
			output.add(entity);
		}
	}

	
}
