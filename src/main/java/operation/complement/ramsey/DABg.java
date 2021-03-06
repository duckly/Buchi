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

package operation.complement.ramsey;

import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import automata.Buchi;
import automata.DOA;
import automata.IAcc;
import automata.IBuchi;
import automata.IS;
import automata.StateDA;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import operation.explore.UtilExplore;
import util.ISet;
import util.UtilISet;


/**
 * deterministic automaton for Buchi automata to partition words in \Sigma^+ 
 * <br>
 * Automaton name B<sub>g</sub> is taken from paper "BÜCHI COMPLEMENTATION AND SIZE-CHANGE TERMINATION"
 * */

public class DABg extends DOA {

    private final TObjectIntMap<StateDABg> mStateIndices;
    private final IBuchi mOperand;
    
    
    public DABg(IBuchi operand) {
        super(operand.getAlphabetSize());
        this.mOperand = operand;
        this.mStateIndices = new TObjectIntHashMap<>();
        computeInitialState();
    }
    
    protected IBuchi getOperand() {
        return this.mOperand;
    }
    
    protected void computeInitialState() {
        Profile profile = new Profile(mOperand);
        StateDABg init = getOrAddState(profile);
        super.setInitial(init.getId());
    }

    @Override
    public IAcc getAcceptance() {
        return null;
    }
    
    public StateDABg getStateDAProfile(int id) {
        return (StateDABg) super.getState(id);
    }
    
    public StateDABg getOrAddState(Profile profile) {
        StateDABg state = new StateDABg(this, 0, profile);
        if(mStateIndices.containsKey(state)) {
            return getStateDAProfile(mStateIndices.get(state));
        }else {
            int index = getStateSize();
            state = new StateDABg(this, index, profile);
            int id = addState(state);
            mStateIndices.put(state, id);
            return state;
        }
    }
    
    /**
     * check whether the language Y<sub>ij</sub> is disjoint with the language of input
     * NBA where Y<sub>ij</sub> = X<sub>j</sub>(X<sub>j</sub>)<sup>w</sup> and X<sub>i</sub> = L(D)
     * defined in "BÜCHI COMPLEMENTATION AND SIZE-CHANGE TERMINATION"
     * in Lemma 2.7 by SETH FOGARTY a AND MOSHE Y. VARDI in LMCS 2012 
     ***/
    public boolean isDisjointWith(int i, int j) {
        return getStateDAProfile(i).isDisjointWith(getStateDAProfile(j));
    }
    
    /**
     * check whether the language Y<sub>ij</sub> is proper:
     * <br>
     *   X<sub>i</sub>(X<sub>j</sub>) <= X<sub>i</sub> and
     * <br>  
     *   X<sub>j</sub>(X<sub>j</sub>) <= X<sub>j</sub>
     * <br>
     *  where Y<sub>ij</sub> = X<sub>j</sub>(X<sub>j</sub>)<sup>w</sup> and X<sub>i</sub> = L(D)
     *  This definition is defined in "BÜCHI COMPLEMENTATION AND SIZE-CHANGE TERMINATION"
     *  before Lemma 2.5 by SETH FOGARTY a AND MOSHE Y. VARDI in LMCS 2012
     ***/
    protected boolean isProper(int i, int j) {
        return getStateDAProfile(i).isProper(getStateDAProfile(j));
    }
    
    @Override
    public void toDot(PrintStream out, List<String> alphabet) {
        // output automata in dot
        out.print("digraph {\n");
        Collection<IS> states = getStates();
        for (IS state : states) {
            StateDA st = (StateDA)state;
            out.print("  " + state.getId() + " [label=\"s" + state.getId() 
            + " : " + state + "\" , shape = ");
            out.print("circle");

            out.print("];\n");
            st.toDot(out, alphabet);
        }
        
        out.print("  " + states.size() + " [label=\"\", shape = plaintext];\n");
        out.print("  " + states.size() + " -> " + this.getInitialState() + " [label=\"\"];\n");
        out.print("}\n\n");
    }
    
    public static void main(String[] args) {
        IBuchi buchi = new Buchi(2);
        buchi.addState();
        buchi.addState();
        
        buchi.getState(0).addSuccessor(0, 0);
        buchi.getState(0).addSuccessor(1, 0);
        buchi.getState(0).addSuccessor(1, 1);
        
        buchi.getState(1).addSuccessor(0, 0);
        buchi.getState(1).addSuccessor(0, 1);
        
        buchi.setFinal(1);
        buchi.setInitial(0);
        
        DABg po = new DABg(buchi);
        
        LinkedList<StateDA> walkList = new LinkedList<>();
        walkList.add(po.getState(po.getInitialState()));

        ISet visited = UtilISet.newISet();

        while (!walkList.isEmpty()) {
            StateDA s = walkList.remove();
            if (visited.get(s.getId()))
                continue;
            visited.set(s.getId());

            for (int letter = 0; letter < po.getAlphabetSize(); letter++) {
                int succ = s.getSuccessor(letter);
                if (!visited.get(succ)) {
                    walkList.addFirst(po.getState(succ));
                }
            }
        }
        System.out.println(po.toDot());
        
        buchi = new Buchi(2);
        buchi.addState();
        buchi.addState();
        buchi.addState();
        buchi.addState();
        
        buchi.getState(0).addSuccessor(0, 0);
        buchi.getState(0).addSuccessor(1, 0);
        buchi.getState(0).addSuccessor(1, 1);
        
        buchi.getState(1).addSuccessor(0, 2);
        buchi.getState(1).addSuccessor(1, 1);
        
        buchi.getState(2).addSuccessor(0, 3);
        buchi.getState(2).addSuccessor(1, 2);
        
        buchi.getState(3).addSuccessor(0, 3);
        buchi.getState(3).addSuccessor(1, 1);
        
        buchi.setFinal(3);
        buchi.setInitial(0);
        
        po = new DABg(buchi);
        UtilExplore.explore(po);
        System.out.println(po.toDot());
        
    }

}
