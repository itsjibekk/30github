package problem3;

public class Solution {
    public static int removeDuplicates(int[] nums) {

        if (nums.length == 0) return 0; // Edge case: empty array

        int i = 0; // Pointer for unique elements
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) { // Found a new unique element
                i++;
                nums[i] = nums[j]; // Move it to the next unique position
            }
        }
        return i + 1;
    }
    public static void main(String[] args) {
        System.out.println(removeDuplicates(new int[]{1,1,2,2})); // Output: "10101"
    }
}