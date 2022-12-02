package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */ 
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */ 
    public void makeSortedList() {
        StdIn.setFile(fileName);

	/* Your code goes here */
    
    // initialize the array list
    sortedCharFreqList = new ArrayList<CharFreq>();

    // create an array to keep track of occurences, and a char to keep track of what char we are reading/referring to
    int[] charTracker = new int[128];
    char c = 0;

    // read through the file character by character, store the number of occurences of that character in an array
    while(StdIn.hasNextChar()){
        c = StdIn.readChar();
        charTracker[c]++;
    }

    // calculate the total number characters
    int totalChars = 0;
    for(int i = 0; i < charTracker.length; i++){
        if(charTracker[i] != 0){
            totalChars += charTracker[i];
        }
    }

    // Iterate through the array and do multiple things:
    for(int j = 0; j < charTracker.length; j++){
        if(charTracker[j] != 0){ // if there is a freq of character j greater than one
            // create a CharFreq object
            CharFreq curr = new CharFreq();

            // assign the current character to this object
            char x = (char) j;
            curr.setCharacter(x);

            // calculate the probOcc of this character and assign it to this object
            double freq = (double) charTracker[j] / (double) totalChars;
            curr.setProbOcc(freq);

            // add this CharFreq object to the sortedCharFreqList
            sortedCharFreqList.add(curr);
        }
        
    }

    // edge case: 1 type of char in entire file: 

    int countForOneChar = 0;
    char holder = 0;
    for(int i = 0; i < charTracker.length; i++){
        if(charTracker[i] != 0){
            countForOneChar++;
            holder = (char) (i + 1);
        }
    }

    if(countForOneChar == 1){ // we have one char
        CharFreq empty = new CharFreq();
        if(holder == 128){
            char a = (char) 0;
            empty.setCharacter(a);
        } else {
            empty.setCharacter(holder);
        }
        empty.setProbOcc(0);
        sortedCharFreqList.add(empty);
    }
    
    Collections.sort(sortedCharFreqList);

    }

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    public void makeTree() {

	/* Your code goes here */

        Queue<TreeNode> source = new Queue<TreeNode>();
        Queue<TreeNode> target = new Queue<TreeNode>();

        for(int i = 0; i < sortedCharFreqList.size(); i++){
            TreeNode t = new TreeNode();
            t.setData(sortedCharFreqList.get(i));
            source.enqueue(t);
        }

        TreeNode holderOne = null;
        TreeNode holderTwo = null;

        while(!source.isEmpty() || target.size() != 1){
            holderOne = find(source, target);
            holderTwo = find(source, target);
            target.enqueue(createNode(holderOne, holderTwo));
        }

        if(source.isEmpty() && target.size() == 2){
            holderOne = target.dequeue();
            holderTwo = target.dequeue();
            target.enqueue(createNode(holderOne, holderTwo));
        }

        huffmanRoot = target.peek();
    }

    private TreeNode find(Queue<TreeNode> source, Queue<TreeNode> target){
        // purpose of this method: given two queues, return the smallest node
        TreeNode holder = null;
        if(target.isEmpty() && !source.isEmpty()){ // if the target is empty and the source is not empty
            holder = source.dequeue(); // dequeue from the source (it's our only option)
        } else if (!source.isEmpty() && !target.isEmpty()) { // target NOT empty, source NOT empty
            if(source.peek().getData().getProbOcc() <= target.peek().getData().getProbOcc()){
                holder = source.dequeue();
            } else {
                holder = target.dequeue();
            }
        } else if(!target.isEmpty() && source.size() == 0){ // target NOT empty, source empty
            holder = target.dequeue();
        }

        return holder;
    }

    private TreeNode createNode(TreeNode one, TreeNode two){
        // purpose of this method: given two TreeNodes one and two, create a new node with 1.) freq = sum of probOcc of each given node and 2.) first node is left child of newNode and second node is right child of newNode
        double sum = one.getData().getProbOcc() + two.getData().getProbOcc();
        CharFreq newCharFreq = new CharFreq(null, sum);
        TreeNode newNode = new TreeNode(newCharFreq, one, two);
        return newNode;
    }
    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() {

	/* Your code goes here */

        // create an array to store the binary strings
        String[] arr = new String[128];
        String s = new String();
        String parent = new String();
        encodings = inOrder(huffmanRoot, arr, s, parent);

    }

    private String[] inOrder(TreeNode n, String[] arr, String s, String parent){
        // String reset = new String();  
        if(n == null){ // if the node is null, have "reset" s to be the parent strand (the strand before we went to this node)
            s = parent;
            return arr;
        }

        // do something with left
        if(n.getLeft() != null){
            parent = s;
            s = s + "0";  
        }
        inOrder(n.getLeft(), arr, s, parent);
        

        // do something with self
        if(n.getData().getCharacter() != null){ // if we are at a node that has a character
            arr[n.getData().getCharacter()] = s;
        } else { // if we are at a node that DOES NOT have a character
            s = parent;
        }
            
        // do something with right
        if(n.getRight() != null){
            parent = s;
            s = s + "1";
        }
        inOrder(n.getRight(), arr, s, parent);

        return arr;

    }

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);

	/* Your code goes here */

        String s = new String(); 
        char c = 0;
        while(StdIn.hasNextChar()){ // while there is still a char to read
            c = StdIn.readChar(); // store the char in c
            s = s + encodings[c]; // store the binary string for the c
        }

        writeBitString(encodedFile, s);

    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);

	/* Your code goes here */

    String s = readBitString(encodedFile);
    
    TreeNode ptr = huffmanRoot;
    int i = 0;
    String finalString = new String();
    while(i != s.length()){
        if(s.charAt(i) == '0'){
            ptr = ptr.getLeft();
            if(ptr.getData().getCharacter() != null){
                finalString += ptr.getData().getCharacter();
                ptr = huffmanRoot;
            }
        } else {
            ptr = ptr.getRight();
            if(ptr.getData().getCharacter() != null){
                finalString += ptr.getData().getCharacter();
                ptr = huffmanRoot;
            }
        }
        i++;
    }
    
    StdOut.print(finalString);
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
