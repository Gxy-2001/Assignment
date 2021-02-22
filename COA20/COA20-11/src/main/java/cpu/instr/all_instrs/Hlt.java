package cpu.instr.all_instrs;


import cpu.CPU_State;
import cpu.MMU;
import program.Log;

/**
 * @program: 2020
 * @description:
 * @author: Gxy-2001
 * @create: 2020-12-15
 */
public class Hlt implements Instruction {
    @Override
    public int exec(String eip, int opcode) {
        //结束
        String cs = CPU_State.cs.read();
        String instr = new String(MMU.getMMU().read(cs + eip, 8));
        Log.write(instr);
        return 8;
    }
}