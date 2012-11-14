import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class FileByter {
	
	
	/*
	public static void main(String[] args){
		String fileLocation;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try{
			System.out.println("Enter the file Location:");
			fileLocation=br.readLine();
			printBytes(getBytesFromFile(new File(fileLocation)));
		}catch(IOException e){
			System.out.println("Error."+ e);
			
		}
		
	}
		*/
	
	public FileByter(File file){
		try{
			printBytes(getBytesFromFile(file));
		}catch(IOException e){
			System.out.println("There was an exception:"+e);
		}
	}
	
	// Returns the contents of the file in a byte array.
	public byte[] getBytesFromFile(File file) throws IOException {
	    InputStream is = new FileInputStream(file);

	    // Get the size of the file
	    long length = file.length();

	    // You cannot create an array using a long type.
	    // It needs to be an int type.
	    // Before converting to an int type, check
	    // to ensure that file is not larger than Integer.MAX_VALUE.
	    if (length > Integer.MAX_VALUE) {
	        // File is too large
	    }

	    // Create the byte array to hold the data
	    byte[] bytes = new byte[(int)length];

	    // Read in the bytes
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length
	           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }

	    // Ensure all the bytes have been read in
	    if (offset < bytes.length) {
	        throw new IOException("Could not completely read file "+file.getName());
	    }

	    // Close the input stream and return bytes
	    is.close();
	    return bytes;
	    
	}
	
	public static void printBytes(byte[] bytes1){
		for(int i=0; i<bytes1.length; i++){
			StringBuffer s= new StringBuffer(Integer.toString(bytes1[i] & 0xFF, 16));
			if(s.length()==1){
				s.insert(0, "0");
			}
			if(i%50==0){
				System.out.println();
			}else{
				System.out.print(s +" ");
			}
		}
		for(int i=0; i<10; i++){
			System.out.println();
		}
		
		
		
	}
	
	

}
