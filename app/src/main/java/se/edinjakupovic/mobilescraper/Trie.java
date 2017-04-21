package se.edinjakupovic.mobilescraper;



import java.util.HashMap;
import java.util.Map;

/**
 * Trie.java A Trie implementation for quicly looking up words
 * Only has functionality for adding new words and checking if they
 * exist in a tree.
 *
 * @author Edin Jakupovic
 * @version 1.0
 *
 *
 */

class Trie{
    private Node root;

    Trie(){
        this.root = new Node();
    }


    /**
    * Puts a new word in the Trie. When
    * the last character is reached endsWord
    * is set to true.
    *
    * @param word A String to put in
    * */
    void put(String word){
        HashMap<Character,Node> children = root.children;

        for(int i=0;i<word.length();i++){
            char c = word.charAt(i);
            Node currentNode;

            if(children.containsKey(c)){
                currentNode = children.get(c);
            }else{
                currentNode = new Node(c);
                children.put(c,currentNode);
            }
            children = currentNode.children;

            if(i==word.length()-1){
                currentNode.endsWord = true;
            }

        }
    }


    /**
    * Checks if a String is in the Trie
    * @param word String to search for
    * @return currentNode  Returns true if the word
    * is in the Trie else false.
    * */
    boolean search(String word){
        Node currentNode = searchNode(word);
        return currentNode != null && currentNode.endsWord;
    }


    /**
    * Traverses and updates currentNode until end of word
    * If the word exist, returns the last Node
    * @param str String to search for
    * @return currentNode Returns last node found
    * */
    private Node searchNode(String str){
        Map<Character,Node> children = root.children;
        Node currentNode = new Node();
        for(int i=0;i<str.length();i++){
            char c = str.charAt(i);
            if(children.containsKey(c)){
                currentNode = children.get(c);
                children = currentNode.children;
            }else{
                return null;
            }
        }
        return currentNode;
    }




}
/**
* Node An class representing a node in a Trie
* Uses a Hashmap for nextChar instead of array
* for larger ascii coverage
* */
class Node{
    private char character;
    HashMap<Character,Node> children = new HashMap<>();
    boolean endsWord;

    Node(){}

    Node(char character){
        this.character = character;
    }

}
