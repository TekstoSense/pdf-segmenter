/*******************************************************************************
 * Copyright (c) 2016, TekstoSense and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.tekstosense.segmenter.model;

/**
 * @author tekstosense
 */
public class DoublyLinkedList<T> {

    int size = 0;


    private Node first = null;
    private Node last = null;

    public Node getFirst() {
        return first;
    }

    public Node getLast() {
        return last;
    }

    public void addToFirst(T data) {
        Node newNode = new Node(data);

        if (isEmpty()) {
            newNode.setNext(null);
            newNode.setPrev(null);
            first = newNode;
            last = newNode;
            this.size++;
        } else {
            first.setPrev(newNode);
            newNode.setNext(first);
            newNode.setPrev(null);
            first = newNode;
            this.size++;
        }
    }

    public void addToLast(T data) {
        Node newNode = new Node(data);

        if (isEmpty()) {
            newNode.setNext(null);
            newNode.setPrev(null);
            first = newNode;
            last = newNode;
            this.size++;
        } else {
            last.setNext(newNode);
            newNode.setNext(null);
            newNode.setPrev(last);
            last = newNode;
            this.size++;
        }
    }

    public boolean isEmpty() {
        return (first == null);
    }

    public void displayList() {
        Node current = first;
        while (current != null) {
            current.displayNode();
            current = current.getNext();
        }
        System.out.println();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node current = first;
        while (current != null) {
            builder.append(current.toString());
            current = current.getNext();
        }

        return builder.toString();
    }

    public int getSize() {
        return this.size;
    }


    public void removeFirstNode() {
        if (!isEmpty()) {
            Node temp = first;

            if (first.getNext() == null) {
                first = null;
                last = null;
            } else {
                first = first.getNext();
                first.setPrev(null);
            }
        }
    }

    public void removeLastNode() {
        Node temp = last;

        if (!isEmpty()) {

            if (first.getNext() == null) {
                first = null;
                last = null;
            } else {
                last = last.getPrev();
                last.setNext(null);
            }
        }
    }
}
