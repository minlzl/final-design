package com.example.finalend.leetcode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

public class FrontMiddleBackQueue {

    Deque<Integer> left = new ArrayDeque<>();
    Deque<Integer> right = new ArrayDeque<>();

    private void balanced() {
        if (left.size() > right.size()) {
            right.addFirst(left.pollLast());
        } else if (right.size() > left.size() + 1) {
            left.addLast(right.pollFirst());
        }
    }

    public FrontMiddleBackQueue() {

    }

    public void pushFront(int val) {
        left.addFirst(val);
        balanced();
    }

    public void pushMiddle(int val) {
        if (left.size() < right.size()) {
            left.addLast(val);
        } else {
            right.addFirst(val);
        }
    }

    public void pushBack(int val) {
        right.addLast(val);
        balanced();
    }

    public int popFront() {
        if (right.isEmpty()) {
            return -1;
        }
        int val = left.isEmpty() ? right.pollFirst() : left.pollFirst();
        balanced();
        return val;
    }

    public int popMiddle() {
        if (right.isEmpty()) {
            return -1;
        }
        if (left.size() == right.size()) {
            return left.pollLast();
        }
        return right.pollFirst();
    }

    public int popBack() {
        if (right.isEmpty()) {
            return -1;
        }
        int val = right.pollLast();
        balanced();
        return val;
    }
}
