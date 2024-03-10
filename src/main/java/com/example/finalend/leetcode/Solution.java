package com.example.finalend.leetcode;


import org.checkerframework.checker.units.qual.A;

import java.util.*;

public class Solution {

    public boolean isAcronym(List<String> words, String s) {
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(word.charAt(0));
        }
        return sb.toString().equals(s);
    }
    public int[] findPeakGrid(int[][] mat) {
        int left = 0, right = mat.length - 1;
        while (left < right) {
            int mid = left + (right - left) >>> 1;
            int j = indexOf(mat[mid]);
            if (mat[mid][j] > mat[mid + 1][j]) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return new int[]{left, indexOf(mat[left])};
    }
    private int indexOf(int[] a) {
        int index = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] > a[index]) {
                index = i;
            }
        }
        return index;
    }
    public long maximumSumOfHeights(List<Integer> maxHeights) {
        int n = maxHeights.size();
        long res = 0;
        long[] prefix = new long[n];
        long[] suffix = new long[n];
        Deque<Integer> stack1 = new ArrayDeque<>();
        Deque<Integer> stack2 = new ArrayDeque<>();
        for (int i  = 0; i < n; i++) {
            while (!stack1.isEmpty() && maxHeights.get(i) < maxHeights.get(stack1.peek())) {
                stack1.pop();
            }
            if (stack1.isEmpty()) {
                prefix[i] = (long) (i + 1) * maxHeights.get(i);
            } else {
                prefix[i] = prefix[stack1.peek()] + (long) (i - stack1.peek()) * maxHeights.get(i);
            }
            stack1.push(i);
        }
        for (int i = n - 1; i >= 0; i--) {
            while (!stack2.isEmpty() && maxHeights.get(i) < maxHeights.get(stack2.peek())) {
                stack2.pop();
            }
            if (stack2.isEmpty()) {
                suffix[i] = (long) (n - i) * maxHeights.get(i);
            } else {
                suffix[i] = suffix[stack2.peek()] + (long) (stack2.peek() - i) * maxHeights.get(i);
            }
            stack2.push(i);
            res = Math.max(res, prefix[i] + suffix[i] - maxHeights.get(i));
        }
        return res;
    }
    public String getHint(String secret, String guess) {
        int bulls = 0, cows = 0;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < secret.length(); i++) {
            if (secret.charAt(i) == guess.charAt(i)) {
                bulls++;
            } else {
                map.put(Integer.parseInt(String.valueOf(secret.charAt(i))), map.getOrDefault(Integer.parseInt(String.valueOf(secret.charAt(i))), 0) + 1);
            }
        }
        for (int i = 0; i < secret.length(); i++) {
            if (secret.charAt(i) == guess.charAt(i)) {
                continue;
            }
            int key = Integer.parseInt(String.valueOf(guess.charAt(i)));
            if (map.get(key) != null && map.get(key) > 0) {
                cows++;
                map.put(key, map.get(key) - 1);
            }
        }
        return "" + bulls + "A" + cows + "B";
    }

    public static void main(String[] args) {
    }
}
