package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.MMU;
import cpu.alu.ALU;
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
public class Mov implements Instruction {
    //move
    String instr;
    String cs;
    String eip;
    ALU alu = new ALU();
    Memory memory = Memory.getMemory();

    @Override
    public int exec(String eip, int opcode) {
        this.eip = eip;
        cs = CPU_State.cs.read();
        switch (opcode) {
            case 184:
                mov_184();
                return 40;
            case 199:
                mov_199();
                return 80;
            case 139:
                mov_139();
                return 48;
            case 137:
                mov_137();
                return 48;
            default:
                return 0;
        }
    }

    private void mov_137() {
        //0x89 MOV Ev, Gv
        instr = new String(MMU.getMMU().read(cs + eip, 48));
        Log.write(instr);

        String MOD = instr.substring(8, 10);
        String Reg_Opcode = instr.substring(10, 13);
        String R_M = instr.substring(13, 16);
        String displacement = instr.substring(16);
        String tar = "";
        String baseAddr = "";
        switch (Reg_Opcode) {
            case "000":
                tar = CPU_State.eax.read();
                break;
            case "001":
                tar = CPU_State.ecx.read();
                break;
            case "010":
                tar = CPU_State.edx.read();
                break;
            case "011":
                tar = CPU_State.ebx.read();
                break;
            default:
        }
        if (MOD.equals("11")) {
            //Ev在寄存器
            switch (R_M) {
                case "000":
                    CPU_State.eax.write(tar);
                    break;
                case "001":
                    CPU_State.ecx.write(tar);
                    break;
                case "010":
                    CPU_State.edx.write(tar);
                    break;
                case "011":
                    CPU_State.ebx.write(tar);
                    break;
                default:
            }
        } else {
            //Ev在内存
            switch (R_M) {
                case "000":
                    baseAddr = CPU_State.eax.read();
                    break;
                case "001":
                    baseAddr = CPU_State.ecx.read();
                    break;
                case "010":
                    baseAddr = CPU_State.edx.read();
                    break;
                case "011":
                    baseAddr = CPU_State.ebx.read();
                    break;
                default:
            }
            memory.write(alu.add(baseAddr, displacement), 32, tar.toCharArray());
        }
    }

    private void mov_139() {
        //0x8b MOV Gv, Ev
        //Move immediate word to r/m word
        instr = new String(MMU.getMMU().read(cs + eip, 48));
        Log.write(instr);

        String MOD = instr.substring(8, 10);
        String Reg_Opcode = instr.substring(10, 13);
        String R_M = instr.substring(13, 16);
        String displacement = instr.substring(16, 48);
        String tar = "";
        if (MOD.equals("11")) {
            //Ev在寄存器
            switch (R_M) {
                case "000":
                    tar = CPU_State.eax.read();
                    break;
                case "001":
                    tar = CPU_State.ecx.read();
                    break;
                case "010":
                    tar = CPU_State.edx.read();
                    break;
                case "011":
                    tar = CPU_State.ebx.read();
                    break;
                default:
            }
        } else {
            //Ev在内存
            String addr = "";
            switch (R_M) {
                case "000":
                    addr = CPU_State.eax.read();
                    break;
                case "001":
                    addr = CPU_State.ecx.read();
                    break;
                case "010":
                    addr = CPU_State.edx.read();
                    break;
                case "011":
                    addr = CPU_State.ebx.read();
                    break;
                default:
            }
            tar = String.valueOf(MMU.getMMU().read(CPU_State.cs.read() + alu.add(addr, displacement), 32));
        }
        switch (Reg_Opcode) {
            case "000":
                CPU_State.eax.write(tar);
                break;
            case "001":
                CPU_State.ecx.write(tar);
                break;
            case "010":
                CPU_State.edx.write(tar);
                break;
            case "011":
                CPU_State.ebx.write(tar);
                break;
            default:
        }
    }

    private void mov_199() {
        //0xc7 MOV Ev, Iv
        instr = new String(MMU.getMMU().read(cs + eip, 80));
        Log.write(instr);

        String MOD = instr.substring(8, 10);
        String R_M = instr.substring(13, 16);
        String displacement = instr.substring(16, 48);
        String imm32 = instr.substring(48, 80);
        String baseAddr = "";
        if (MOD.equals("11")) {
            //Ev在寄存器
            switch (R_M) {
                case "000":
                    CPU_State.eax.write(imm32);
                    break;
                case "001":
                    CPU_State.ecx.write(imm32);
                    break;
                case "010":
                    CPU_State.edx.write(imm32);
                    break;
                case "011":
                    CPU_State.ebx.write(imm32);
                    break;
                default:
                    break;
            }
        } else {
            //Ev在内存里
            //RM表示基地址的位置
            switch (R_M) {
                case "000":
                    baseAddr = CPU_State.eax.read();
                    break;
                case "001":
                    baseAddr = CPU_State.ecx.read();
                    break;
                case "010":
                    baseAddr = CPU_State.edx.read();
                    break;
                case "011":
                    baseAddr = CPU_State.ebx.read();
                    break;
                default:
                    baseAddr = "";
            }
            memory.write(alu.add(baseAddr, displacement), 32, imm32.toCharArray());
        }
    }

    private void mov_184() {
        instr = new String(MMU.getMMU().read(cs + eip, 40));
        Log.write(instr);

        String imm32 = instr.substring(8);
        CPU_State.eax.write(imm32);
    }
}