package MerkleTreeVerify;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class is a controller that creates a new Block
 * either from a specified URL or the most recent block 
 * from the blockchain web page [ https://blockchain.info ].
 * A BlockVerify Object is then created from the Block Object.
 * The results from the BlockVerify object are then printed.
 * 
 * 
 * @author Robert Pierce
 * date: 07/17/2015
 
 */
public class verifyTester {

	public static void main(String[] args) {
		Block block = null;                 // create the variable to hold the Blook object
		
		try{
		     //block = new Block(new URL("https://blockchain.info/block/00000000000000000955561565f3a46c5e3c1c221467e43c55ea892ef2d27c8f"));  //get a specific block
			 block = new Block();               // get the most recent block
		} catch (MalformedURLException e) {     // catch a bad URL
			System.out.println("Sorry, the URL is bad ...");
			e.printStackTrace();
		}
		
		// create a new BlockVerify object
		BlockVerify blockTester = new BlockVerify(block);               
		
		// Print the results 
		System.out.println("Block #: " + blockTester.getBlockNumber()); 
		System.out.println();
		System.out.println("The Merkle Tree Verifies the block: " + blockTester.verifyRoot());
		System.out.println("The merkle root extracted from the block is: " + blockTester.getRootFromBlock());
		//System.out.println();
		System.out.println("The merkle root from the Merkle Tree is:     " + blockTester.getRootFromTree());
	}
}
