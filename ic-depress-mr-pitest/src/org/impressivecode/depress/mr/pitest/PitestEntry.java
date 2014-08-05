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
		private String mutatedClass;
		private double mutationScoreIndicator;

	    @Override
	    public String toString() {
	        return String
	                .format("PitestEntry [detection=%s, mutatedClass=%s]",
	                        detection, mutatedClass);
	    }

	    
	    public void setDetection(boolean textContent){
	    	this.detection = textContent;
	    }
	    
	    public void setMutationScoreIndicator(double textContent){
	    	this.mutationScoreIndicator = textContent;
	    }
	    
		public void setMutatedClass(String textContent) {
			this.mutatedClass = textContent;
			
		}

		public boolean getDetection(){
			return detection;
		}
		
		public double getMutationScoreIndicator(){
			return mutationScoreIndicator;
		}

		public String getMutatedClass() {
			return mutatedClass;
			
		}

}
