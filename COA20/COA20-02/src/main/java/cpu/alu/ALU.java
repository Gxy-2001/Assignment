package cpu.alu;

/**
 * Arithmetic Logic Unit
 * ALU封装类
 * TODO: 加减与逻辑运算
 */
public class ALU {

    // 模拟寄存器中的进位标志位
    private String CF = "0";

    // 模拟寄存器中的溢出标志位
    private String OF = "0";

    //add two integer
    public String add(String src, String dest) {
        char c = '0';
        StringBuilder sb = new StringBuilder();
        for (int i = src.length() - 1; i >= 0; i--) {
            String t = fulladd(src.charAt(i), dest.charAt(i), c);
            c = t.charAt(1);
            sb.insert(0, t.charAt(0));
        }
        OF = String.valueOf(c);
        return sb.toString();
        /*char[] X = src.toCharArray();
        char[] Y = dest.toCharArray();
        char C = '0';
        StringBuilder sb = new StringBuilder();
        for (int i = X.length - 1; i >= 0; i--) {
            sb.append((char) (X[i] ^ Y[i] ^ C));
            C = (char) ((X[i] & Y[i]) | (X[i] & C) | (Y[i] & C));
        }
        //TODO 要不然在最后reverse，要不然就insert(0,char)
        return sb.reverse().toString();
        //return CLA(src, dest, '0').substring(0, 32);

        */
        /*StringBuilder res = new StringBuilder();
        String s = fullAdder(src.charAt(31), dest.charAt(31), '0');
        res.append(s.charAt(1));
        for (int i = 30; i >= 0; i--) {
            s = fullAdder(src.charAt(i), dest.charAt(i), s.charAt(0));
            res.insert(0, s.charAt(1));
        }
        return res.toString();*/
    }


    private String fulladd(char x, char y, char c) {
        char s = (char) (x ^ y ^ c);
        c = (char) ((x & y) | (y & c) | (x & c));
        return s + "" + c;
    }

    public String CLA(String src, String dest, char carry) {
        char[] s = src.toCharArray();
        char[] d = dest.toCharArray();
        int n = s.length;
        int[] X = new int[n];
        int[] Y = new int[n];

        for (int i = n - 1; i >= 0; i--) {
            X[i] = (s[n - i - 1] - '0');
            Y[i] = (d[n - i - 1] - '0');
        }

        int[] p = new int[n];
        int[] g = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = X[i] | Y[i];
            g[i] = X[i] & Y[i];
        }

        int[] C = new int[n];

        for (int i = 1; i < n; i++) {
            C[i] = (C[i - 1] & p[i]) | g[i];
        }

        StringBuilder res = new StringBuilder("" + (X[0] ^ Y[0] ^ (carry - '0')));
        for (int i = 1; i < n; i++) {
            res.append(X[i] ^ Y[i] ^ C[i - 1]);
        }
        res = (res.reverse()).append(C[n - 1]);
        return res.toString();
    }

    public String fullAdder(char X, char Y, char C) {
        int S = (X - '0') ^ (Y - '0') ^ (C - '0');
        int c = ((X - '0') & (Y - '0')) | ((X - '0') & (C - '0')) | ((Y - '0') & (C - '0'));
        return c + "" + S;
    }

    //sub two integer
    // dest - src
    public String sub(String src, String dest) {
        return add(dest, Com(src));
    }


    private String Com(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            //TODO 这里不能 (char)s[i]^1
            sb.append((s.charAt(i) ^ '1'));
        }
        for (int i = sb.length() - 1; i >= 0; i--) {
            if (sb.charAt(i) == '0') {
                sb.replace(i, i + 1, "1");
                break;
            } else {
                sb.replace(i, i + 1, "0");
            }
        }
        return sb.toString();
    }

    public String and(String src, String dest) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            //TODO 加(char)
            sb.append((char) (src.charAt(i) & dest.charAt(i)));
        }
        return sb.toString();
    }

    public String or(String src, String dest) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            sb.append((char) (src.charAt(i) | dest.charAt(i)));
        }
        return sb.toString();
    }

    public String xor(String src, String dest) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            //TODO 真nmd奇怪，这里又不能加(char)
            sb.append((src.charAt(i) ^ dest.charAt(i)));
        }
        return sb.toString();
    }

}
