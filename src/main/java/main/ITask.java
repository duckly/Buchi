/*
 * Written by Yong Li (liyong@ios.ac.cn)
 * This file is part of the Buchi.
 * 
 * Buchi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Buchi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Buchi. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package main;

/**
 * General Task Interface
 * */
public interface ITask {
	
	
	String getOperationName();
		
	long getRuntime();
	
	void runTask();
	
	void setRunningTime(long time);
	
	ResultValue getResultValue();
	
	void setResultValue(ResultValue resultValue);
	
	String toString();
	String toStringVerbose();

}
