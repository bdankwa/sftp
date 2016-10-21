package sftp;

import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public final class SFTPCrypto {
	
	private static final String ALGORITHM = "AES";
	private static final String SECRETE_KEY = "lca578wexteqEf97";
	
	public static String encrypt(String plainText) throws Exception{
		
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.ENCRYPT_MODE, generateKey());
		
		byte[] cipherString = c.doFinal(plainText.getBytes());
		return new BASE64Encoder().encode(cipherString);		
	}
	
	public static byte[] encrypt(byte[] plainText) throws Exception{
		
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.ENCRYPT_MODE, generateKey());
		return c.doFinal(plainText);
	}	
	
	public static String decrypt(String cypherText) throws Exception{
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.DECRYPT_MODE, generateKey());		
		
		byte[] decodedVal = new BASE64Decoder().decodeBuffer(cypherText);
		return new String(c.doFinal(decodedVal));		
	}
	
	public static byte[] decrypt( byte[] cypherText) throws Exception{
		
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.DECRYPT_MODE, generateKey());
		return c.doFinal(cypherText);
	}
	
	private static Key generateKey(){
		
		byte[] keyValue;
		
		keyValue = SECRETE_KEY.getBytes();		
		return new SecretKeySpec(keyValue, ALGORITHM);
		
	}

}
