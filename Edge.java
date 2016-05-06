/* 
 * Edge.java
 *
 * Defines a new "Edge" type, which stores the information associated
 * with an edge in the lattice
 *
 * Note that the Edge type is immutable: after the fields are initialized
 * in the constructor, they cannot be modified.
 *
 * This class has been implemented for you
 * 
 * Brian Hutchinson
 * Janary 2016
 *
 */

public class Edge {
    private String label;          // The word associated with the edge
    private int amScore, lmScore;  // The acoustic and language model scores
                                   // (A speech recognizer trades off scores of
                                   //  these two models to find the best path)

    // Constructors

    // Edge
    // Preconditions:
    //     - label, amScore and lmScore contain the label and two weights
    //       associated with the edge to be constructed
    // Post-conditions
    //     - Field this.label is set to label
    //     - Field this.amScore is set to amScore
    //     - Field this.lmScore is set to lmScore
    public Edge(String label, int amScore, int lmScore) {
        this.label = label;
        this.amScore = amScore;
        this.lmScore = lmScore;
        return;
    }

    // Edge - the "copy constructor" which duplicates the content of another edge
    // Preconditions:
    //     - e is an Edge to be copied
    // Post-conditions
    //     - this.label initialized to e's label
    //     - this.lmScore initialized to e's lmScore
    //     - this.amScore initialized to e's amScore
    public Edge(Edge e) {
        this.label = e.getLabel();
        this.amScore = e.getAmScore();
        this.lmScore = e.getLmScore();
    }

    // Accessors 

    // getLabel
    // Preconditions:
    //     - None
    // Post-conditions
    //     - Return's this.label
    public String getLabel() {
        return this.label;
    }

    // getLmScore
    // Preconditions:
    //     - None
    // Post-conditions
    //     - Return's this.lmScore
    public int getLmScore() {
        return this.lmScore;
    }

    // getAmScore
    // Preconditions:
    //     - None
    // Post-conditions
    //     - Return's this.amScore
    public int getAmScore() {
        return this.amScore;
    }
    
    // getCombinedScore
    // Preconditions:
    //     - lmScale specifies how much to weight the lmScore
    // Post-conditions
    //     - Return's the weighted sum of the two scores, namely:
    //          amScore + lmScale * lmScore
    // Notes
    //     - This gives us a single weight for the edge, which is
    //       needed for finding the shortest path
    //     - lmScale == 0 means we use only the amScore, while 
    //       very large lmScale means we use (almost) only the lmScore
    public int getCombinedScore(double lmScale) {
        return this.amScore + (int)(lmScale * this.lmScore);
    }
}
