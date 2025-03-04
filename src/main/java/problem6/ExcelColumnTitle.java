package problem6;

public class ExcelColumnTitle {
    public static String convertToTitle(int columnNumber) {
        StringBuilder result = new StringBuilder();

        while (columnNumber > 0) {
            columnNumber--; // Adjust to 0-based index
            char letter = (char) ('A' + columnNumber % 26); // Find the corresponding letter
            result.insert(0, letter); // Prepend to result
            columnNumber /= 26; // Move to the next position
        }

        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(convertToTitle(1));    // Output: A
        System.out.println(convertToTitle(28));   // Output: AB
        System.out.println(convertToTitle(701));  // Output: ZY
        System.out.println(convertToTitle(2147483647)); // Large input test
    }
}
