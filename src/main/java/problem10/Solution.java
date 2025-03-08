package problem10;

class Solution {
    public static double myPow(double x, int n) {
        if (n == Integer.MIN_VALUE) {
            x = x * x;
            n = n / 2;
        }
        if(n == 0){
            return 1.0;
        }
        if (n < 0) {
            return 1 / myPow(x, -n);
        }
        double half = myPow(x, n/2);
        if(n % 2 == 0){
            return half * half;
        }else{
            return x * half * half;
        }
    }

    public static void main(String[] args) {
        System.out.println(myPow(2.0, 10));
    }
}
