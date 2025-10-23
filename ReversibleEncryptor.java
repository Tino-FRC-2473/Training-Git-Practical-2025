import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ReversibleEncryptor {

	/**
	 * Ecryptor method that encrypts a given input file and writes it to a given output file.
	 * This method is reversible, so encrypting an encrypted file will decrypt it.
	 * @param args java commandline arguments (not used)
	 */
	public static void main(String... args) {
		final String inputFile;
		final String outputFile;
		try (var stdin = new Scanner(System.in)) {
			System.out.print("Enter the input file: ");
			inputFile = stdin.nextLine();
			System.out.print("Enter the output file: ");
			outputFile = stdin.nextLine();
		}
		final String password;
		try (var env = new FileInputStream(".env")) {
			password = new String(env.readAllBytes());
			assert !password.isEmpty();
		} catch (IOException | AssertionError e) {
			System.err.println("Password is empty or missing");
			return;
		}
		try (
			var src = new FileInputStream(inputFile);
		) {
			try (var out = new FileOutputStream(outputFile)) {
				var srcBytes = src.readAllBytes();
				var passwordBytes = password.getBytes();
				for (int i = 0; i < srcBytes.length; i++) {
					srcBytes[i] = (byte) (srcBytes[i] ^ (passwordBytes[i % passwordBytes.length]));
				}
				out.write(srcBytes);
			}
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
			return;
		}
	}
}
