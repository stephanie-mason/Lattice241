/*
* Lattice.java
*
* Defines a new "Lattice" type, which is a directed acyclic graph that
* compactly represents a very large space of speech recognition hypotheses
*
* Note that the Lattice type is immutable: after the fields are initialized
* in the constructor, they cannot be modified.
*
* Students may only use functionality provided in the packages
*     java.lang
*     java.util
*     java.io
*
* as well as the class java.math.BigInteger
*
* Use of any additional Java Class Library components is not permitted
*
* Stephanie Mason
*
*/

public class Lattice {
  private String utteranceID;       // A unique ID for the sentence
  private int startIdx, endIdx;     // Indices of the special start and end tokens
  private int numNodes, numEdges;   // The number of nodes and edges, respectively
  private Edge[][] adjMatrix;       // Adjacency matrix representing the lattice
  //   Two dimensional array of Edge objects
  //   adjMatrix[i][j] == null means no edge (i,j)
  private double[] nodeTimes;       // Stores the timestamp for each node

  // Constructor

  // Lattice
  // Preconditions:
  //     - latticeFilename contains the path of a valid lattice file
  // Post-conditions
  //     - Field id is set to the lattice's ID
  //     - Field startIdx contains the node number for the start node
  //     - Field endIdx contains the node number for the end node
  //     - Field numNodes contains the number of nodes in the lattice
  //     - Field numEdges contains the number of edges in the lattice
  //     - Field adjMatrix encodes the edges in the lattice:
  //        If an edge exists from node i to node j, adjMatrix[i][j] contains
  //        the address of an Edge object, which itself contains
  //           1) The edge's label (word)
  //           2) The edge's acoustic model score (amScore)
  //           3) The edge's language model score (lmScore)
  //        If no edge exists from node i to node j, adjMatrix[i][j] == null
  //     - Field nodeTimes is allocated and populated with the timestamps for each node
  // Notes:
  //     - If you encounter a FileNotFoundException, print to standard error
  //         "Error: Unable to open file " + latticeFilename
  //       and exit with status (return code) 1
  //     - If you encounter a NoSuchElementException, print to standard error
  //         "Error: Not able to parse file " + latticeFilename
  //       and exit with status (return code) 2
  public Lattice(String latticeFilename) {
    System.out.println("");
    System.out.println("-----------------------------------------------------");
    System.out.println("STEPHANIE STUFF STARTS");
    System.out.println("-----------------------------------------------------");
    System.out.println("latticeFileName is " + latticeFilename);

    java.util.Scanner input = null;
    try {
      input = new java.util.Scanner(new java.io.File(latticeFilename));
    } catch( java.io.FileNotFoundException e ) {
      System.err.println("Error: Unable to open file " + latticeFilename);
      System.exit(1);
    }
    catch( java.util.NoSuchElementException e ) {
      System.err.println("Error: Not able to parse file " + latticeFilename);
      System.exit(2);
    }

    System.out.println("File opened successfully! ");

    //get the id
    input.next(); //"id"
    this.utteranceID = input.next();
    System.out.println("ID: " + utteranceID);


    input.next(); //"start"
    this.startIdx = input.nextInt();
    System.out.println("start: " + startIdx);

    input.next(); //"end"
    this.endIdx = input.nextInt();
    System.out.println("end: " + endIdx);

    input.next(); //"numNodes"
    this.numNodes = input.nextInt();
    System.out.println("numNodes: " + numNodes);

    input.next(); //"numEdges"
    this.numEdges = input.nextInt();
    System.out.println("numEdges: " + numEdges);

    this.nodeTimes = new double[numNodes];

    for (int i = 0; i < numNodes; i++ ){
      input.next(); //"node"
      int node = input.nextInt();
      double timeStamp = input.nextDouble();
      nodeTimes[node] = timeStamp;
      System.out.println("Timestamp for node " + node + ": " + nodeTimes[node]);
    }

    this.adjMatrix = new Edge[numNodes][numNodes];
    //adjMatrix[0][0] = new Edge("label", 1, 7);

    for (int i = 0; i < numEdges; i++) {
      input.next(); //"edge"
      int startNode = input.nextInt();
      int endNode = input.nextInt();
      String label = input.next();
      int amScore = input.nextInt();
      int lmScore = input.nextInt();

      adjMatrix[startNode][endNode] = new Edge(label, amScore, lmScore);
    }

    //Table
    System.out.println();
    System.out.println("All Edges:" + numNodes);
    for(int i = 0; i < numNodes; i++) {
      for (int j = 0; j < numNodes; j++) {
        if (adjMatrix[i][j] != null) {
          System.out.println("adjMatrix[" + i + "][" + j + "]: " + adjMatrix[i][j].getLabel() + " " + adjMatrix[i][j].getAmScore() + " " + adjMatrix[i][j].getLmScore());
        }
      }
    }


    //END TESTING

    return;
  }

  // Accessors

  // getUtteranceID
  // Pre-conditions:
  //    - None
  // Post-conditions:
  //    - Returns the utterance ID
  public String getUtteranceID() {
    return this.utteranceID;
  }

  // getNumNodes
  // Pre-conditions:
  //    - None
  // Post-conditions:
  //    - Returns the number of nodes in the lattice
  public int getNumNodes() {
    return numNodes;
  }

  // getNumEdges
  // Pre-conditions:
  //    - None
  // Post-conditions:
  //    - Returns the number of edges in the lattice
  public int getNumEdges() {
    return numEdges;
  }

  // toString
  // Pre-conditions:
  //    - None
  // Post-conditions:
  //    - Constructs and returns a string describing the lattice in the same
  //      format as the input files.  Nodes should be sorted ascending by node
  //      index, edges should be sorted primarily by start node index, and
  //      secondarily by end node index
  // Notes:
  //    - Do not store the input string verbatim: reconstruct it on they fly
  //      from the class's fields
  //    - toString simply returns a string, it should not print anything itself
  // Hints:
  //    - You can use the String.format method to print a floating point value
  //      to two decimal places
  //    - A StringBuilder is asymptotically more efficient for accumulating a
  //      String than repeated concatenation
  public String toString() {
    return "";
  }

  // decode
  // Pre-conditions:
  //    - lmScale specifies how much lmScore should be weighted
  //        the overall weight for an edge is amScore + lmScale * lmScore
  // Post-conditions:
  //    - A new Hypothesis object is returned that contains the shortest path
  //      (aka most probable path) from the startIdx to the endIdx
  // Hints:
  //    - You can create a new empty Hypothesis object and then
  //      repeatedly call Hypothesis's addWord method to add the words and
  //      weights, but this needs to be done in order (first to last word)
  //      Backtracking will give you words in reverse order.
  //    - java.lang.Double.POSITIVE_INFINITY represents positive infinity
  // Notes:
  //    - It is okay if this algorithm has time complexity O(V^2)
  public Hypothesis decode(double lmScale) {
    return new Hypothesis();
  }

  // topologicalSort
  // Pre-conditions:
  //    - None
  // Post-conditions:
  //    - A new int[] is returned with a topological sort of the nodes
  //      For example, the 0'th element of the returned array has no
  //      incoming edges.  More generally, the node in the i'th element
  //      has no incoming edges from nodes in the i+1'th or later elements
  public int[] topologicalSort() {
    return null;
  }

  // countAllPaths
  // Pre-conditions:
  //    - None
  // Post-conditions:
  //    - Returns the total number of distinct paths from startIdx to endIdx
  //       (do not count other subpaths)
  // Hints:
  //    - The straightforward recursive traversal is prohibitively slow
  //    - This can be solved efficiently using something similar to the
  //        shortest path algorithm used in decode
  //        Instead of min'ing scores over the incoming edges, you'll want to
  //        do some other operation...
  public java.math.BigInteger countAllPaths() {
    return null;
  }

  // getLatticeDensity
  // Pre-conditions:
  //    - None
  // Post-conditions:
  //    - Returns the lattice density, which is defined to be:
  //      (# of non -silence- words in lattice) / (# seconds from start to end index)
  //      Note that multiwords (e.g. to_the) count as a single non-silence word
  public double getLatticeDensity() {
    return 0.0;
  }

  // writeAsDot - write lattice in dot format
  // Pre-conditions:
  //    - dotFilename is the name of the intended output file
  // Post-conditions:
  //    - The lattice is written in the specified dot format to dotFilename
  // Notes:
  //    - See the assignment description for the exact formatting to use
  //    - For context on the dot format, see
  //        - http://en.wikipedia.org/wiki/DOT_%28graph_description_language%29
  //        - http://www.graphviz.org/pdf/dotguide.pdf
  public void writeAsDot(String dotFilename) {
    return;
  }

  // saveAsFile - write in the simplified lattice format (same as input format)
  // Pre-conditions:
  //    - latticeOutputFilename is the name of the intended output file
  // Post-conditions:
  //    - The lattice's toString() representation is written to the output file
  // Note:
  //    - This output file should be in the same format as the input .lattice file
  public void saveAsFile(String latticeOutputFilename) {
    return;
  }

  // uniqueWordsAtTime - find all words at a certain point in time
  // Pre-conditions:
  //    - time is the time you want to query
  // Post-conditions:
  //    - A HashSet is returned containing all unique words that overlap
  //      with the specified time
  //     (If the time is not within the time range of the lattice, the Hashset should be empty)
  public java.util.HashSet<String> uniqueWordsAtTime(double time) {
    return null;
  }

  // printSortedHits - print in sorted order all times where a given token appears
  // Pre-conditions:
  //    - word is the word (or multiword) that you want to find in the lattice
  // Post-conditions:
  //    - The midpoint (halfway between start and end time) for each instance of word
  //      in the lattice is printed to two decimal places in sorted (ascending) order
  //      All times should be printed on the same line, separated by a single space character
  //      (If no instances appear, nothing is printed)
  // Note:
  //    - java.util.Arrays.sort can be used to sort
  //    - PrintStream's format method can print numbers to two decimal places
  public void printSortedHits(String word) {
    return;
  }
}
