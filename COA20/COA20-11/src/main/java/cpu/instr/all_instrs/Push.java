package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.alu.ALU;
import memory.Memory;
import transformer.Transformer;

/**
 * @program: 2020
 * @description:
 * @author: Gxy-2001
 * @create: 2020-12-11
 */
public class Push implements Instruction {
    @Override
    public int exec(String eip, int opcode) {
        ALU alu = new ALU();
        Memory memory = Memory.getMemory();
        String s = CPU_State.ebx.read();
        Transformer transformer = new Transformer();
        CPU_State.esp.write(transformer.intToBinary(Integer.parseInt(CPU_State.esp.read(), 2) - 1 + ""));

        memory.pushStack(CPU_State.esp.read(), s);
        //栈指针-1,改一下地址
        return 8;
    }
}
