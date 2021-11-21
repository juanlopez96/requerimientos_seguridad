/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;


import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.validator.routines.EmailValidator;


/**
 *
 * @author Yeiny
 */
public class encrypt {

    private static SecretKeySpec secretkey;
    private byte[] key;
    private final String thekey = "STLqu+bOCZZVg6OW5f/0GA==";//SHA-256
    private boolean specialChar = false, lowercase = false, uppercase = false, number = false;
    private final String specialChars = "~`!@#$%^&*()-_=+\\|[{]};:'\",<.>/?";

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

    public boolean validatePasswordLength(String password) {
        if (password.length() > 11) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean validatePasswordNumber(String password) {
        boolean number = false;
        for (int i = 0; i < password.length(); i++) {
            char current = password.charAt(i);
            if (Character.isDigit(current)) {
                number = true;
            }
        }
        return number;
    }
    
    public boolean validatePasswordUpper(String password) {
        boolean uppercase = false;
        for (int i = 0; i < password.length(); i++) {
            char current = password.charAt(i);
            if (Character.isUpperCase(current)) {
                uppercase = true;
            }
        }
        return uppercase;
    }
    
    public boolean validatePasswordLower(String password) {
        boolean lowercase = false;
        for (int i = 0; i < password.length(); i++) {
            char current = password.charAt(i);
            if (Character.isLowerCase(current)) {
                lowercase = true;
            }
        }
        return lowercase;
    }

    public boolean validatePasswordSpecial(String password) {
        boolean specialChar = false;
        for (int i = 0; i < password.length(); i++) {
            char current = password.charAt(i);
            if (specialChars.contains(String.valueOf(current))) {
                specialChar = true;
            }
        }
        return specialChar;
    }

    public String passwordStrength(String password, char typed) {
        char[] letters = "abcdefghijklmnñopqrstuvwxyz".toCharArray();
        int points = 0;
        int upper = 0, lower = 0, special = 0, number = 0, length = 0;
        int repeatChar = 0, consec = 0;
        int auxSeqChar = 0, auxSeqNumber = 0;
        ArrayList<Character> used = new ArrayList();
        boolean onlyletters = true, onlynumbers = true,
                hasUpper = false, hasLower = false, hasSpecial = false, hasNumber = false, hasLength = false;
        length = password.length();
        for (int i = 0; i < length; i++) {
            char aux = password.charAt(i);
            if (Character.isUpperCase(aux)) {
                upper++;
            } else if (Character.isLowerCase(aux)) {
                lower++;
            } else if (Character.isDigit(aux)) {
                number++;
            } else if (specialChars.contains(String.valueOf(aux))) {
                special++;
            }
        }
        int suma = 0;
        if (upper > 0) {
            if (hasLower || hasNumber || hasSpecial) {
                suma += (length - upper) * 2;
            }
            hasUpper = true;
        }
        if (lower > 0) {
            if (hasUpper || hasNumber || hasSpecial) {
                suma += (length - lower) * 2;
            }
            hasLower = true;
        }
        if (number > 0) {
            if (hasLower || hasUpper || hasSpecial) {
                suma += number * 4;
            }
            hasNumber = true;
        }
        if (special > 0) {
            suma += special * 6;
            hasSpecial = true;
        }
        if (length > 0) {
            suma += (length * 4);
        }
        if (hasUpper && hasLower && hasNumber && hasSpecial && length > 11) {
            suma += 10;
        }
        points += suma;

        char[] chars = password.toCharArray();
        for (char x : chars) {
            if (!Character.isLetter(x)) {
                onlyletters = false;
            } else if (!Character.isDigit(x)) {
                onlynumbers = false;
            }
        }
        for (int i = 0; i < password.length(); i++) {
            if (used.size() > 0) {
                boolean exist = false;
                for (char x : used) {
                    if (x == password.charAt(i)) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    used.add(password.charAt(i));
                }
            } else {
                used.add(password.charAt(i));
            }
            if (i - 1 > -1) {
                /*
                if (password.charAt(i) == password.charAt(i - 1)) {
                    consec++;
                }*/
                if (Character.isLetter(password.charAt(i))) {
                    if (Character.isUpperCase(password.charAt(i)) && Character.isUpperCase(password.charAt(i - 1))) {
                        consec++;
                    } else if (Character.isLowerCase(password.charAt(i)) && Character.isLowerCase(password.charAt(i - 1))) {
                        consec++;
                    }
                }
                if (Character.isDigit(password.charAt(i)) && Character.isDigit(password.charAt(i - 1))) {
                    consec++;
                }
            }
        }
        int aux1 = 0;
        used = new ArrayList();
        for (int i = 0; i < password.length(); i++) {
            if (used.size() > 0) {
                boolean exist = false;
                for (char x : used) {
                    if (Character.toLowerCase(x) == Character.toLowerCase(password.charAt(i))) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    used.add(Character.toLowerCase(password.charAt(i)));
                }
            } else {
                used.add(password.charAt(i));
            }
        }
        for (int i = 0; i < used.size(); i++) {
            repeatChar = 0;
            for (int j = i; j < password.length(); j++) {
                if (Character.toLowerCase(used.get(i)) == Character.toLowerCase(password.charAt(j))) {
                    repeatChar++;
                }
            }
            repeatChar = repeatChar == 1 ? 0 : repeatChar;
            aux1 += repeatChar * (repeatChar - 1);
        }
        int sequenceChar = 1;
        int sequenceNumber = 1;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLetter(password.charAt(i))) {
                int index = 0;
                for (int j = 0; j < letters.length; j++) {
                    if (i != 0) {
                        if (password.charAt(i - 1) == letters[j]) {
                            index = j;
                        }
                    }
                    if (password.charAt(i) == letters[j]) {
                        if (j - index == 1) {
                            sequenceChar++;
                        }
                    }
                }
            } else if (Character.isDigit(password.charAt(i))) {
                if (i != 0) {
                    int last = Character.getNumericValue(password.charAt(i - 1));
                    int actual = Character.getNumericValue(password.charAt(i));
                    if (actual - last == 1) {
                        sequenceNumber++;
                    }
                }
            }
        }
        int resta = 0;
        if (sequenceChar > 2) {
            auxSeqChar = (sequenceChar - 2) * 3;
        }
        if (sequenceNumber > 2) {
            auxSeqNumber = (sequenceNumber - 2) * 3;
        }
        if (onlyletters) {
            resta -= password.length();
        }
        if (onlynumbers) {
            resta -= password.length();
        }

        resta -= auxSeqChar;
        resta -= auxSeqNumber;
        resta -= aux1;
        resta -= (consec * 2);
        System.out.println("sequnumb " + auxSeqNumber);
        System.out.println("Suma " + suma);
        System.out.println("Resta " + resta);
        System.out.println("Resultado " + (suma + resta));
        if ((suma + resta) >= 0 && (suma + resta) < 20 || (suma + resta)<0) {
            return "Muy debil";
        } else if ((suma + resta) >= 20 && (suma + resta) < 40) {
            return "Debil";
        } else if ((suma + resta) >= 40 && (suma + resta) < 60) {
            return "Buena";
        } else if ((suma + resta) >= 60 && (suma + resta) < 80) {
            return "Fuerte";
        } else {
            return "Muy Fuerte";
        }
    }
    public static boolean validateEmail(String email) {
        try {
            EmailValidator em = EmailValidator.getInstance();
            return em.isValid(email);
        } catch (Exception e) {
            return false;
        }
    }
    public static String generateCode() {
        int number = (int) (Math.random() * 899999) + 100000;
        System.out.println(number);
        return String.valueOf(number);
    }
}
