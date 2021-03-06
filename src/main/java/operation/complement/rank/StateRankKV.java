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

package operation.complement.rank;

import java.util.Collection;

import main.Options;
import util.ISet;

/**
 * a representation for a state in rank-based complementation algorithm (S, O, f)
 * */

public class StateRankKV extends StateRank<ComplementRankKV> {
    
    public StateRankKV(ComplementRankKV complement, int id, LevelRanking lvlRank) {
        super(complement, id, lvlRank);
    }
    
    @Override
    public ISet getSuccessors(int letter) {
        if(mVisitedLetters.get(letter)) {
            return super.getSuccessors(letter);
        }
        mVisitedLetters.set(letter);
        LevelRankingConstraint constraint = UtilRank.getRankedConstraint(mOperand, mLevelRanking, letter);
        LevelRankingGenerator generator = new LevelRankingGenerator(mOperand);
        System.out.println("state=" + this.toString() + " letter=" + letter);
        Collection<LevelRanking> lvlRanks = generator.generateLevelRankings(constraint, Options.mMinusOne);
        for(LevelRanking lvlRank : lvlRanks) {
            StateRankKV succ = mComplement.getOrAddState(lvlRank);
            super.addSuccessor(letter, succ.getId());
            System.out.println("Successor: " + succ.getId() + " = " + succ);
        }
        
        return super.getSuccessors(letter);
    }

}
