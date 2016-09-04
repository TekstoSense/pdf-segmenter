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

// TODO: Auto-generated Javadoc
/**
 * The Class LinkedNGramUtil.
 */
public class LinkedNGramUtil {


    /**
     * Builds the N gram.
     *
     * @param node the node
     * @param N the n
     * @return the node[]
     */
    public static Node<LineNode>[] buildNGram(Node<LineNode> node, int N) {
        Node<LineNode>[] grams = new Node[N];
        Node<LineNode> currentNode = node;
        if (currentNode != null) {
            for (int i = 0; i < N; i++) {
                grams[i] = currentNode;
                if(currentNode != null)
                currentNode = currentNode.getNext();
            }
        }
        return grams;
    }
}
