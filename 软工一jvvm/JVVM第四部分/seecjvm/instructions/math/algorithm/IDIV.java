package com.njuse.seecjvm.instructions.math.algorithm;

import com.njuse.seecjvm.instructions.base.NoOperandsInstruction;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;

public class IDIV extends NoOperandsInstruction {

    /**
     * TODO：实现这条指令
     * 将栈顶两int型数值相除并将结果压入栈顶
     */
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        int val2 = stack.popInt();
        int val1 = stack.popInt();
        stack.pushInt(val1/val2);
    }
}
