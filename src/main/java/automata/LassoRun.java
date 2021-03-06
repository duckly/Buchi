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

package automata;

public class LassoRun {
    
    private final Run mStem;
    private final Run mLoop;
    
    public LassoRun(Run stem, Run loop) {
        mStem = stem;
        mLoop = loop;
    }
    
    public Run getStem() {
        return mStem;
    }
    
    public Run getLoop() {
        return mLoop;
    }
    
    @Override
    public String toString() {
        return "(" + mStem + ", " + mLoop + ")";
    }
    

}
