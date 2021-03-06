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

package operation.complement.ncsb;

import main.Options;
import util.ISet;
import util.UtilISet;

/**
 * NCSB tuple 
 * TODO: in order to make it unmodifiable
 * */
public class NCSB {
	
	protected ISet mNSet;
	protected ISet mCSet;
	protected ISet mSSet;
	protected ISet mBSet;
	
	public NCSB(ISet N, ISet C, ISet S, ISet B) {
		this.mNSet = N;
		this.mCSet = C;
		this.mSSet = S;
		this.mBSet = B;
	}
	
	public NCSB() {
		this.mNSet = UtilISet.newISet();
		this.mCSet = UtilISet.newISet();
		this.mSSet = UtilISet.newISet();
		this.mBSet = UtilISet.newISet();
	}
	
	// be aware that we use the same object
	//CLONE object to make modification
	public ISet getNSet() {
		return  mNSet;
	}
	
	public ISet getCSet() {
		return  mCSet;
	}
	
	public ISet getSSet() {
		return  mSSet;
	}
	
	public ISet getBSet() {
		return  mBSet;
	}
	
	// Safe operations for (N, C, S, B)
	public ISet copyNSet() {
		return  mNSet.clone();
	}
	
	public ISet copyCSet() {
		return  mCSet.clone();
	}
	
	public ISet copySSet() {
		return  mSSet.clone();
	}
	
	public ISet copyBSet() {
		return  mBSet.clone();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof NCSB)) {
			return false;
		}
		NCSB ncsb = (NCSB)obj;
		return  contentEqual(ncsb);
	}
	
	protected boolean contentEqual(NCSB ncsb) {
		if(! mNSet.equals(ncsb.mNSet)
		|| ! mCSet.equals(ncsb.mCSet)
		|| ! mSSet.equals(ncsb.mSSet)
		|| ! mBSet.equals(ncsb.mBSet)) {
			return false;
		}
		return true;
	}
	

	public boolean coveredBy(NCSB other) {
	    if(Options.mLazyS && !other.mBSet.subsetOf(mBSet)) {
            return false;
        }
		if(! other.mNSet.subsetOf(mNSet)
		|| ! other.mCSet.subsetOf(mCSet)
		|| ! other.mSSet.subsetOf(mSSet)) {
			return false;
		}

		return true;
	}
	
	// this.N >= other.N & this.C >= other.C & this.S >= other.S & this.B >= other.B
	public boolean strictlyCoveredBy(NCSB other) {
		if(! other.mNSet.subsetOf(mNSet)
		|| ! other.mCSet.subsetOf(mCSet)
		|| ! other.mSSet.subsetOf(mSSet)
		|| ! other.mBSet.subsetOf(mBSet)) {
			return false;
		}

		return true;
	}
	
	private ISet mAllSets = null; 
	
	private void initializeAllSets() {
	    mAllSets = copyNSet();
	    mAllSets.or(mCSet);
	    mAllSets.or(mSSet);
	}
	
	public boolean subsetOf(NCSB other) {
	    if(mAllSets == null) {
	        initializeAllSets();
	    }
	    if(other.mAllSets == null) {
	        other.initializeAllSets();
	    }
        return mAllSets.subsetOf(other.mAllSets);
	}
	
	@Override
	public NCSB clone() {
		return new NCSB(mNSet.clone(), mCSet.clone(), mSSet.clone(), mBSet.clone());
	}
	
	@Override
	public String toString() {
		return "(" + mNSet.toString() + "," 
		           + mCSet.toString() + ","
		           + mSSet.toString() + ","
		           + mBSet.toString() + ")";
	}
	
    protected int hashCode;
    protected boolean hasCode = false;
	
	@Override
	public int hashCode() {
		if(hasCode) return hashCode;
		else {
			hasCode = true;
			hashCode = 1;
			final int prime = 31;
			hashCode= prime * hashCode + hashValue(mNSet);
			hashCode= prime * hashCode + hashValue(mCSet);
			hashCode= prime * hashCode + hashValue(mSSet);
			hashCode= prime * hashCode + hashValue(mBSet);
			return hashCode;
		}
	}
	
	public static int hashValue(ISet set) {
		final int prime = 31;
        int result = 1;
        for(final int n : set) {
        	result = prime * result + n;
        }
        return result;
	}
	

}
