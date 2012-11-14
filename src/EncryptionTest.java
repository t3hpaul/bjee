import java.io.BufferedReader;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.activity.InvalidActivityException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/*
 * Paul Stasiuk
 * 10/21/2011
 * COMP348
 * 
 * Originally written for a network based transfer, modified to work as a stand-alone with a set initialization vector and salt value(s).
 *
 *This is an application that will encrypt a file based on a password that you input and export the file wherever you want it..
 *Note that you need to export the file as a binary file(without an extension) in order for it to decrypt the file..
 *
 *
 */
public class EncryptionTest {
	
	public static void main(String args[]){
		try{
						
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("Encrypt or decrypt?");
			String eOrD= br.readLine();
			if(eOrD.equalsIgnoreCase("encrypt")){
				
				System.out.print("Enter the file path of the file you wish to encrypt:");
				String fileLocation = br.readLine();
				System.out.println();
				
				System.out.print("Enter the file path of the destination of the encrypted file:");
				String destination= br.readLine();
				System.out.println();
				
				System.out.print("Enter the password that you wish to encrypt the file with:");
				String password=br.readLine();
				encryptFile(password,fileLocation, destination, true);
				
			}else{
				
				System.out.print("Enter the file path of the file you wish to decrypt:");
				String fileLocation = br.readLine();
				System.out.println();
				
				System.out.print("Enter the file path of the destination of the decrypted file:");
				String destination= br.readLine();
				System.out.println();
					
				System.out.print("Enter the password that you wish to decrypt the file with:");
				String password=br.readLine();
				encryptFile(password,fileLocation, destination, false);
			}			
	
		}catch(IOException e){
			System.out.println("There was an IOException:"+e);
			
		}

		
	}
	
	
	
	public static void encryptFile(String passwd, String inPath, String outPath, boolean encrypt) throws IOException{
		File inFile= new File(inPath);
		File outFile = new File(outPath);
		
		InputStream is = new FileInputStream(inFile);
		OutputStream os = new FileOutputStream(outFile);
					
		String salt="wlajefa";
		char[] saltArr= salt.toCharArray();
		char[] password=passwd.toCharArray();
		byte[] iv=new byte[]{(byte)0x8E, 0x12, 0x39, (byte)0x9C,
	            0x07, 0x72, 0x6F, 0x5A, 0x5B, (byte)0x8E, 0x12, 0x39, (byte)0x9C,
	            0x04,0x10,0x07};
		byte[] saltBArr=new byte[salt.length()];
		
		for(int i=0; i<salt.length();i++){
			saltBArr[i]=(byte)saltArr[i];
		}
		
		AlgorithmParameterSpec paramSpec= new IvParameterSpec(iv);
		
		try{
			
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec kSpec = new PBEKeySpec(password, saltBArr, 1024, 256);
			SecretKey sKey= factory.generateSecret(kSpec);
			SecretKey actSecret= new SecretKeySpec(sKey.getEncoded(), "AES");
			
			
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			
			byte[] buf = new byte[1024];
			
			if(encrypt){
				cipher.init(Cipher.ENCRYPT_MODE, actSecret, paramSpec);
				os= new CipherOutputStream(os,cipher);
				
				int readIn= 0;
				while((readIn= is.read(buf))>=0){
					os.write(buf,0,readIn);
				}
				os.close();
				
			}else{
				
				cipher.init(Cipher.DECRYPT_MODE, actSecret, paramSpec);
				System.out.println("Creating a new CipherInputStream...");
				System.out.println("Available bytes: "+ is.available());
				
				is= new CipherInputStream(is,cipher);
				System.out.println("Available bytes: "+ is.available());
				
				int readIn= 0;
				while((readIn= is.read(buf))>=0){
					os.write(buf,0,readIn);
				}
				os.close();
				
			}
			
		}catch(NoSuchAlgorithmException nosaE){
			System.out.println("There is no such algorithm: "+nosaE);
			nosaE.printStackTrace();
		}catch(NoSuchPaddingException nspE){
			System.out.println("There is no such padding schema: "+ nspE);
			nspE.printStackTrace();
		}catch(InvalidKeyException ikE){
			System.out.println("There is no such key: "+ ikE);
			ikE.printStackTrace();
		}catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch( IOException e){
			System.out.println("There was an IOError: "+e);
		}
		
		
	}
}
