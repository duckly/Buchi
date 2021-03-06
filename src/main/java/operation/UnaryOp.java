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

package operation;

public abstract class UnaryOp<I, O> implements IUnaryOp<I, O>{
    
    protected final I mOperand;
    protected O mResult;
    
    public UnaryOp(I operand) {
        mOperand = operand;
    }

    @Override
    public I getOperand() {
        return mOperand;
    }

    @Override
    public O getResult() {
        return mResult;
    }
    

}
