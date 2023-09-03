package Utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {

    public static String hashLogin(String email, String password) throws NoSuchAlgorithmException {
        return new StringBuilder(email+"-"+password).reverse().toString();
    }

}
