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

import main.Options;

public class LevelRankingConstraint extends LevelRanking {
    
    public LevelRankingConstraint() {
        super(true, Options.mTurnwise);
    }
    
    
    public void addConstraint(final int state, final int predRank, final boolean predIsInO
            , final boolean predOIsEmpty) {
        final int oldRank = mRanks.get(state);
        int rank = oldRank;
        if (!mRanks.containsKey(state) || oldRank > predRank) {
            rank = predRank;
        }
        boolean isInO = predIsInO || predOIsEmpty;
        this.addLevelRank(state, rank, isInO);
    }
    

}
