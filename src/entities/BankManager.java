package entities;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;

public class BankManager {
    private static Set<String> accountsNumbers = new HashSet<>();
    private static Random random = new Random();

    public static String generateNumber() {
        String newNumber;
        do {
            int prefix = random.nextInt(9000) + 1000;
            int suffix = random.nextInt(10);
            newNumber = String.valueOf(prefix) + "-" + String.valueOf(suffix);
        } while(accountsNumbers.contains(newNumber));

        registerNumber(newNumber);

        return newNumber;
    }

    private static boolean isValid(String number) {
        return !accountsNumbers.contains(number);
    }

    private static void registerNumber(String number) {
        accountsNumbers.add(number);
    }
}
