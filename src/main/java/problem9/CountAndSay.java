package problem9;

public class CountAndSay {
    public static String countAndSay(int n) {
        if (n == 1) return "1"; // Base case

        String prev = countAndSay(n - 1); // Get previous sequence
        StringBuilder result = new StringBuilder();

        int count = 1;
        for (int i = 1; i < prev.length(); i++) {
            if (prev.charAt(i) == prev.charAt(i - 1)) {
                count++; // Counting consecutive characters
            } else {
                result.append(count).append(prev.charAt(i - 1)); // Append count and character
                count = 1; // Reset count
            }
        }
        result.append(count).append(prev.charAt(prev.length() - 1)); // Append last group

        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(countAndSay(1));  // Output: "1"
        System.out.println(countAndSay(2));  // Output: "11"
        System.out.println(countAndSay(3));  // Output: "21"
        System.out.println(countAndSay(4));  // Output: "1211"
        System.out.println(countAndSay(5));  // Output: "111221"
        System.out.println(countAndSay(10)); // Larger example
    }
}

