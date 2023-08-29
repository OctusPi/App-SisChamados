package Utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {

    public static String hashLogin(String email, String password) throws NoSuchAlgorithmException {
        return new StringBuilder(getHashMd5(email)+"-"+getHashMd5(password)).reverse().toString();
    }

    public static String getHashMd5(String value) throws NoSuchAlgorithmException {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        BigInteger hash = new BigInteger(1, messageDigest.digest(value.getBytes()));
        return hash.toString(16);
    }
}
