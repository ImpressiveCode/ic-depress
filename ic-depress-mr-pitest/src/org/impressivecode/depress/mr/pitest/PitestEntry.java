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

package org.impressivecode.depress.mr.pitest;

/**
 * 
 * @author Zuzanna Pacholczyk, Capgemini Poland
 * 
 **/

public class PitestEntry {
		private boolean detection;
		private String mutationStatus;
		private String sourceFile;
		private String mutatedClass;
		private String mutatedMethod;
		private String methodDescription;
		private int lineNumber;
		private String mutator;
		private int index;
		private String killingTest;
	    @Override
	    public String toString() {
	        return String
	                .format("PitestEntry [mutationStatus=%s, detection=%s, sourceFile=%s, mutatedClass=%s, mutatedMethod=%s, methodDescription=%s, lineNumber=%s, mutator=%s, index=%s, killingTest=%s]",
	                        mutationStatus, detection, sourceFile, mutatedClass, mutatedMethod, methodDescription, lineNumber, mutator, index, killingTest);
	    }

	    
	    public void setMutationStatus(String textContent){
	    	this.mutationStatus = textContent;
	    }
	    
	    public void setDetection(boolean textContent){
	    	this.detection = textContent;
	    }
	    
		public void setSourceFile(String textContent) {
			this.sourceFile = textContent;
			
		}

		public void setMutatedClass(String textContent) {
			this.mutatedClass = textContent;
			
		}

		public void setMutatedMethod(String textContent) {
			this.mutatedMethod = textContent;
			
		}

		public void setMethodDescription(String textContent) {
			this.methodDescription = textContent;
			
		}

		public void setLineNumber(int textContent) {
			this.lineNumber = textContent;
			
		}

		public void setMutator(String textContent) {
			this.mutator = textContent;
			
		}

		public void setIndex(int i) {
			this.index = i;
			
		}

		public void setKillingTest(String textContent) {
			this.killingTest = textContent;
			
		}
		
		public String getMutationStatus(){
			return mutationStatus;
		}
		
		public boolean getDetection(){
			return detection;
		}

		public String getSourceFile() {
			return sourceFile;
			
		}

		public String getMutatedClass() {
			return mutatedClass;
			
		}

		public String getMutatedMethod() {
			return mutatedMethod;
			
		}

		public String getMethodDescription() {
			return methodDescription;
			
		}

		public int getLineNumber() {
			return lineNumber;
			
		}

		public String getMutator() {
			return mutator;
			
		}

		public int getIndex() {
			return index;
			
		}

		public String getKillingTest() {
			return killingTest;
			
		}
}
