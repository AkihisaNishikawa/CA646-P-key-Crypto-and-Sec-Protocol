package test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Base64;

import javax.crypto.Cipher;

public class A4_P3 {

	public static KeyPair KeyPair() throws Exception {
		KeyPairGenerator key = KeyPairGenerator.getInstance("RSA");
		key.initialize(2048, new SecureRandom());

		return key.generateKeyPair();
	}

	public static String rsaEnc(String plainText, PublicKey publicKey) throws Exception {
		Cipher encryptCipher = Cipher.getInstance("RSA");
		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return Base64.getEncoder().encodeToString(encryptCipher.doFinal(plainText.getBytes("ASCII")));
	}

	public static String rsaDec(String cipherText, PrivateKey privateKey) throws Exception {
		byte[] bytes = Base64.getDecoder().decode(cipherText);
		Cipher decriptCipher = Cipher.getInstance("RSA");
		decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

		return new String(decriptCipher.doFinal(bytes), "ASCII");
	}

	public static String rsaSign(String plainText, PrivateKey privateKey) throws Exception {
		Signature priSign = Signature.getInstance("SHA1withRSA");
		priSign.initSign(privateKey);
		priSign.update(plainText.getBytes("ASCII"));

		byte[] signature = priSign.sign();

		return Base64.getEncoder().encodeToString(signature);
	}

	public static boolean verification(String plainText, String signature, PublicKey publicKey) throws Exception {
		Signature pubSign = Signature.getInstance("SHA1withRSA");
		pubSign.initVerify(publicKey);
		pubSign.update(plainText.getBytes("ASCII"));

		return pubSign.verify(Base64.getDecoder().decode(signature));
	}

	public static void main(String[] args) throws Exception {
		// Using java security package
		// RSA is implemented in java security package
		// Reference: https://niels.nu/blog/2016/java-rsa.html
		
		KeyPair pair = KeyPair();
		String message = "This practical question is relatively hard";

		// Encrypt message
		String c = rsaEnc(message, pair.getPublic());
		System.out.println("Encrypted Message: "+c);

		// Decrypt message and print it
		String original = rsaDec(c, pair.getPrivate());
		System.out.println("Decrypted message: " + original);

		String signature = rsaSign(message, pair.getPrivate());

		// Checking signature
		System.out.println("Correct Signature is : " + verification(message, signature, pair.getPublic()));
	}

}
