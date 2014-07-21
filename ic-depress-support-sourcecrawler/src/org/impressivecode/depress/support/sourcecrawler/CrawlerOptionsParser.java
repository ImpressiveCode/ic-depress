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

import java.util.Iterator;
/**
 * 
 * @author Maciej Borkowski, Capgemini Poland
 * 
 */
public class CrawlerOptionsParser {
	
	static final String PUBLIC = "Public";
    static final String PRIVATE = "Private";
    static final String PROTECTED = "Protected";
    static final String PACKAGE = "Package-private";
    static final String CLASS = "Class";
    static final String INTERFACE = "Interface";
    static final String ABSTRACT = "Abstract";
    static final String ENUM = "Enum";
    static final String EXCEPTION = "Exception";
    static final String INNER = "Inner";
    static final String TEST = "Test";
    static final String FINAL = "Final"; 
    
    private boolean booleanPublic;
    private boolean booleanPrivate;
    private boolean booleanProtected;
    private boolean booleanPackage;
    private boolean booleanClass;
    private boolean booleanInterface;
    private boolean booleanAbstract;
    private boolean booleanEnum;
    private boolean booleanException;
    private boolean booleanInner;
    private boolean booleanTest;
    private boolean booleanFinal;
    
    public CrawlerOptionsParser(boolean booleanPublic, boolean booleanPrivate,
			boolean booleanProtected, boolean booleanPackage,
			boolean booleanClass, boolean booleanInterface,
			boolean booleanAbstract, boolean booleanEnum,
			boolean booleanException, boolean booleanInner,
			boolean booleanTest, boolean booleanFinal) {
		super();
		
		this.booleanPublic = booleanPublic;
		this.booleanPrivate = booleanPrivate;
		this.booleanProtected = booleanProtected;
		this.booleanPackage = booleanPackage;
		this.booleanClass = booleanClass;
		this.booleanInterface = booleanInterface;
		this.booleanAbstract = booleanAbstract;
		this.booleanEnum = booleanEnum;
		this.booleanException = booleanException;
		this.booleanInner = booleanInner;
		this.booleanTest = booleanTest;
		this.booleanFinal = booleanFinal;
	}

    public SourceCrawlerOutput checkRequirements(SourceCrawlerOutput input){
    	for (Iterator<SourceFile> fileIterator =  input.getSourceFiles().iterator(); fileIterator.hasNext(); ){
    		SourceFile file = fileIterator.next();
    	    for (Iterator<Clazz> clazzIterator = file.getClasses().iterator(); clazzIterator.hasNext(); )
    			if(!checkClazz(clazzIterator.next())){
    				clazzIterator.remove();
    			}
    		if(file.getClasses().isEmpty()){
    			fileIterator.remove();
    		}
    	}
    	return input;
    }
    
    private boolean checkClazz(Clazz clazz){
    	if(!checkAccess(clazz.getAccess())) return false;
    	if(!checkType(clazz.getType())) return false;
    	if(!booleanException && clazz.isException()) return false;
    	if(!booleanInner && clazz.isInner()) return false;
    	if(!booleanTest && clazz.isTest()) return false;
    	if(!booleanFinal && clazz.isFinal()) return false;
    	return true;
    }
   
    private boolean checkAccess(String access){
    	if(access.equals(PUBLIC) && booleanPublic) return true;
    	if(access.equals(PRIVATE) && booleanPrivate) return true;
    	if(access.equals(PROTECTED) && booleanProtected) return true;
    	if(access.equals(PACKAGE) && booleanPackage) return true;
    	return false;
    }
    
    private boolean checkType(String type){
    	if(type.equals(CLASS) && booleanClass) return true;
    	if(type.equals(INTERFACE) && booleanInterface) return true;
    	if(type.equals(ABSTRACT) && booleanAbstract) return true;
    	if(type.equals(ENUM) && booleanEnum) return true;
    	return false;
    }
       
}
