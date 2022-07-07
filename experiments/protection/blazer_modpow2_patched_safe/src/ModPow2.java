import java.math.BigInteger;
import java.util.Random;

public class ModPow2 {
  
/*
 * this example is from STAC engagement proram: snapbuddy_1
 */
    
    //top-level modPow method: replaced the fastMultiply with BigInteger standard multiply
    public static BigInteger modPow2_safe(BigInteger base, BigInteger exponent, BigInteger modulus, int width) {
        BigInteger r0 = BigInteger.valueOf(1);
        BigInteger r1 = base;
        // int width = exponent.bitLength(); // use width parameter because bitlength is wrong for 0
        for (int i = 0; i < width; i++) {
            BigInteger tmp0 = r0.multiply(r1).mod(modulus);
            BigInteger tmp1 = r0.multiply(r0).mod(modulus);
            BigInteger tmp2 = r1.multiply(r1).mod(modulus);
            if (!exponent.testBit(width - i - 1)) {
                // r1 = OptimizedMultiplier.fastMultiply(r0, r1).mod(modulus);
                r1 = tmp0;
                r0 = tmp1;
                // r1 = r0.multiply(r1).mod(modulus);
                // r0 = r0.multiply(r0).mod(modulus);
            } else {
                // r0 = OptimizedMultiplier.fastMultiply(r0, r1).mod(modulus);
                r0 = tmp0;
                r1 = tmp2;
                // r0 = r0.multiply(r1).mod(modulus);
                // r1 = r1.multiply(r1).mod(modulus);
            }
        }
        return r0;
    }
    
    //top-level modPow method: inline the custom implementation of fastMultiply
    public static BigInteger modPow2_unsafe(BigInteger base, BigInteger exponent, BigInteger modulus, int width) {
        BigInteger r0 = BigInteger.valueOf(1);
        BigInteger r1 = base;
//        int width = exponent.bitLength(); // use width parameter because bitlength is wrong for 0
        for (int i = 0; i < width; i++) {
            if (!exponent.testBit(width - i - 1)) {
                  r1 = fastMultiply_inline(r0, r1).mod(modulus);
                  r0 = r0.multiply(r0).mod(modulus);
            } else {
                  r0 = fastMultiply_inline(r0, r1).mod(modulus);
                  r1 = r1.multiply(r1).mod(modulus);
            }
        }
        return r0;
    }
    
    //fastMultiply method: inlined standardMultiply implementation
    public static BigInteger fastMultiply_inline(BigInteger x, BigInteger y) {
        int xLen = x.bitLength();
        int yLen = y.bitLength();
        if (x.equals(BigInteger.ONE)) {
            return y;
        }
        if (y.equals(BigInteger.ONE)) {
            return x;
        }
        BigInteger ret = BigInteger.ZERO;
        int N = Math.max(xLen, yLen);
        int conditionObj0 = 800;
        int conditionObj1 = 32;
        if (N <= conditionObj0) {
            ret = x.multiply(y);
        } else if (Math.abs(xLen - yLen) >= conditionObj1) {
            ret = BigInteger.ZERO;
            for (int i = 0; i < y.bitLength(); i++) {
                if (y.testBit(i)) {
                    ret = ret.add(x.shiftLeft(i));
                }
            }
        } else {
            //Number of bits/2 rounding up
            N = (N / 2) + (N % 2);
            // x = a + 2^N*b, y = c + 2^N*d
            BigInteger b = x.shiftRight(N);
            BigInteger a = x.subtract(b.shiftLeft(N));
            BigInteger d = y.shiftRight(N);
            BigInteger c = y.subtract(d.shiftLeft(N));
            // Compute intermediate values
            BigInteger ac = fastMultiply_1(a, c);
            BigInteger bd = fastMultiply_1(b, d);
            BigInteger crossterms = fastMultiply_1(a.add(b), c.add(d));
            ret = ac.add(crossterms.subtract(ac).subtract(bd).shiftLeft(N)).add(bd.shiftLeft(2 * N));
        }
        return ret;
    }
    
    //fastMultiply method: replace standardMuliply call with BigInteger libarary implementation of multiply
    public static BigInteger fastMultiply_1(BigInteger x, BigInteger y) {
        int xLen = x.bitLength();
        int yLen = y.bitLength();
        if (x.equals(BigInteger.ONE)) {
            return y;
        }
        if (y.equals(BigInteger.ONE)) {
            return x;
        }
        BigInteger ret = BigInteger.ZERO;
        int N = Math.max(xLen, yLen);
        int conditionObj0 = 800;
        int conditionObj1 = 32;
        if (N <= conditionObj0) {
            ret = x.multiply(y);
        } else if (Math.abs(xLen - yLen) >= conditionObj1) {
            //ret = standardMultiply(x, y);
            ret = x.multiply(y);
        } else {
            //Number of bits/2 rounding up
            N = (N / 2) + (N % 2);
            // x = a + 2^N*b, y = c + 2^N*d
            BigInteger b = x.shiftRight(N);
            BigInteger a = x.subtract(b.shiftLeft(N));
            BigInteger d = y.shiftRight(N);
            BigInteger c = y.subtract(d.shiftLeft(N));
            // Compute intermediate values
            BigInteger ac = fastMultiply_1(a, c);
            BigInteger bd = fastMultiply_1(b, d);
            BigInteger crossterms = fastMultiply_1(a.add(b), c.add(d));
            ret = ac.add(crossterms.subtract(ac).subtract(bd).shiftLeft(N)).add(bd.shiftLeft(2 * N));
        }
        return ret;
    }
    
    //standardMultiply method
    public static BigInteger standardMultiply(BigInteger x, BigInteger y) {
        BigInteger ret = BigInteger.ZERO;
        for (int i = 0; i < y.bitLength(); i++) {
            if (y.testBit(i)) {
                ret = ret.add(x.shiftLeft(i));
            }
        }
        return ret;
    }

   
//    public static void main(String[] args) {
//    	//base, exponent, modulus
//    	Random rnd = new Random();
//    	BigInteger base = new BigInteger(800, rnd);
//    	System.out.println(base + ":" + base.bitLength());
//    	BigInteger result1 = ModPow2.modPow2_unsafe(base, BigInteger.valueOf(2), BigInteger.valueOf(23) );
//    System.out.println("result1: " + result1);
//
//    }
}
