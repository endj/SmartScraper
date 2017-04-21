package se.edinjakupovic.mobilescraper;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Trie implementation for faster weooord lookup
 */

class Trie{
    private Node root;

    Trie(){
        this.root = new Node();
    }
    void put(String word){
        HashMap<Character,Node> children = root.children;

        for(int i=0;i<word.length();i++){
            char c = word.charAt(i);
            Node currentNode;

            if(children.containsKey(c)){ // checks if current node has next char
                currentNode = children.get(c); // up
            }else{
                currentNode = new Node(c); // if it does not, insert current char
                children.put(c,currentNode);
            }
            children = currentNode.children; // Check next node map

            if(i==word.length()-1){ // at the last char indicate end of word
                currentNode.endsWord = true;
            }

        }
    }

    boolean search(String word){
        Node currentNode = searchNode(word);
        return currentNode != null && currentNode.endsWord;
    }


    /*
    * Traverses and updates currentNode until end of word
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

class Node{
    private char character;
    HashMap<Character,Node> children = new HashMap<>();
    boolean endsWord;

    Node(){}

    Node(char character){
        this.character = character;
    }

}
