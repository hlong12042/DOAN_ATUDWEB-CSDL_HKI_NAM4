package ptithcm.Hash;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hash {
	public static final int MD5_LENGTH = 32;
	public static final int SHA1_LENGTH = 40;
	//In ra mã hex từ byte[]
    public static String convertByteToHex(byte[] data, int length){
        BigInteger number = new BigInteger(1, data);
        String hashtext = number.toString(16);
        while (hashtext.length()<length){
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }
    //Tạo mã băm MD5
    public static byte[] convertMD5(String text){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes("UTF-8"));
            byte[] hashData = md.digest();
            return hashData;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(Hash.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    //Tạo mã băm SHA1
    public static byte[] convertSHA1(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("UTF-8"));
            byte[] hashData = md.digest();
            return hashData;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(Hash.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static byte[] convertMD5(byte[] text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text);
            byte[] hashData = md.digest();
            return hashData;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Hash.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static byte[] convertSHA1(byte[] text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(text);
            byte[] hashData = md.digest();
            return hashData;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Hash.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    //Test các phương thức
    public static void main(String[] args) {
        System.out.println(convertByteToHex(convertSHA1(new String("123456").getBytes()), 40));
    }
}
