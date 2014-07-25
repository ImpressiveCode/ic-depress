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
package org.impressivecode.depress.support.sourcecrawler;

import java.util.Hashtable;
import java.util.Iterator;

import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
/**
 * 
 * @author Maciej Borkowski, Capgemini Poland
 * 
 */
public class CrawlerOptionsParser {
    private boolean acceptPublic;
    private boolean acceptPrivate;
    private boolean acceptProtected;
    private boolean acceptPackage;
    private boolean acceptClass;
    private boolean acceptInterface;
    private boolean acceptAbstract;
    private boolean acceptEnum;
    private boolean acceptException;
    private boolean acceptInner;
    private boolean acceptTest;
    private boolean acceptFinal;
	private String acceptPackageName;

	public CrawlerOptionsParser(
			final Hashtable<String, SettingsModelBoolean> acceptSettings,
			final SettingsModelString packageSettings) {
		acceptPublic = acceptSettings.get(CrawlerAdapterNodeModel.PUBLIC).getBooleanValue();
		acceptPrivate = acceptSettings.get(CrawlerAdapterNodeModel.PRIVATE).getBooleanValue();
		acceptProtected = acceptSettings.get(CrawlerAdapterNodeModel.PROTECTED).getBooleanValue();
		acceptPackage = acceptSettings.get(CrawlerAdapterNodeModel.CLASS).getBooleanValue();
		acceptClass = acceptSettings.get(CrawlerAdapterNodeModel.PACKAGE).getBooleanValue();
		acceptInterface = acceptSettings.get(CrawlerAdapterNodeModel.INTERFACE).getBooleanValue();
		acceptAbstract = acceptSettings.get(CrawlerAdapterNodeModel.ABSTRACT).getBooleanValue();
		acceptEnum = acceptSettings.get(CrawlerAdapterNodeModel.ENUM).getBooleanValue();
		acceptException = acceptSettings.get(CrawlerAdapterNodeModel.EXCEPTION).getBooleanValue();
		acceptInner = acceptSettings.get(CrawlerAdapterNodeModel.INNER).getBooleanValue();
		acceptTest = acceptSettings.get(CrawlerAdapterNodeModel.TEST).getBooleanValue();
		acceptFinal = acceptSettings.get(CrawlerAdapterNodeModel.FINAL).getBooleanValue();
		
		acceptPackageName = packageSettings.getStringValue();
	}

	public void checkRequirements(SourceCrawlerOutput input){
    	for (Iterator<SourceFile> fileIterator =  input.getSourceFiles().iterator(); fileIterator.hasNext();){
    		SourceFile file = fileIterator.next();
    		if(!checkPackage(file.getSourcePackage())){
    			fileIterator.remove();
    			continue;
    		}
    	    for (Iterator<Clazz> clazzIterator = file.getClasses().iterator(); clazzIterator.hasNext();)
    			if(!checkClazz(clazzIterator.next())){
    				clazzIterator.remove();
    			}
    		if(file.getClasses().isEmpty()){
    			fileIterator.remove();
    		}
    	}
    }
    
    private boolean checkClazz(final Clazz clazz){
    	if(!checkAccess(clazz.getAccess())) return false;
    	if(!checkType(clazz.getType())) return false;
    	if(!acceptException && clazz.isException()) return false;
    	if(!acceptInner && clazz.isInner()) return false;
    	if(!acceptTest && clazz.isTest()) return false;
    	if(!acceptFinal && clazz.isFinal()) return false;
    	return true;
    }
   
    private boolean checkAccess(final String access){
    	if(access.equals(CrawlerAdapterNodeModel.PUBLIC) && acceptPublic) return true;
    	if(access.equals(CrawlerAdapterNodeModel.PRIVATE) && acceptPrivate) return true;
    	if(access.equals(CrawlerAdapterNodeModel.PROTECTED) && acceptProtected) return true;
    	if(access.equals(CrawlerAdapterNodeModel.PACKAGE) && acceptPackage) return true;
    	return false;
    }
    
    private boolean checkType(final String type){
    	if(type.equals(CrawlerAdapterNodeModel.CLASS) && acceptClass) return true;
    	if(type.equals(CrawlerAdapterNodeModel.INTERFACE) && acceptInterface) return true;
    	if(type.equals(CrawlerAdapterNodeModel.ABSTRACT) && acceptAbstract) return true;
    	if(type.equals(CrawlerAdapterNodeModel.ENUM) && acceptEnum) return true;
    	return false;
    }
    
    private boolean checkPackage(final String packageName) {
    	return packageName.contains(acceptPackageName);
    }
       
}
