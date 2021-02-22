package cpu;

import cpu.instr.all_instrs.InstrFactory;
import cpu.instr.all_instrs.Instruction;
import transformer.Transformer;

public class CPU {


    Transformer transformer = new Transformer();
    MMU mmu = MMU.getMMU();


    /**
     * execInstr specific numbers of instructions
     *
     * @param number numbers of instructions
     */
    public int execInstr(long number) {
        // 执行过的指令的总长度
        int totalLen = 0;
        while (number > 0) {
            // TODO
            number--;
            totalLen += execInstr();
        }
        return totalLen;
    }

    /**
     * execInstr a single instruction according to eip value
     */
    private int execInstr() {
        String eip = CPU_State.eip.read();
        int len = decodeAndExecute(eip);
        if (len != 0) {
            //更新eip
            CPU_State.eip.write(transformer.intToBinary((Integer.parseInt(eip, 2) + len) + ""));
        }
        return len;
    }

    private int decodeAndExecute(String eip) {
        int opcode = instrFetch(eip, 1);
        Instruction instruction = InstrFactory.getInstr(opcode);
        assert instruction != null;

        int len = instruction.exec(eip, opcode);
        return len;


    }

    /**
     * @param eip
     * @param length opcode的字节数，本作业只使用单字节opcode
     * @return
     */
    private int instrFetch(String eip, int length) {
        String segReg = CPU_State.cs.read();
        String s = new String(mmu.read(segReg + eip, length * 8));
        int res = Integer.parseInt(s, 2);
        return res;
    }

    public void execUntilHlt() {
        // TODO ICC
        while (true) {
            int t = execInstr();
            if (t == 8) {
                //hack for halt
                break;
            }
        }
    }
}

