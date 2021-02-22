package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.alu.ALU;
import cpu.registers.Register;
import memory.Disk;
import memory.Memory;
import transformer.Transformer;

/**
 * @program: 2020
 * @description:
 * @author: Gxy-2001
 * @create: 2020-12-11
 */
public class Pop implements Instruction {
    @Override
    public int exec(String eip, int opcode) {
        Memory memory = Memory.getMemory();
        Transformer transformer = new Transformer();
        String s = memory.topOfStack(CPU_State.esp.read());
        CPU_State.esp.write(transformer.intToBinary(Integer.parseInt(CPU_State.esp.read(), 2) + 1 + ""));

        if (opcode == 88) {
            CPU_State.eax.write(s);
        } else if (opcode == 89) {
            CPU_State.ecx.write(s);
        } else if (opcode == 90) {
            CPU_State.edx.write(s);
        }
        return 8;
    }
}