package utils;

import io.restassured.response.Response;

import java.nio.charset.Charset;
import java.util.Random;

import static filesReaders.ReadFromFiles.getJsonStringValueByKey;

public class Utils {

    public static String generateRandomString (int lettersSize)
    {
        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(lettersSize);

        for (int i = 0; i < lettersSize; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
