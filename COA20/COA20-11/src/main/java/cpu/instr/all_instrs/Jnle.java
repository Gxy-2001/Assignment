package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.MMU;
import cpu.alu.ALU;
import cpu.registers.EFlag;
import program.Log;

/**
 * @program: 2019
 * @description:
 * @author: Gxy-2001
 * @create: 2020-12-21
 */
public class Jnle implements Instruction {
    @Override
    public int exec(String eip, int opcode) {
        //0x7f	JG/JNLE rel8
        String cs = CPU_State.cs.read();
        String instr = new String(MMU.getMMU().read(cs + eip, 16));
        Log.write(instr);

        EFlag eflag = (EFlag) CPU_State.eflag;
        String imm8 = instr.substring(8);

        if (!eflag.getZF() && !eflag.getOF() && !eflag.getSF()) {//大于就跳转，需要是正号 不为0 没有进位
            CPU_State.eip.write(new ALU().add(CPU_State.eip.read(), imm8));
            return 0;
        } else {
            //CPU_State.eip.write(new ALU().add(CPU_State.eip.read(),"00000000000000000000000000010000"));
            return 16;
        }

        //return 0;
    }
}
