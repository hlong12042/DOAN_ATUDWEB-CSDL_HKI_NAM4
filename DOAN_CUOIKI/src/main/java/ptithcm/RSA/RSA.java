package ptithcm.RSA;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {
	//Đọc PrivateKey từ file
    public static PrivateKey getPrivateKey(String name){
        try {
            byte[] keybytes = Files.readAllBytes(new File(GenerateKeys.PRIVATE_KEY_FOLDER + name).toPath());
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keybytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    //Đọc PublicKey từ file
    public static PublicKey getPublicKey(String name){
        try {
            byte[] keyBytes = Files.readAllBytes(new File(GenerateKeys.PUBLIC_KEY_FOLDER + name + ".pub").toPath());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    //Mã hóa và trả về mảng bytes Cipher text
    public static byte[] encryptRSA(String plain, String name, int keylength) {
        try {
            PublicKey pubkey = getPublicKey(name);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);
            
            byte[] byteEncrypted = cipher.doFinal(plain.getBytes());
            return byteEncrypted;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    //Giải mã và trả về Chuỗi Plain text
    public static String decryptRSA(byte[] byteEncrypted, String name, int keylength) {
        try {
            PrivateKey prikey = getPrivateKey(name);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, prikey);
            
            byte[] byteDecrypted = cipher.doFinal(byteEncrypted);
            return new String(byteDecrypted);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    //Kiểm tra xem đã nhân viên đã sinh khóa chưa
    public boolean isEncrypted(String name){
        return new File(GenerateKeys.PRIVATE_KEY_FOLDER + name).exists();
    }
    //Test bộ mã hóa
    public static void main(String[] args) {
        try{
            String name = "NV01", luong = "3000000";
            new GenerateKeys(GenerateKeys.KEY_1024, name).gernerateKeysToFile();
            byte[] byteEncrypted = RSA.encryptRSA(luong, name, GenerateKeys.KEY_2048);
            String plain = RSA.decryptRSA(byteEncrypted, name, GenerateKeys.KEY_2048);
            System.out.println(plain);
        } catch (NoSuchAlgorithmException | IOException  ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
