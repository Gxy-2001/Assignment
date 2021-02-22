package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.MMU;
import cpu.alu.ALU;
import cpu.registers.EAX;
import cpu.registers.Register;
import memory.Disk;
import memory.Memory;

/**
 * @program: 2020
 * @description:
 * @author: Gxy-2001
 * @create: 2020-12-10
 */
public class Add implements Instruction {
    @Override
    public int exec(String eip, int opcode) {
        //32位立即数和eax相加
        Register eax = CPU_State.eax;
        ALU alu = new ALU();
        String cs = CPU_State.cs.read();
        String imm32 = (new String(MMU.getMMU().read(cs + eip, 40))).substring(8);

        String EAX = eax.read();
        eax.write(alu.add(imm32, EAX));
        return 40;
    }
}
