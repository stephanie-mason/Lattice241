/* 
 * Hypothesis.java
 *
 * Defines a new "Hypothesis" type, which is a path that represents
 * a hypothesized utterance, obtained by searching a lattice
 *
 * This class has already been implemented for you.
 * 
 * Brian Hutchinson
 * January 2016
 *
 */

public class Hypothesis {
    private double pathScore;                  // Stores the cumulative path score
    private java.util.ArrayList<String> words; // Array of words in the path

    // Constructor

    // Hypothesis
    // Preconditions:
    //     - None
    // Post-conditions
    //     - this.words points to a new, empty, ArrayList object
    //     - this.pathScore == 0
    public Hypothesis() {
        words = new java.util.ArrayList<String>();
    }

    // Mutator/Modifier

    // addWord
    // Preconditions:
    //     - word is another word to add to (the end of) the path
    //     - combinedScore is the weight on the corresponding edge
    //       which is a weighted sum of the amScore and lmScore
    // Post-conditions
    //     - If word equals "-silence-" then
    //            words is NOT modified: -silence- words are not included
    //            the combinedScore IS added to the pathScore: its weight counts
    //     - If word does not equal "-silence-" but DOES contain an underscore
    //            word is split into individual words at the underscore(s)
    //            each individual word is added, in sequence, to words
    //            For example, if word equals "going_to" then "going" is first
    //            added to the end of words, and then "to" is added to the end
    //            of words, so that words is now two longer 
    //            the combinedScore is added (once) to the pathScore
    //     - If word is not "-silence-" and DOES NOT contain an underscore
    //            word is added to the end of words, so that words is one longer
    //            the combinedScore is added to the pathScore
    // Hints:
    //     - To split a word into individual words, see String's split method
    public void addWord(String word, double combinedScore) {
        pathScore += combinedScore;
        if( !word.equals("-silence-") ) {
            String[] parts = word.split("_");
            for( int i=0; i<parts.length; i++ ) {
                words.add(parts[i]);
            }
        }
    }

    // Accessors

    // getPathScore
    // Preconditions:
    //     - None
    // Post-conditions
    //     - this.pathScore is returned
    public double getPathScore() {
        return this.pathScore;
    }

    // getHypothesisString
    // Preconditions:
    //     - The hypothesis has already been created via calls to addWord
    // Post-conditions
    //     - A single string representation of the hypothesis sequence
    //       is returned, obtained by concatenating the individual words
    //       in the hypothesis (with spaces in-between)
    public String getHypothesisString() {
        String result = "";
        for( int i=0; i<words.size(); i++ ) {
            result += words.get(i) + " ";
        }    
        return result;
    }

    // computeWER
    // Preconditions:
    //     - referenceFilename is the name of a file with the reference transcript
    //     - The hypothesis has already been created via calls to addWord
    // Post-conditions
    //         - The word error rate (WER) of the hypothesis with respect to 
    //        the reference transcript is returned.
    //      - WER first requires you to compute the minimum edit distance between
    //        the hypothesis and reference word sequences.  Given that, WER
    //          is simply the minimum edit distance divided by the number of words
    //        in the reference sequence
    public double computeWER(String referenceFilename) {
        java.util.Scanner input = null;
        try {
            input = new java.util.Scanner(new java.io.File(referenceFilename));
        } catch( java.io.FileNotFoundException e ) {
            System.out.println("Error: File " + referenceFilename + " not found");
            System.exit(1);
        }

        java.util.ArrayList<String> reference = new java.util.ArrayList<String>();
        while( input.hasNext() ) {
            reference.add(input.next());
        }
        
        double[][] d = new double[words.size()+1][reference.size()+1];
        for( int i=0; i<=words.size(); i++ ) {
            d[i][0] = i;
        }
        for( int j=0; j<=reference.size(); j++ ) {
            d[0][j] = j;
        }
        for( int j=1; j<=reference.size(); j++ ) {
            for( int i=1; i<=words.size(); i++ ) {
                if( words.get(i-1).equals(reference.get(j-1)) ) {
                    d[i][j] = d[i-1][j-1];
                } else {
                    d[i][j] = 1+min3(d[i-1][j],d[i][j-1],d[i-1][j-1]);
                }
            }
        }

        return d[words.size()][reference.size()]/reference.size();
    }

    // min3
    // Preconditions:
    //     - a, b and c are values to be compared
    // Post-conditions
    //     - The smallest value out of a, b and c is returned
    private double min3(double a, double b, double c) {
        if( a<b && a<c ) {
            return a;
        } else if( b<a && b<c ) {
            return b;
        } else {
            return c;
        }
    }
}
