package problem8;

public class PowerOfTwo {
    public static boolean isPowerOfTwo(int n) {
        if (n <= 0) return false;  // Power of two must be positive
        while (n % 2 == 0) {
            n /= 2;
        }
        return n == 1;  // If reduced to 1, it's a power of two
    }

    public static void main(String[] args) {
        System.out.println(isPowerOfTwo(16));  // true
        System.out.println(isPowerOfTwo(18));  // false
        System.out.println(isPowerOfTwo(1));   // true
        System.out.println(isPowerOfTwo(1024));// true
    }
}

