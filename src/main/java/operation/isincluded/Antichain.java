/*
 * Written by Yong Li (liyong@ios.ac.cn)
 * This file is part of the Buchi which is a simple version of SemiBuchi.
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

package operation.isincluded;

import java.util.ArrayList;
import java.util.List;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import operation.complement.ncsb.StateNcsbOtf;

public class Antichain {
    
    private TIntObjectMap<List<StateNcsbOtf>> mPairMap;
    
    public Antichain() {
        mPairMap = new TIntObjectHashMap<>();
    }
    
    /**
     * return true if @param snd has been added successfully
     * */
    public boolean addAsccPair(AsccPair pair) {
        return addAsccPair(pair.getFstState(), pair.getSndComplementState());
    }
    
    public boolean addAsccPair(int fst, StateNcsbOtf snd) {
        
        List<StateNcsbOtf> sndElem = mPairMap.get(fst);
        
        if(sndElem == null) {
            sndElem = new ArrayList<>();
        }
        
        List<StateNcsbOtf> copy = new ArrayList<>();
        //avoid to add pairs are covered by other pairs
        for(int i = 0; i < sndElem.size(); i ++) {
            StateNcsbOtf s = sndElem.get(i);
            //pairs covered by the new pair
            //will not be kept in copy
            if(s.getNCSB().coveredBy(snd.getNCSB())) {
//              mTask.increaseDelPairInAntichain();
                continue;
            }else if(snd.getNCSB().coveredBy(s.getNCSB())) {
                // no need to add it
//              mTask.increaseRejPairByAntichain();
                return false;
            }
            copy.add(s);
        }
        
        copy.add(snd); // should add snd
        mPairMap.put(fst, copy);
        return true;
    }
    
    public boolean covers(AsccPair pair) {
        List<StateNcsbOtf> sndElem = mPairMap.get(pair.getFstState());
        if(sndElem == null) return false;
        
        StateNcsbOtf snd = pair.getSndComplementState();
        for(int i = 0; i < sndElem.size(); i ++) {
            StateNcsbOtf s = sndElem.get(i);
            if(snd.getNCSB().coveredBy(s.getNCSB())) { // no need to add it
                return true;
            }
        }
        
        return false;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
//      for(Entry<Integer, List<StateNCSB>> entry : mPairMap.entrySet()) {
//          sb.append(entry.getKey() + " -> " + entry.getValue() + "\n");
//      }
        TIntObjectIterator<List<StateNcsbOtf>> iter = mPairMap.iterator();
        while(iter.hasNext()) {
            iter.advance();
            sb.append(iter.key() + " -> " + iter.value() + "\n");
        }
        return sb.toString();
    }
    
    public int size() {
        int num = 0;
//      for(Map.Entry<Integer, List<StateNCSB>> entry : mPairMap.entrySet()) {
//          num += entry.getValue().size();
//      }
        TIntObjectIterator<List<StateNcsbOtf>> iter = mPairMap.iterator();
        while(iter.hasNext()) {
            iter.advance();
            num += iter.value().size();
        }
        return num;
    }

}
