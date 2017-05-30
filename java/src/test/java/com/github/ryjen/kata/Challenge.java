package com.github.ryjen.kata;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Stack;

/**
 * Created by ryanjennings on 2017-05-29.
 */
public class Challenge {

    public static class Node<T> {
        public T data;
        public Node<T> next;
        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public static <T> Node<T> createList(T... values) {
        if (values == null) {
            return null;
        }
        int i = 0;
        Node<T> head = new Node(values[i]);
        Node<T> node = head;
        while(++i < values.length) {
            node.next = new Node(values[i]);
            node = node.next;
        }
        return head;
    }

    public static <T extends Comparable<T>> Node<T> partition(Node<T> node, T value) {
        Node<T> head = node;
        Node<T> tail = node;

        while (node != null) {
            Node<T> next = node.next;
            if (node.data.compareTo(value) < 0) {
                node.next = head;
                head = node;
            } else {
                tail.next = node;
                tail = node;
            }
            node = next;
        }
        tail.next = null;
        return head;
    }

    public static <T> boolean isPalindrome(Node<T> head) {
        Stack<T> stack = new Stack<>();
        Node<T> slow = head;
        Node<T> fast = head;

        while(fast != null && fast.next != null) {
            stack.push(slow.data);
            slow = slow.next;
            fast = fast.next.next;
        }

        if (fast != null) {
            slow = slow.next;
        }

        while(slow != null) {
            T prev = stack.pop();
            if (prev != slow.data) {
                return false;
            }
            slow = slow.next;
        }
        return true;
    }

    public static <T> void removeDuplicates(Node<T> node) {
        HashSet<T> values = new HashSet<>();

        Node<T> previous = null;

        while (node != null) {
            if (!values.contains(node.data)) {
                values.add(node.data);
                previous = node;
            } else {
                previous.next = node.next;
                // TODO: recycle node?
            }
            node = node.next;
        }
    }

    public static <T> Node<T> findKtoLast(Node<T> node, int k) {
        Node<T> p1 = node;
        Node<T> p2 = node;

        for(int i = 0; i < k; i++) {
            p1 = p1.next;
        }

        while(p1 != null && p2 != null) {
            p1 = p1.next;
            p2 = p2.next;
        }

        return p2;
    }

    @Test
    public void testIsPalindrome()
    {
        Node<Integer> head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(2);
        head.next.next.next.next = new Node(1);

        Assert.assertTrue(isPalindrome(head));
    }

    @Test
    public void testIsNotPalindrome()
    {
        Node<Integer> head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(2);

        Assert.assertFalse(isPalindrome(head));
    }

    @Test
    public void testPartition()
    {
        Node<Integer> head = createList(0,3,5,10,1,8,5,8,4);

        Node<Integer> result = partition(head, 5);

        Node<Integer> expected = createList(4,1,3,0,5,10,8,5,8);

        while(result != null && expected != null) {
            Assert.assertEquals(expected.data, result.data);

            result = result.next;
            expected = expected.next;
        }
    }

    @Test
    public void testRemoveDuplicates()
    {
        Node<Integer> result = createList(0,4,3,2,3,5);

        removeDuplicates(result);

        Node<Integer> expected = createList(0,4,3,2,5);

        while(result != null && expected != null) {
            Assert.assertEquals(expected.data, result.data);

            result = result.next;
            expected = expected.next;
        }
    }

    @Test
    public void testFindKToLast()
    {
        Node<Integer> head = createList(0,4,3,5,7,6);

        Node<Integer> result = findKtoLast(head, 3);

        Assert.assertEquals(new Integer(5), result.data);
    }
}
