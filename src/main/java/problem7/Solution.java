package problem7;

import java.util.HashSet;
import java.util.Set;

public class Solution {
    public static boolean isHappy(int n) {
        Set<Integer> seen = new HashSet<>();

        while (n != 1 && !seen.contains(n)) {
            seen.add(n);
            n = getSumOfSquares(n);
        }

        return n == 1; // If we exit while loop, return true if n == 1, otherwise false
    }

    // Helper method to calculate the sum of squares of digits
    private static int getSumOfSquares(int num) {
        int sum = 0;
        while (num > 0) {
            int digit = num % 10;  // Extract last digit
            sum += digit * digit;  // Square it and add to sum
            num /= 10;             // Remove last digit
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(isHappy(19));  // Output: true
        System.out.println(isHappy(2));   // Output: false
    }
}
