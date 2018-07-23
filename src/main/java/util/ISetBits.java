package util;

import java.util.BitSet;
import java.util.Iterator;

public class ISetBits implements ISet {
	
	private BitSet mSet;
	
	public ISetBits() {
		mSet = new BitSet();
	}

	@Override
	public ISet clone() {
		ISetBits bits = new ISetBits();
		bits.mSet = (BitSet) mSet.clone();
		return bits;
	}

	@Override
	public void andNot(ISet set) {
		if(! (set instanceof ISetBits)) {
		    throw new UnsupportedOperationException("OPERAND should be BitSet");
		}
		BitSet bits = (BitSet) set.get();
		this.mSet.andNot(bits);
	}

	@Override
	public void and(ISet set) {
		if(! (set instanceof ISetBits)) {
		    throw new UnsupportedOperationException("OPERAND should be BitSet");
		}
		BitSet bits = (BitSet) set.get();
		this.mSet.and(bits);
	}

	@Override
	public void or(ISet set) {
		if(! (set instanceof ISetBits)) {
			throw new UnsupportedOperationException("OPERAND should be BitSet");
		}
		BitSet bits = (BitSet) set.get();
		this.mSet.or(bits);		
	}

	@Override
	public boolean get(int value) {
		return mSet.get(value);
	}
	
	@Override
	public void set(int value) {
		mSet.set(value);
	}

	@Override
	public void clear(int value) {
		mSet.clear(value);
	}
	
	@Override
	public void clear() {
		mSet.clear();
	}

	@Override
	public boolean isEmpty() {
		return mSet.isEmpty();
	}

	@Override
	public int cardinality() {
		return mSet.cardinality();
	}
	
	@Override
	public boolean overlap(ISet set) {
		if(! (set instanceof ISetBits)) {
		    throw new UnsupportedOperationException("OPERAND should be BitSet");
		}
		ISetBits temp = (ISetBits) set;
		return temp.mSet.intersects(this.mSet);
	}
	

	@Override
	public boolean subsetOf(ISet set) {
		if(! (set instanceof ISetBits)) {
		    throw new UnsupportedOperationException("OPERAND should be BitSet");
		}
		BitSet temp = (BitSet) this.mSet.clone();
		BitSet bits = (BitSet) set.get();
		temp.andNot(bits);
		return temp.isEmpty();
	}

	@Override
	public boolean contentEq(ISet set) {
		if(! (set instanceof ISetBits)) {
			throw new UnsupportedOperationException("OPERAND should be BitSet");
		}
		BitSet bits = (BitSet) set.get();
		return this.mSet.equals(bits);
	}

	@Override
	public Object get() {
		return mSet;
	}
	
	@Override
	public String toString() {
		return mSet.toString();
	}
	
	public boolean equals(Object obj) {
		if(! (obj instanceof ISetBits)) {
		    throw new UnsupportedOperationException("OPERAND should be BitSet");
		}
		ISetBits bits = (ISetBits)obj;
		return this.contentEq(bits);
	}
	
	public static class BitsIterator implements Iterator<Integer> {

		private BitSet mBits;
		private int mIndex;
		
		public BitsIterator(ISetBits set) {
			this.mBits = set.mSet;
			mIndex = mBits.nextSetBit(0);
		}
		
		public boolean hasNext() {
			return (mIndex >= 0);
		}
		
		public Integer next() {
			int rv = mIndex;
			mIndex = mBits.nextSetBit(mIndex + 1);
			return rv;
		}
	}

    @Override
    public Iterator<Integer> iterator() {
        return new BitsIterator(this);
    }

    @Override
    public void set(int from, int to) {
        mSet.set(from, to + 1);
    }

}
