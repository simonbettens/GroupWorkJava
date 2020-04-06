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
        outputBytes[0] = 0x00; // format marker
        System.arraycopy(salt,0, outputBytes, 1, saltSize); 
        System.out.println(outputBytes +" "+ Base64.getEncoder().encodeToString(outputBytes)); 
        System.arraycopy(subkey, 0, outputBytes, 1 + saltSize, pbkdf2SubkeyLength);
        System.out.println(outputBytes+" "+ Base64.getEncoder().encodeToString(outputBytes));
        
        return outputBytes;
    }
	/*
	 * private static byte[] HashPasswordV2(string password, RandomNumberGenerator rng)
        {
            const KeyDerivationPrf Pbkdf2Prf = KeyDerivationPrf.HMACSHA1; // default for Rfc2898DeriveBytes
            const int Pbkdf2IterCount = 1000; // default for Rfc2898DeriveBytes
            const int Pbkdf2SubkeyLength = 256 / 8; // 256 bits
            const int SaltSize = 128 / 8; // 128 bits

            // Produce a version 2 (see comment above) text hash.
            byte[] salt = new byte[SaltSize];
            rng.GetBytes(salt);
            byte[] subkey = KeyDerivation.Pbkdf2(password, salt, Pbkdf2Prf, Pbkdf2IterCount, Pbkdf2SubkeyLength);

            var outputBytes = new byte[1 + SaltSize + Pbkdf2SubkeyLength];
            outputBytes[0] = 0x00; // format marker
            Buffer.BlockCopy(salt, 0, outputBytes, 1, SaltSize);
            Buffer.BlockCopy(subkey, 0, outputBytes, 1 + SaltSize, Pbkdf2SubkeyLength);
            return outputBytes;
        }
	 */
	
}
