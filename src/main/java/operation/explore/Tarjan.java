package operation.explore;

import java.util.Stack;
import automata.IGba;
import automata.IGbaState;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import util.ISet;
import util.UtilISet;

public class Tarjan {
    private int mIndex;
    private final Stack<Integer> mStack;
    private final TIntIntMap mIndexMap ;
    private final TIntIntMap mLowlinkMap;
    private final IGba mOperand;
    private boolean mIsEmpty = true;
    
    public Tarjan(IGba gba) {
        this.mOperand = gba;
        this.mStack = new Stack<>();
        this.mLowlinkMap = new TIntIntHashMap();
        this.mIndexMap   = new TIntIntHashMap();
        explore();
    }

    private void explore() {
        // TODO Auto-generated method stub
        mIndex = 0;
        for(final int n : mOperand.getInitialStates()) {
            if(! mIndexMap.containsKey(n) ){
                strongConnect(n);
                if(mIsEmpty == false) return;
            }
        }
    }
    
    public boolean isEmpty() {
        return mIsEmpty;
    }

    private void strongConnect(int v) {
        // TODO Auto-generated method stub
        mStack.push(v);
        mIndexMap.put(v, mIndex);
        mLowlinkMap.put(v, mIndex);
        ++ mIndex;          
        
        IGbaState state = (IGbaState) mOperand.getState(v);
        for(int letter = 0; letter < mOperand.getAlphabetSize(); letter ++) {
            for(int succ : state.getSuccessors(letter)) {
                if(! mIndexMap.containsKey(succ)) {
                    strongConnect(succ);
                    if(mIsEmpty == false) return;
                    mLowlinkMap.put(v, Math.min(mLowlinkMap.get(v), mLowlinkMap.get(succ)));                    
                }else if(mStack.contains(succ)) {
                    mLowlinkMap.put(v, Math.min(mLowlinkMap.get(v), mIndexMap.get(succ)));                  
                }
            }
        }
        
        if(mLowlinkMap.get(v) == mIndexMap.get(v)){
            ISet scc = UtilISet.newISet();
            ISet list = UtilISet.newISet();
            while(! mStack.empty()){
                int t = mStack.pop();
                scc.or(mOperand.getAccSet(t));
                list.set(t);
                if(t == v)
                    break;
            }
            boolean acc = scc.cardinality() == mOperand.getAccSize();
            if(acc && list.cardinality() > 1) {
                mIsEmpty = false;
            }else if(acc){
                boolean hasSelfLoop = false;
                for(int letter : state.getEnabledLetters()) {
                    if(state.getSuccessors(letter).get(v)) hasSelfLoop = true;
                }
                if(hasSelfLoop) mIsEmpty = false;
            }
            System.out.println("scc: " + list + " isEmpty: " + mIsEmpty);
        }
    }
}