package operation.determinize.sdba;

import java.util.ArrayList;
import java.util.List;

import automata.Buchi;
import automata.IBuchi;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import operation.IUnaryOp;
import operation.determinize.Determinize;
import operation.explore.Explore;
import util.ISet;
import util.UtilISet;

public class DeterminizeSDBA extends Buchi implements IUnaryOp<IBuchi, IBuchi> {

    private final IBuchi mOperand;
    private final TObjectIntMap<StateDet> mStateIndices = new TObjectIntHashMap<>();

    public DeterminizeSDBA(IBuchi operand) {
        super(operand.getAlphabetSize());
        this.mOperand = operand;
        computeInitialStates();
    }
    
    protected void computeInitialStates() {
        ISet D = mOperand.getInitialStates().clone();
        D.and(mOperand.getFinalStates()); // goto C
        ISet N = mOperand.getInitialStates().clone();
        N.andNot(D);
        // we have to get the indexed
        int label = 0;
        ParallelRuns runs = new ParallelRuns(N);
        for(int s : D) {
            runs.addLabel(s, label);
            label ++;
        }
        StateDet init = getOrAddState(runs);
        this.setInitial(init.getId());
    }

    @Override
    public String getName() {
        return "Determinize";
    }

    @Override
    public IBuchi getOperand() {
        return mOperand;
    }

    @Override
    public IBuchi getResult() {
        return this;
    }

    public StateDet getStateDet(int id) {
        return (StateDet) getState(id);
    }

    protected StateDet getOrAddState(ParallelRuns ndb) {

        StateDet state = new StateDet(this, 0, ndb);

        if (mStateIndices.containsKey(state)) {
            return getStateDet(mStateIndices.get(state));
        } else {
            int index = getStateSize();
            StateDet newState = new StateDet(this, index, ndb);
            int id = this.addState(newState);
            mStateIndices.put(newState, id);
//            if (ndb.getBSet().overlap(mOperand.getFinalStates()))
//                setFinal(index);
            return newState;
        }
    }
    
    public static void main(String[] args) {
        IBuchi buchi = new Buchi(2);
        int fst = 0, snd = 1, thd = 2, fur = 3, fiv = 4;
        
        buchi.addState();
        buchi.addState();
        buchi.addState();
        buchi.addState();
        buchi.addState();
        
        // 
        buchi.setInitial(fst);
        buchi.setFinal(snd);
        buchi.setFinal(fiv);
        
        // 
        buchi.getState(fst).addSuccessor(0, fst);
        buchi.getState(fst).addSuccessor(1, fst);
        
        buchi.getState(fst).addSuccessor(1, snd);
        buchi.getState(fst).addSuccessor(1, fur);
        
        buchi.getState(snd).addSuccessor(0, thd);
        buchi.getState(snd).addSuccessor(1, snd);
        
        buchi.getState(thd).addSuccessor(0, thd);
        buchi.getState(thd).addSuccessor(1, fiv);
        
        buchi.getState(fur).addSuccessor(0, fst);
        buchi.getState(fur).addSuccessor(0, fiv);
        
        buchi.getState(fiv).addSuccessor(0, fiv);
        
        // 
        System.out.println(buchi.toDot());
        
        DeterminizeSDBA deted = new DeterminizeSDBA(buchi);
        new Explore(deted);
        
        System.out.println(deted.toDot());
        
        Buchi b2 = new Buchi(2);
        b2.addState();
        b2.addState();
        
        b2.setInitial(0);
        b2.setFinal(1);
        
        // 
        b2.getState(0).addSuccessor(0, 0);
        b2.getState(0).addSuccessor(1, 0);
        b2.getState(0).addSuccessor(1, 1);
        
        b2.getState(1).addSuccessor(0, 0);
        b2.getState(1).addSuccessor(0, 1);
        
        System.out.println(b2.toDot());
        
        deted = new DeterminizeSDBA(b2);
        new Explore(deted);
        
        System.out.println(deted.toDot());
    }

}
