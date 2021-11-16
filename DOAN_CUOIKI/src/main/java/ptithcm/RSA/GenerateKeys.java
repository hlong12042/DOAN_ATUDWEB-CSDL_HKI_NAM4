package ptithcm.RSA;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenerateKeys {
	//Thư mục chứa key
    public static final String PUBLIC_KEY_FOLDER = "rsa_keypair/publicKey/";
    public static final String PRIVATE_KEY_FOLDER = "rsa_keypair/privateKey/";
    //Loại key
    public static final int KEY_1024 = 1024;
    public static final int KEY_2048 = 2048;
    public static final int KEY_3072 = 3072;
    
    public String name;//Tên file key
    private KeyPairGenerator keyGen;//Bộ tạo key của java
    private KeyPair pair;//Bộ chứa key
    private PublicKey pubkey;//PublicKey lấy từ KeyPair
    private PrivateKey prikey;//PrivateKey lấy từ KeyPair
    //Constructor
    public GenerateKeys (int keylength, String name) throws NoSuchAlgorithmException {
        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(keylength);
        this.name = name;
    }
    //Tạo key từ KeyPairGen
    public void createKeys(){
        this.pair = this.keyGen.generateKeyPair();
        this.prikey = pair.getPrivate();
        this.pubkey = pair.getPublic();
    }
    //Các Getter
    public PrivateKey getPrivateKey(){
        return this.prikey;
    }
    
    public PublicKey getPublicKey(){
        return this.pubkey;
    }
    //Ghi key ra file
    public void writeToFile(String path, byte[] key) throws IOException{
        File file = new File(path);
        file.getParentFile().mkdirs();
        
        FileOutputStream out = new FileOutputStream(file);
        out.write(key);
        out.flush();
        out.close();
    }
    //Tạo key -> ghi ra file
    public void gernerateKeysToFile() throws IOException{
        System.out.println("Starting generate...");
        this.createKeys();
        this.writeToFile(PUBLIC_KEY_FOLDER + name +".pub", this.getPublicKey().getEncoded());
        this.writeToFile(PRIVATE_KEY_FOLDER + name, this.getPrivateKey().getEncoded());
        System.out.println("Generated!");
    }
    //Test chức năng
    public static void main(String[] args) {
        try{
            new GenerateKeys(KEY_1024, "NV02").gernerateKeysToFile();
            new GenerateKeys(KEY_1024, "NV03").gernerateKeysToFile();
            new GenerateKeys(KEY_1024, "NV04").gernerateKeysToFile();
        } catch (NoSuchAlgorithmException | IOException ex) {
            Logger.getLogger(GenerateKeys.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
