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

package operation.convert.dpa;

import automata.DPA;
import automata.State;
import util.ISet;
import util.UtilISet;

/**
 * TODO
 * two states p and q are in the same equivalent class with respect to an even parity 2k
 * if and only 
 *  (1) succ(p, a) = succ(q, a) for every a and either
 *  (2) <i> parity(p) = parity(q) = 2k,
 *      <ii> parity(p) > 2k and parity(q) > 2k or
 *      <iii> parity(p) < 2k and parity(q) < 2k
 * */
public class StateNBA extends State {
    
    private final int mState;
    private final int mLabel;
    private final DPA2NBA mNBA;
    
    public StateNBA(DPA2NBA nba, int id, int state, int label) {
        super(id);
        this.mNBA = nba;
        this.mState = state;
        this.mLabel = label;
    }
    
    public int getState() {
        return mState;
    }
    
    public int getLabel() {
        return mLabel;
    }
    
    private ISet mVisitedLetters = UtilISet.newISet();
    
    @Override
    public ISet getSuccessors(int letter) {
        if(mVisitedLetters.get(letter)) {
            return super.getSuccessors(letter);
        }
        mVisitedLetters.set(letter);
        // check whether current state has even parity
        DPA dpa = mNBA.getOperand();
        // original parity
        StateNBA bsucc = null;
        ISet bsuccs = UtilISet.newISet();
        
        int psucc = dpa.getState(mState).getSuccessor(letter);
        int succParity = dpa.getAcceptance().getColor(psucc);
        
        
        if(mLabel < 0) {
            //1. add successor state from the original DPA in the copy DPA
            bsucc = mNBA.getOrAddState(psucc, -1);
            System.out.println(toString() + " -> " + bsucc + " : " + letter);
            super.addSuccessor(letter, bsucc.getId());
            bsuccs.set(bsucc.getId());
            
            //2. check if we have to add a copy for successors with even parities
            if(succParity % 2 == 0) {
                bsucc = mNBA.getOrAddState(psucc, succParity);
                super.addSuccessor(letter, bsucc.getId());
                bsuccs.set(bsucc.getId());
                System.out.println(toString() + " -> " + bsucc + " : " + letter);
            }
        }else {
            //3. if currently the parity of successor is larger than the label
            if(succParity >= mLabel) {
                bsucc = mNBA.getOrAddState(psucc, mLabel);
                System.out.println(toString() + " -> " + bsucc + " : " + letter);
                super.addSuccessor(letter, bsucc.getId());
                bsuccs.set(bsucc.getId());
            }
        }
        
        return bsuccs;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(this == obj) return true;
        if(obj instanceof StateNBA) {
            StateNBA other = (StateNBA)obj;
            return this.mState == other.mState
               &&  this.mLabel == other.mLabel;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mState;
        result = prime * result + mLabel;
        return result;
    }
    
    @Override
    public String toString() {
        return "s" + mState + (mLabel >= 0 ? (":" + mLabel) : "");
    }
    

}
