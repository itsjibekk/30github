package problem11;

class Solution {
    public static int lengthOfLastWord(String s) {
        s = s.trim();
        int l = 0;
        int siz = s.length() - 1;
        int i = siz;
        while(i >= 0 && !s.substring(i, i + 1).equals(" ")){
            l++;
            i--;
        }
        return l;
    }

    public static void main(String[] args) {
        System.out.println(lengthOfLastWord("wer nsj erfer"));
    }
}
