package autoComplete;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

/**
 * A prefix tree used for autocompletion. The root of the tree just stores links to child nodes (up to 26, one per letter).
 * Each child node represents a letter. A path from a root's child node down to a node where isWord is true represents the sequence
 * of characters in a word.
 */
public class PrefixTree {
    private TreeNode root; 

    // Number of words contained in the tree
    private int size;

    public PrefixTree(){
        root = new TreeNode();
    }

    /**
     * Adds the word to the tree where each letter in sequence is added as a node
     * If the word, is already in the tree, then this has no effect.
     * @param word
     */
    public void add(String word){
        TreeNode whereToAdd = root;
        for(int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            if(whereToAdd.children.get(c) == null){
                TreeNode newNode = new TreeNode();
                newNode.letter = c;
                whereToAdd.children.put(c, newNode);
                if(i == word.length()-1){
                    newNode.isWord = true;
                    size ++;
                }
                whereToAdd = newNode;
            }
            else{
                whereToAdd = whereToAdd.children.get(c);
                if(i == word.length()-1){
                    whereToAdd.isWord = true;

                }
            }
        }

    }

    /**
     * Checks whether the word has been added to the tree
     * @param word
     * @return true if contained in the tree.
     */
    public boolean contains(String word){
        TreeNode currentNode = root;
        for(int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            if(currentNode.children.get(c) == null){
                return false;
            }
            else{
                currentNode = currentNode.children.get(c);
            }
            if(i == word.length()-1){
                if(!currentNode.isWord){
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Finds the words in the tree that start with prefix (including prefix if it is a word itself).
     * The order of the list can be arbitrary.
     * @param prefix
     * @return list of words with prefix
     */
    public ArrayList<String> getWordsForPrefix(String prefix){
        ArrayList<String> result = new ArrayList<>();
        TreeNode node = root;
    
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (node.children.containsKey(c)) {
                node = node.children.get(c);
            } else {
                return result; 
            }
        }
        
        preOrderHelper(node, new StringBuilder(prefix), result);
        return result;
    }
    /**
     * Recursive method that adds words with correct prefix to result list.
     * @param node
     * @param prefix
     * @param result
     */
    private void preOrderHelper(TreeNode node, StringBuilder prefix, ArrayList<String> result) {
        if (node.isWord) {
            result.add(prefix.toString());
        }
        for (TreeNode treeNode : node.children.values()) {
            prefix.append(treeNode.letter);
            preOrderHelper(treeNode, prefix, result);
            prefix.deleteCharAt(prefix.length() - 1); 
        }
    }
   
    /**
     * @return the number of words in the tree
     */
    public int size(){
        return size;
    }

    
    
}
