package MerkleTreeVerify;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;



/**
 * This class is a BlockVerify Object that takes in 
 * a Block Object as a parameter and instantiates a 
 * MerkleTree Object. Data from the Block and the Merkle
 * Tree are then compared in instance methods. 
 * 
 * @author Robert Pierce
 * date: 07/17/2015
 */
public class BlockVerify {
	MerkleTree merkTree;       // a variable to hold the MerkleTree object
	Block block;               // a variable to hold a Block object
	String[] transactionIDs;   // a string array to hold the transaction IDs
	String merkleRoot = "";    // a string to hold a merkleRoot
	
	//----------------Constructors-------------------------------------------//
	/**
	 * This constructor constructs a BlockVerify object 
	 *  
	 * @param blk a Block Object used to create a MerkleTree
	 * object 
	 */
	public BlockVerify(Block blk) {
		block = blk;
		merkleRoot = blk.getMerkleRoot();
		transactionIDs = blk.getTransactionIDs();     // get the transactionIDs
		merkTree = new MerkleTree(transactionIDs);    // gets the merkleTree associated with the transactionIDs 
	}
	//----------------End Constructors---------------------------------------//
	
	//----------------Public Methods-----------------------------------------//
	
	/**
	 * This method gets the transaction IDs from a Block
	 * object.
	 * 
	 * @return the transactionIDs obtained from the block
	 */
	public String[] getTransactions() { return transactionIDs; }
	
	/**
	 * This method gets the Merkle Root from the block
	 * instance variable.
	 * 
	 * @return the Merkle Root from from a the block
	 * instance variable
	 */
	public String getRootFromBlock() { return merkleRoot; }
	
	
	/**
	 * This method get the Merkle Root from the merkTree 
	 * instance variable.
	 * 
	 * @return the Merkle Root from the merkTree instance 
	 * variable
	 */
	public String getRootFromTree() { return merkTree.getMerkleRoot(); }
	
	
	/**
	 * This method verifies that the Merkle Root from the block
	 * instance variable matches the Merkle Root from merkTree
	 * instance variable
	 * 
	 * @return a boolean if the Merkle Roots from the block instance
	 * variable and the merkTree instance variable are equal 
	 */
	public boolean verifyRoot() { return merkTree.getMerkleRoot().equals(merkleRoot); }
	
	
	/**
	 * This method returns the block number from the block instance
	 * variable.
	 *  
	 * @return and int that is the block number from the block
	 * instance variable
	 */
	public int getBlockNumber() { return block.getBlockNumber(); }
	
	//------------------------End Public Methods-----------------------------//	
}


