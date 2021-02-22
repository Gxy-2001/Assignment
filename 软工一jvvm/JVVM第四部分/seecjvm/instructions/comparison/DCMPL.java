package com.njuse.seecjvm.instructions.comparison;

import com.njuse.seecjvm.instructions.base.NoOperandsInstruction;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;

public class DCMPL extends NoOperandsInstruction {

    /**
     * TODO：实现这条指令
     * 比较栈顶两double型数值大小，并将结果（1，0，-1）压入栈顶；当其中一个数值为NaN时，将-1压入栈顶
     */
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        double val2 = stack.popDouble();
        double val1 = stack.popDouble();
        if(Double.isNaN(val1) || Double.isNaN(val2)) stack.pushInt(-1);
        if (val1>val2)stack.pushInt(1);
        if (val1==val2)stack.pushInt(0);
        if (val1<val2)stack.pushInt(-1);
    }
}
