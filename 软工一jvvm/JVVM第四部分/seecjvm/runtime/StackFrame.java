package com.njuse.seecjvm.runtime;

import com.njuse.seecjvm.memory.jclass.Method;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StackFrame {
    //栈帧,ThreadStack下的栈帧
    private OperandStack operandStack;
    private Vars localVars;
    private JThread thread;
    private int nextPC;
    private Method method;

    public StackFrame(JThread thread, Method method, int maxStackSize, int maxVarSize) {
        this.thread = thread;
        this.method = method;
        operandStack = new OperandStack(maxStackSize);
        localVars = new Vars(maxVarSize);
    }

}
