package com.njuse.seecjvm.instructions.load;

import com.njuse.seecjvm.instructions.base.Index8Instruction;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;
import com.njuse.seecjvm.runtime.Vars;

public class ILOAD extends Index8Instruction {

    /**
     * TODO：实现这条指令
     * 其中成员index是这条指令的参数，已经读取好了
     * 将指定的int型本地变量推送至栈顶
     */
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        Vars vars= frame.getLocalVars();
        stack.pushInt(vars.getInt(index));
    }
}
