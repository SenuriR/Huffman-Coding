# Huffman-Coding
[CS112 - Data Structures] Compress data files using Huffman Coding

OVERVIEW OF FILES:
CharFreq class, which houses a Character object “character” representing a certain ASCII character, and a double “probOcc” representing its probability of occurrence (value between 0 and 1 showing its frequency). These objects are implemented to compare primarily by probOcc, then by character if those are equal. Note that “character” can be null. Getters and setters are provided. Do not edit this class.

Queue class, which functions as a simple generic queue. It implements isEmpty(), size(), enqueue(), dequeue(), and peek(). Do not edit this class.
TreeNode class, which houses a CharFreq object “data” representing a certain character and its frequency, and TreeNodes “left” and “right” representing the left and right subtrees of the binary tree. Getters and setters are provided. Do not edit this class.

Driver class, which you can run to test any of your methods interactively. Feel free to edit this class, as it is provided only to help you test. It is not submitted and it is not used to grade your code.

StdIn and StdOut, which are used by the driver, provided methods, and some of your implemented methods as well. Do not edit these classes.

HuffmanCoding class, which contains some provided methods in addition to annotated method signatures for all the methods you are expected to fill in. You will write your solutions in this file, and it is the file which will be submitted for grading. It contains instance variables fileName, sortedCharFreqList, huffmanRoot and encodings, which must be set by your methods.

Multiple text files which contain input data, and can be read by the driver as test cases. These files, as well as the files used for grading are guaranteed to be ASCII only. Feel free to edit them or even make new ones to help test your code. They are not submitted.
