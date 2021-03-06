package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.MMU;
import cpu.alu.ALU;
import cpu.registers.Register;
import memory.Disk;
import memory.Memory;

/**
 * @program: 2020
 * @description:
 * @author: Gxy-2001
 * @create: 2020-12-11
 */
public class Sbb implements Instruction {
    @Override
    public int exec(String eip, int opcode) {

        Register eax = CPU_State.eax;
        ALU alu = new ALU();

        String cs = CPU_State.cs.read();
        String imm32 = (new String(MMU.getMMU().read(cs + eip, 40))).substring(8);

        String EAX = eax.read();
        String carry = "00000000000000000000000000000001";
        //DEST ← DEST - (SRC + CF);
        eax.write(alu.sub(carry, alu.sub(imm32, EAX)));
        return 40;
    }
}