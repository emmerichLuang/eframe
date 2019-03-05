package com.base.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 链表节点
 * see:https://stackoverflow.com/questions/18654698/implement-linked-list-in-java
 * @Date 2019年3月5日
 * @author E.E.
 *
 * @param <T>
 */
public class Node<T> {

	private T value;
	
	private Node<T> next;
	
	public Node(T value) {
        this.value = value;
    }
	
	public static <T> Node<T> createLinkedListFromArray(T... array) {

        if (checkIfArrayIsNullOrEmpty(array)){
        	return new Node<T>(null);
        }

        Node<T> head = new Node<T>(array[0]);

        createLinkedList(array, head);

        return head;
    }
	
	private static <T> boolean checkIfArrayIsNullOrEmpty(T[] array) {
        return array == null || array.length == 0;
    }
	
	private static <T> void createLinkedList(T[] array, Node<T> head) {

        Node<T> node = head;

        for (int index = 1; index < array.length; index++) {
            T t = array[index];
            node.setNext(new Node<T>(t));
            node = node.getNext();
        }
    }
	
	public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;
        return value != null && value.equals(node.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public String toString() {
		List ret = createList();
        return Arrays.toString(ret.toArray());
    }

    
    @SuppressWarnings({"rawtypes", "unchecked"})
	private List createList() {
        Node root = this;
        List ret = new ArrayList();
        while (root != null) {
            ret.add(root.getValue());
            root = root.getNext();
        }
        return ret;
    }
}
