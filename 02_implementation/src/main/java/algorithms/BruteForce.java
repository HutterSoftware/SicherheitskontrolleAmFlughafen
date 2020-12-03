package algorithms;

import algorithms.IStringMatching;

public class BruteForce implements IStringMatching {
    public static void main(String... args) {
        BruteForce bruteForce = new BruteForce();
        String text = "1ado3cqcsov53ufyqwf9m447nozdumn9n3xgti51mahpbfyeddzdd74gavjia1wm8q3npxrlmhx5jqrkfo569vbvw6uw1ywn7azvii2phlfp33v503r3e8mq7javab82dgtb2l1fq6iq68acjwdu4xh33vmq0ggqbf8eiudsh4b0i1aqbd6p5d37ki0mubh506";

        String pattern = "java";
        System.out.println("text    : " + text);
        System.out.println("pattern : " + pattern);
        int position = bruteForce.search(text, pattern);

        if (position == -1) {
            System.out.println("pattern not found");
        } else {
            System.out.println("pattern found at position : " + position);
        }
    }

    public int search(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        int j;

        for (int i = 0; i <= (n - m); i++) {
            j = 0;

            while ((j < m) && (text.charAt(i + j) == pattern.charAt(j))) {
                j++;
            }

            if (j == m) {
                return i;
            }
        }

        return -1;
    }
}
