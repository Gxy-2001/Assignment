package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.MMU;
import cpu.alu.ALU;
import program.Log;

/**
 * @program: 2019
 * @description:
 * @author: Gxy-2001
 * @create: 2020-12-21
 */
public class Jmp implements Instruction{
    @Override
    public int exec(String eip, int opcode) {
        String cs = CPU_State.cs.read();
        String instr = String.valueOf(MMU.getMMU().read(cs+eip,16));
        Log.write(instr);
        String imm8 = instr.substring(8);
        CPU_State.eip.write(new ALU().add(CPU_State.eip.read(),imm8));
        return 0;
    }
}
