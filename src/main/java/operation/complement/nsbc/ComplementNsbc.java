package operation.complement.nsbc;

import automata.Buchi;
import automata.IBuchi;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import main.Options;
import operation.complement.Complement;
import operation.explore.Explore;
import operation.removal.Remove;
import util.ISet;
import util.UtilISet;

public class ComplementNsbc extends Complement {

    protected final ISet mDetStates;
    protected final ISet mNondetStates;
    
    public ComplementNsbc(IBuchi operand) {
        super(operand);
        this.mDetStates = operand.getDetStatesAfterFinals();
        this.mNondetStates = UtilISet.newISet();
        for(int s = 0; s < operand.getStateSize(); s ++) {
            if(!this.mDetStates.get(s)) {
                this.mNondetStates.set(s);
            }
        }
    }
    
    private TObjectIntMap<StateNsbc> mStateIndices;
    
    @Override
    protected void computeInitialStates() {
        // compute initial states
        mStateIndices = new TObjectIntHashMap<>();
        ISet inits = mOperand.getInitialStates().clone();
        NSBC nsbc = new NSBC(inits);
        StateNsbc stateSlice = getOrAddState(nsbc);
        this.setInitial(stateSlice.getId());
    }
    
    protected StateNsbc getStateNsbc(int id) {
        return (StateNsbc)getState(id);
    }

    protected StateNsbc getOrAddState(NSBC nsbc) {
        StateNsbc state = new StateNsbc(this, 0, nsbc);
        if(mStateIndices.containsKey(state)) {
            return getStateNsbc(mStateIndices.get(state));
        }else {
            int index = getStateSize();
            StateNsbc newState = new StateNsbc(this, index, nsbc);
            int id = this.addState(newState);
            mStateIndices.put(newState, id);
            if(nsbc.isFinal()) setFinal(index);
            return newState;
        }
    }

    @Override
    public String getName() {
        return "ComplementSliceSDBA";
    }
    
    
    public static void main(String[] args) {
        IBuchi buchi = new Buchi(2);
        
        buchi.addState();
        buchi.addState();
        buchi.addState();
        
        buchi.getState(0).addSuccessor(0, 0);
        buchi.getState(0).addSuccessor(1, 0);
        buchi.getState(0).addSuccessor(0, 1);
        buchi.getState(0).addSuccessor(1, 1);
        
        buchi.getState(1).addSuccessor(0, 2);
        buchi.getState(1).addSuccessor(1, 1);
        
        buchi.getState(2).addSuccessor(0, 2);
        buchi.getState(2).addSuccessor(1, 2);
        
        buchi.setFinal(1);
        buchi.setInitial(0);
        
        System.out.println(buchi.toDot());
        System.out.println(buchi.getDetStatesAfterFinals());
        Options.mEnhancedSliceGuess = true;
        ComplementNsbc complement = new ComplementNsbc(buchi);
        new Explore(complement);
        System.out.println(complement.toDot());
        
        System.out.println(complement.toBA());
        
        IBuchi result = (new Remove(complement)).getResult();
        
        System.out.println(result.toDot());
        
        System.out.println(result.toBA());
        

    }

}
