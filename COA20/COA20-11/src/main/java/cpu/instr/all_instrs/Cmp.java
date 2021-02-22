package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.MMU;
import cpu.alu.ALU;
import cpu.registers.EFlag;
import cpu.registers.Register;
import memory.Disk;
import memory.Memory;
import program.Log;
import transformer.Transformer;

/**
 * @program: 2020
 * @description:
 * @author: Gxy-2001
 * @create: 2020-12-11
 */
public class Cmp implements Instruction {
    String instr;
    String cs;
    String eip;

    @Override
    public int exec(String eip, int opcode) {
        //32位立即数和eax和carry相加
        this.eip = eip;
        cs = CPU_State.cs.read();
        switch (opcode) {
            case 61:
                cmp_61();
                return 40;
            case 57:
                cmp_57();
                return 16;
            default:
                return 0;
        }

    }

    private void cmp_57() {
        //0x39 CMP Ev, Gv
        instr = new String(MMU.getMMU().read(cs + eip, 16));
        Log.write(instr);

        String a = "";
        String b = "";
        String MOD = instr.substring(8, 10);
        String Reg_Opcode = instr.substring(10, 13);
        String R_M = instr.substring(13, 16);
        EFlag eflag = (EFlag) CPU_State.eflag;
        switch (Reg_Opcode) {
            case "000":
                b = CPU_State.eax.read();
                break;
            case "001":
                b = CPU_State.ecx.read();
                break;
            case "010":
                b = CPU_State.edx.read();
                break;
            case "011":
                b = CPU_State.ebx.read();
                break;
            default:
        }
        if (MOD.equals("11")) {
            //Ev在寄存器
            switch (R_M) {
                case "000":
                    a = CPU_State.eax.read();
                    break;
                case "001":
                    a = CPU_State.ecx.read();
                    break;
                case "010":
                    a = CPU_State.edx.read();
                    break;
                case "011":
                    a = CPU_State.ebx.read();
                    break;
                default:
            }
        }
        if (b.equals(a)) {
            //b=a
            eflag.setZF(true);
            eflag.setOF(false);
            eflag.setSF(false);
        } else if ((new ALU().sub(b, a)).charAt(0) == '0') {
            //b>a
            eflag.setZF(false);
            eflag.setOF(false);
            eflag.setSF(false);
        } else if ((new ALU().sub(b, a)).charAt(0) == '1') {
            //a>b
            eflag.setZF(false);
            eflag.setOF(false);
            eflag.setSF(true);
        }
    }

    private void cmp_61() {
        instr = new String(MMU.getMMU().read(cs + eip, 48));
        Log.write(instr);

        Register eax = CPU_State.eax;
        ALU alu = new ALU();
        String imm32 = instr.substring(8);
        String EAX = eax.read();
        EFlag eflag = (EFlag) CPU_State.eflag;
        if (alu.sub(EAX, imm32).equals("00000000000000000000000000000000")) {
            eflag.setZF(true);
        } else {
            eflag.setZF(false);
        }
    }
}