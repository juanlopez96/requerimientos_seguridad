/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;


import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 *
 * @author Yeiny
 */
public class encrypt {

    private static SecretKeySpec secretkey;
    private byte[] key;
    private final String thekey = "STLqu+bOCZZVg6OW5f/0GA==";//SHA-256

    public encrypt() {
        setkey(thekey);
    }

    private void setkey(String key) {
        MessageDigest msg = null;
        try {
            this.key = key.getBytes("UTF-8");
            msg = MessageDigest.getInstance("SHA-1");
            this.key = msg.digest(this.key);
            this.key = Arrays.copyOf(this.key, 16);
            this.secretkey = new SecretKeySpec(this.key, "AES");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String toEncrypt(String pass) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretkey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(pass.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error al encriptar " + e);
        }
        return null;
    }

}
