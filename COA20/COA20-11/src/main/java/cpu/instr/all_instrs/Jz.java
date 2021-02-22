package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.MMU;
import cpu.alu.ALU;
import cpu.registers.EFlag;
import cpu.registers.Register;
import memory.Disk;
import memory.Memory;
import program.Log;

/**
 * @program: 2020
 * @description:
 * @author: Gxy-2001
 * @create: 2020-12-11
 */
public class Jz implements Instruction {
    @Override
    public int exec(String eip, int opcode) {
        ALU alu = new ALU();
        String cs = CPU_State.cs.read();
        String instr = (new String(MMU.getMMU().read(cs + eip, 16)));
        Log.write(instr);
        String imm8 = instr.substring(8);

        EFlag eflag = (EFlag) CPU_State.eflag;
        if (eflag.getZF()) {
            CPU_State.eip.write(alu.add(CPU_State.eip.read(), imm8));
            return 0;
        } else {
            //CPU_State.eip.write(alu.add(CPU_State.eip.read(), "00000000000000000000000000010000"));
            return 16;
        }
    }
}