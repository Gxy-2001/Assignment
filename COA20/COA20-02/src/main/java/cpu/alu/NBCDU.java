package cpu.alu;

public class NBCDU {

    // 模拟寄存器中的进位标志位
    private String CF = "0";

    // 模拟寄存器中的溢出标志位
    private String OF = "0";
    ALU alu = new ALU();

    /**
     * @param a A 32-bits NBCD String
     * @param b A 32-bits NBCD String
     * @return a + b
     */
    public String add(String a, String b) {
        //首先处理两个符号相同数字相加
        if (a.substring(0, 4).equals(b.substring(0, 4))) {
            String flag = a.substring(0, 4);
            String C = "00000";
            StringBuilder sb = new StringBuilder();
            for (int i = 32; i > 4; i -= 4) {
                String F = alu.add(C, alu.add("0" + a.substring(i - 4, i), "0" + b.substring(i - 4, i)));
                if (Integer.valueOf(F, 2) >= 10) {
                    C = "00001";
                    F = alu.add(F, "00110");
                    sb.insert(0, F.substring(1));
                } else {
                    sb.insert(0, F.substring(1));
                    C = "00000";
                }
            }
            return flag + sb.toString();
        } else if (a.startsWith("1101")) {
            return add(b, a);
        } else {
            //a是正数，b是负数
            // 67-34=67+(100-34)-100=67+(99-34)+1-100
            // 其中99-34的部分是一个取反

            //首先对b的后28位的7个数字进行取反，首先要判断这7个数字要取反几个数字
            int n = numOfBit(b.substring(4));
            //对后n位进行取反
            StringBuilder sb = new StringBuilder(b);
            for (int i = 0; i < n; i++) {
                int start = 32 - 4 * (i + 1), end = 32 - 4 * i;
                sb.replace(start, end, reverse(sb.substring(start, end)));
            }
            sb.replace(0, 4, "1100");
            //将 a,反b,1三者相加
            String res = add(a, add(sb.toString(), "11000000000000000000000000000001"));
            //判断借位的问题，我是向第n+1位借的，有两种情况，够还or不够还
            if (numOfBit(res.substring(4)) >= n + 1) {
                //够还： 只要res 的bit数>=n+1，那一定能还上，还上就行，就是在高位那里减个1，结果是正数
                sb = new StringBuilder(res);
                String string = Integer.toString(Integer.valueOf(sb.substring(32 - (n + 1) * 4, 32 - n * 4), 2) - 1, 2);
                return sb.replace(32 - (n + 1) * 4, 32 - n * 4, Full(string, "0", 4)).toString();
            } else {
                //不够还：取反加一再加上一个负号，这样做具体的原因：见ppt
                n = numOfBit(res.substring(4));
                //对后n位进行取反
                sb = new StringBuilder(res);
                for (int i = 0; i < n; i++) {
                    int start = 32 - 4 * (i + 1), end = 32 - 4 * i;
                    sb.replace(start, end, reverse(sb.substring(start, end)));
                }
                //加1
                sb = new StringBuilder(add(sb.toString(), "11000000000000000000000000000001"));
                //负号
                sb.replace(0, 4, "1101");
                return sb.toString();
            }
        }
    }

    private String reverse(String s) {
        return Full(Integer.toString(9 - Integer.valueOf(s, 2), 2), "0", 4);
    }

    //返回后7位中需要处理的位数
    //TODO 超级超级重要：这个函数必须必须.subString(4),要不然都返回7
    //TODO 太重要了
    public int numOfBit(String s) {
        int i = 0;
        for (; i < s.length(); i += 4) {
            if (!"0000".equals(s.substring(i, i + 4))) {
                break;
            }
        }
        return 7 - i / 4;
    }


    private String Full(String target, String fullChar, int n) {
        StringBuilder sb = new StringBuilder(target);
        while (sb.length() < n) {
            sb.insert(0, fullChar);
        }
        return sb.toString();
    }

    /***
     *
     * @param a A 32-bits NBCD String
     * @param b A 32-bits NBCD String
     * @return b - a
     */
    public String sub(String a, String b) {
        a = "1100".equals(a.substring(0, 4)) ? "1101" + a.substring(4) : "1100" + a.substring(4);
        return add(b, a);
    }

}
