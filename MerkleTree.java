package MerkleTreeVerify;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;


/**
 * This class creates a new MerkleTree Object. The 
 * constructor takes in a strArr that holds transaction IDs
 * of a certain Bit Coin Block. These IDs can be extracted
 * from a Block Object. The MerkleTree then computes the 
 * Merkle Root based on the tree with the transaction IDs
 * as leaves.
 * 
 * @author Robert Pierce
 * date: 07/17/2015 
 * 
 * 
 */
public class MerkleTree {
	String merkleRoot;
	Node[] nodeArr;
	int size;
	
	
	//----------------Constructor---------------------------------------------------------------//
	/**
	 * Constructs a new MerkleTree Object.
	 * In doing so Nodes are created for 
	 * every transaction ID.
	 * 
	 * @param strArr a string array holding 
	 * transaction IDs of a BitCoin Block
	 */
	public MerkleTree(String[] strArr) {
		int size = strArr.length;                     // the number of leaves in the tree
		nodeArr = new Node[strArr.length];            // initialize an array to hold Nodes
		
		if (strArr.length == 0) {                      // creates an empty tree if no transactions available
			merkleRoot = null;
			nodeArr = null;
		} else if (strArr.length == 1) {              // creates a merkle tree with only one leaf
			merkleRoot = strArr[0];
			nodeArr[0] = new Node(strArr[0]);
		} else {                                      // creates leaf nodes 
			for (int i = 0; i < strArr.length; ++i) { // iterate through the transaction ID array creating new nodes 
				if(strArr[i] != null) {
					nodeArr[i]= new Node(strArr[i]);   
				}
			}
			merkleRoot = getRoot(nodeArr);           // recursive call to calculate merkle root
		}
	}
	//-------------------End Constructor-------------------------------------------------------//
	
	//------------------Private Methods--------------------------------------------------------//
	
	/**
	 * A recursive method that gets the merkle root 
	 * for this tree
	 * @param nodeArr an array of nodes
	 * @return  the merkle root
	 */
	private String getRoot(Node[] nodeArr) {
		byte[] hexA, hexB;                                  // variables to hold hexadecimal codes in byte arrays
		Node[] hashList = new Node[(nodeArr.length+1)/2];   // a node array to hold fresh hashes from this layer of the tree
		
		// base case when only one node left (root)
		if (nodeArr.length == 1) {
			return nodeArr[0].getData();       // if only one node in tree then it must be the root, return the root
		}
		
		// iterate through the nodeArr incrementing by two
		// hashing two nodes at a time
		for (int i = 0; i < (nodeArr.length-1); i += 2) {	
			hexA = nodeArr[i].getHexData();        // get the hex data from the first node
			hexB = nodeArr[i+1].getHexData();      // get the hex data from the second node
			hashList[i/2] = hashEm(hexA,hexB);     // hash the two nodes together
		}
		
		// if there are an odd number of nodes
		// hash the last node twice
		if ((nodeArr.length % 2) == 1) {
			hexA = nodeArr[nodeArr.length-1].getHexData();   // get the hex data from the last node
			hashList[hashList.length-1] = hashEm(hexA,hexA); // hash the node with itself
		}
		// recursive call
		return getRoot(hashList);
	}
		
	/**
	 * Hashes two nodes together and returns
	 * a new node made from their hash.
	 * 
	 * @param hexA
	 * @param hexB
	 * @return
	 */
	private Node hashEm(byte[] hexA, byte[] hexB) {
		String hash;                            // a string to hold a hash
		byte[] hexARev, hexBRev, combined;      // byte array variables
		byte[] combinedRev, sha256, sha256Rev;  // more byte array variables
		DigestUtils digest = new DigestUtils(); // a DigestUtils Object from org.apache.commons.codec library to handle sha256 hashing
		
		// first we must reverse the byte array holding the hex codes this
		// compensates for LITTLE-ENDIAN requirement for bitcoin protocol 
		hexARev = new byte[hexA.length];  // create new byte arrays to hold reversed hex
		hexBRev = new byte[hexB.length];
		
		// reverse hexA
		for (int i = 0; i < hexA.length; ++i) {    // iterate through the hex in reverse order
			byte temp = hexA[hexA.length - (i+1)];   
			hexARev[i] = temp;                    
		}
		
		// reverse hexB
		for (int j = 0; j < hexB.length; ++j) {   // iterate through the hex in reverse order
			byte temp = hexB[hexB.length - (j+1)];
			hexBRev[j] = temp;
		}
		
		// now combine the two reversed hex codes
		combined = new byte[hexA.length + hexB.length];                   // create a new byte array to hold combined hex
		System.arraycopy(hexARev, 0, combined, 0, hexA.length);           // put the first hex into the new byte array
		System.arraycopy(hexBRev, 0, combined, hexA.length, hexB.length); // append the second hex into the new byte array
		
		// hash the combined hex code twice with sha256
		sha256 = digest.sha256(digest.sha256(combined));
		
		// we must reverse the hash to put back in BIG-ENDIAN
		sha256Rev = new byte[sha256.length];          // create a new byte array to hold the reversed hash code
		for (int i = 0; i < sha256.length; ++i) {     // iterate through in reverse order
			byte temp = sha256[sha256.length - (1+i)];
			sha256Rev[i] = temp;
		}
		
		// convert to hex a string representation
		hash = Hex.encodeHexString(sha256Rev);
		
		// create a new node with the combined hex string
		return new Node(hash);	
	}
	

	//----------------Public Methods-------------------------------------------------//
	
	public String getMerkleRoot() { return merkleRoot; }


	//---------------Nested Node Class----------------------------------//
	
	/**
	 * This class is a nested class to 
	 * hold leaf nodes of a Merkle Tree.
	  */
	private static class Node {
		String data;               // data must be a string representation of a hexadecimal code 
		byte[] hexData;            // a byte array to hold a hex version of the data
		
		
		//**********Nested Node Constructor******************//
		/**
		 * Constructs a new Node
		 * @param str
		 */
		public Node(String str) {
			data = str;                               // set data to str
			
			// decode the hex string into bytes 
			try {
				hexData =  Hex.decodeHex(data.toCharArray());       // decode the hex string into bytes
			} catch (DecoderException e1) {
				System.out.println("Sorry, a decoding excpetion occured!");
				e1.printStackTrace();
			}
		}
		
		/**
		 * @return the hexadecimal representation 
		 * of the data as byte array
		 */
		public byte[] getHexData() { return hexData; }
		
		
		/**
		 * @return the string representation of the 
		 * data
		 */
		public String getData() { return data; }	
	}
	//------------End Nested Node Class------------------------------------//
}