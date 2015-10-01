package MerkleTreeVerify;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class creates a Block object. The Block 
 * Object either takes in a URL address to a specific
 * block or grabs the most recent block from the internet. 
 * The Block relies on the website https://blockchain.info 
 * as a source of data. The Block Object only stores data
 * is used to recreate the Merkle Root by extracting the 
 * transaction IDs from the block in question.
 * 
 * @author Robert Pierce 
 * @author Conor Fitzptrick
 * date: 07/17/2015 
 * 
 * CS 1332 
 * summer 2015
 * 
 * Project:
 * Merkle Tree
 *
 */
public class Block {
	private URL blockURL;                 // a variable to hold a URL address to the desired block
	private String merkleRoot = "";       // a variable to hold the merkle root from the desired block
	private String[] transactionIDs;      // a string array to hold all of the transaction IDs from the desired block
	private int blockNum;                 // a variable to hold the current block number
	private static final String homePage = "https://blockchain.info/";  // a static string that holds the URL of the front page of the blockchain.info site
	
	//------------------Constructor--------------------------------//
	/**
	 * This constructor constructs a BLock object when
	 * you know the URL to the block from the 
	 * https://blockchain.info site.
	 * 
	 * @param url the URL of the block in question
	 */
	public Block(URL url) throws MalformedURLException {
		blockURL = url;
		transactionIDs = fetchTransactions(url);   // get the transaction IDs from the site
	}
	
	/**
	 * This constructor constructs a Block object when
	 * you want to verify the most recent block from the 
	 * home page  https://blockchain.info/
	 */
	public Block() throws MalformedURLException {
		this(fetchMostRecent(homePage));          // get the URL of the most recent block from the site
	}

	//----------------End Constructor-----------------------------//
	
	/**
	 * This method gets the transaction IDs from the 
	 * desired block.
	 * 
	 * @param url the URL address of the desired block
	 * @return  a string array containing the transaction IDs
	 */
	private String[] fetchTransactions(URL url) {
		String[] strArr = null;                // a string array variable to hold the transaction IDs
		//String numTransactions = "";         // a string to hold data for total number of transaction fetched from the site      
        int numTransactions = 0;               // a variable to hold the total number of transactions in this block  
        int transactionCount = 0;              // a counter to hold the index of the current transaction
        
		try {
            // get URL content
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;               // a String variable to hold the current line from the input stream

            // iterate through the web page source code
            while ((inputLine = br.readLine()) != null) {  
            	
            	// get block number first
            	if (inputLine.contains("Block #")) {  // finds the line with the Block Number
           		    int startInd = inputLine.indexOf("#");              // gets the beginning of the data
                    int endInd = inputLine.lastIndexOf("<");            // gets the end of the data
                    inputLine = inputLine.substring(startInd+1, endInd);// gets the data
                    blockNum = Integer.parseInt(inputLine);             // parse data
            	}  
                    
            	// get number of transactions next
            	if (inputLine.contains("Number Of Transactions")) {      // finds the line with the transaction count Root
            		 inputLine = br.readLine();                          // the number is on the next line, discard this line
            		 int startInd = inputLine.indexOf(">");              // gets the beginning of the data
                     int endInd = inputLine.lastIndexOf("<");            // gets the end of the data
                     inputLine = inputLine.substring(startInd+1, endInd);// gets the data
                     numTransactions = Integer.parseInt(inputLine);      // parse data
                     
                     
                     // initialize strArr: this MUST happen within the if block of code
                     // we need to know numTransactions before initialization but we 
                     // only want initialization to happen once, so must be in if() block
                     strArr = new String[numTransactions];
            	}   
                
            	// get the merkle root from the site
                if (inputLine.contains("Merkle Root")) {                 // finds the portion with the Merkle Root
                    inputLine = br.readLine();                           // reads the line
                    int startInd = inputLine.indexOf(">");               // gets the beginning off the data
                    int endInd = inputLine.lastIndexOf("<");             // gets the end of the data
                    inputLine = inputLine.substring(startInd+1, endInd); // cuts down to just the root
                    merkleRoot = inputLine;                              // save the merkle root for verification
                }
                
                // get all of the transaction IDs
                if (inputLine.contains("\"hash-link\" href=\"/tx/")) {  // finds portion that contains transaction IDs                                       
                    int ind = 0;
                    ind = inputLine.indexOf("/tx/");                    // gets the beginning of the data
                    inputLine = inputLine.substring(ind + 4);           // slices the line to the start of the transaction ID
                    ind = inputLine.indexOf(">");                       // gets the end of the data
                    inputLine = inputLine.substring(0, ind - 1);        // cuts down to just the transaction ID
                    strArr[transactionCount] = inputLine;               // store the transaction ID in the strArr
                    transactionCount++;                                 // increment the strArr index
                }
            }
           //closes buffer
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
		return strArr;
    }	
	
	/**
	 * This method gets the URL of the most recent block from the
	 *  https://blockchain.info site.
	 *  
	 * @param homePge the URL of the homepge of the blockchain.info site
	 * @return  a URL of the most recent block of the form https://blockchain.info/...
	 * @throws MalformedURLException
	 */
	private static URL fetchMostRecent(String homePge) throws MalformedURLException {
		URL homePage = new URL(homePge);                   // create a new URL object from the homepage
		StringBuilder link = new StringBuilder(homePge);   // put the homepage link into a string builder
		String extension = "";                             // a String variable to hold the extension of the most recent block
	
		try {
            // get web page content
            URLConnection conn = homePage.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;                // a variable to hold html data from the web page
            
            // iterate through the web page data
            while ((inputLine = br.readLine()) != null) {                

            	// get the most recent block page extension
            	if (inputLine.contains("<a href=\"/block/")) {            // find the first instance of a block href extension
            		int startInd = inputLine.indexOf("\"");               // gets the beginning of the data
                    int endInd = inputLine.lastIndexOf("\"");             // gets the end of the data
                    inputLine = inputLine.substring(startInd+1, endInd);  // cuts down to just the block extension 
                    extension = inputLine;                                // store the extension
                    break;
            	}
            } 
            
            // create the new URL
            link = link.append(extension);                // append the extension to the home page address 
            URL topBlockLink = new URL(link.toString());  // make a URL Object out of the address string
            return topBlockLink;                          // return the URL Object
            
		} catch (IOException e) {
        e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * This method returns a URL Object of the this 
	 * block.
	 * 
	 * @return a URL Object of the this block
	 */
	public URL getURL() { return blockURL; }
	
	/**
	 * This method returns the Merkle Root of this 
	 * block extracted from the web page.
	 * 
	 * @return the Merkle Root of this block
	 */
	public String getMerkleRoot() { return merkleRoot; }
	
	/**
	 * This method returns a string array containing 
	 * all of the transaction IDs found in this block.
	 * 
	 * @return a string array containing all of the 
	 * transaction IDS found in this block
	 */
	public String[] getTransactionIDs() { return transactionIDs; }
	
	/**
	 * This method returns the block number of this
	 * block.
	 * 
	 * @return the block number of this block
	 */
	public int getBlockNumber() { return blockNum; }
	
	
}
