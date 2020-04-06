package domein;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswoordHasher {
	/*
	 * Version 3: PBKDF2 with HMAC-SHA256, 128-bit salt, 256-bit subkey, 10000
	 * iterations. Format: { 0x01, prf (UInt32), iter count (UInt32), salt length
	 * (UInt32), salt, subkey } (All UInt32s are stored big-endian.)
	 */
	//Dit zou het moeten zijn
	// AQAAAAEAACcQAAAAEAMz2ZnoOBRyFo1zq6aq7filJrxr60Ds0jKOzh2wE4ttyJ5zzCwCelfjaGXNCN/lvQ==
	
	// AQAAAAEAACcQAAAAEKNYDl4H7hwApOwom0bzHGajA5bPAMY1HjI5M0s+F+TsI3BykGWFFq3d7QTTfufaRQ==
	// gepikt hiervan
	// https://stackoverflow.com/questions/22580853/reliable-implementation-of-pbkdf2-hmac-sha256-for-java
	//https://github.com/aspnet/Identity/blob/a8ba99bc5b11c5c48fc31b9b0532c0d6791efdc8/src/Microsoft.AspNetCore.Identity/PasswordHasher.cs
	public static byte[] getEncryptedPassword(String password)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

        int pbkdf2IterCount = 1000; // default for Rfc2898DeriveBytes
        int pbkdf2SubkeyLength = 256 / 8; // 256 bits
        int saltSize = 128 / 8; // 128 bits

        // Produce a version 2 (see comment above) text hash.
        SecureRandom random = new SecureRandom();
		byte[] salt = new byte[saltSize];
		random.nextBytes(salt);
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, pbkdf2IterCount, 256);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] subkey = factory.generateSecret(spec).getEncoded();

        byte[] outputBytes = new byte[1 + saltSize + pbkdf2SubkeyLength];
        System.out.println(1 +" + "+ saltSize +" + "+ pbkdf2SubkeyLength + " = " + outputBytes.length);
        outputBytes[0] = 0x00; // format marker
        System.out.println("ArrayCopy");
		System.arraycopy(salt,0, outputBytes, 1, saltSize); 
		System.out.println(outputBytes +" "+ Base64.getEncoder().encodeToString(outputBytes));
		System.arraycopy(subkey, 0, outputBytes, 1 + saltSize, pbkdf2SubkeyLength);
		System.out.println(outputBytes+" "+ Base64.getEncoder().encodeToString(outputBytes));
        return outputBytes;
	}
	
}
