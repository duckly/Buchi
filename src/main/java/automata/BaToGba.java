package automata;

import java.util.Collection;

import util.ISet;
import util.UtilISet;

/** an GBA wrapper for BA */
public class BaToGba implements IGba {

    public static final int ONE = 1;
    public static final int ZERO = 0;
    private final IBuchi mBuchi;
    
    public BaToGba(IBuchi buchi) {
        assert buchi != null;
        mBuchi = buchi;
    }

    @Override
    public int getAccSize() {
        return ONE;
    }

    @Override
    public ISet getAccSet(int state) {
        ISet acc = UtilISet.newISet();
        if(mBuchi.isFinal(state)) {
            acc.set(ZERO);
        }
        return acc;
    }

    @Override
    public boolean isFinal(int state, int index) {
        if(index < 0 || index > 0) return false;
        return mBuchi.isFinal(state);
    }

    @Override
    public int getStateSize() {
        return mBuchi.getStateSize();
    }

    @Override
    public int getAlphabetSize() {
        return mBuchi.getAlphabetSize();
    }

    @Override
    public IState addState() {
        return mBuchi.addState();
    }

    @Override
    public IState makeState(int id) {
        return mBuchi.makeState(id);
    }

    @Override
    public int addState(IState state) {
        return mBuchi.addState(state);
    }

    @Override
    public IState getState(int id) {
        return mBuchi.getState(id);
    }

    @Override
    public ISet getInitialStates() {
        return mBuchi.getInitialStates();
    }

    @Override
    public ISet getFinalStates() {
        return mBuchi.getFinalStates();
    }

    @Override
    public boolean isInitial(int id) {
        return mBuchi.isInitial(id);
    }

    @Override
    public void setInitial(int id) {
        mBuchi.setInitial(id);
    }

    @Override
    public Collection<IState> getStates() {
        return mBuchi.getStates();
    }

}
