import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ReversibleEncryptor {

    /**
     * Encryptor method that encrypts a given input file and writes it to a given output file.
     * This method is reversible, so encrypting an encrypted file will decrypt it.
     * @param args java commandline arguments (not used)
     */
    public static void main(String[] args) {
        String inputFile;
        String outputFile;

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the name of the input file: ");
            inputFile = scanner.nextLine();

            System.out.print("Enter the name of the output file: ");
            outputFile = scanner.nextLine();
        }

        String password;

        try (FileInputStream env = new FileInputStream(".env")) {
            password = new String(env.readAllBytes()).strip();
            assert !password.isEmpty();
        } catch (IOException | AssertionError e) {
            System.err.println("Password is empty or missing!");
            return;
        }

        try (FileInputStream inp = new FileInputStream(inputFile)) {
            try (FileOutputStream out = new FileOutputStream(outputFile)) {
                byte[] srcBytes = inp.readAllBytes();
                byte[] passwordBytes = password.getBytes();
                for (int i = 0; i < srcBytes.length; i++) {
                    srcBytes[i] = (byte) (srcBytes[i] ^
                        (passwordBytes[i % passwordBytes.length]));
                }
                out.write(srcBytes);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
    }
}
