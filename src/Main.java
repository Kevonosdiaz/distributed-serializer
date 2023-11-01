import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Prompt user to choose between 1) Serializing data or 2) Deserializing data
        // Setup for user input
        Scanner scanner = new Scanner(System.in);

        boolean isSerializing = userChoiceSerializing(scanner);
        // If user chooses 1, serialize data
        if (isSerializing) {
            // Present object options for user to choose to from
            //
            int objChoice = userChoiceObject(scanner);
        }

    }

    private static int userChoiceObject(Scanner scanner) {
        // Print menu options
        System.out.println("Please select one of the following object options:");
        System.out.println("1.) Simple Object (storing primitives only)");
        System.out.println("2.) Complex Object (storing primitives and object refs)");
        System.out.println("3.) Array of Primitives");
        System.out.println("4.) Array of Object References");
        System.out.println("5.) HashSet of Ints (Java Collection)");
        //  Take user input
        int userChoice = scanner.nextInt();
        if (userChoice != 1 && userChoice != 2 && userChoice != 3){
            System.err.println("Invalid input! Please enter 1, 2, or 3 after relaunching.");
            System.exit(1);
        }
        return userChoice;
    }

    private static boolean userChoiceSerializing(Scanner scanner) {
        // Print menu options
        System.out.println("Please select one of the following options:");
        System.out.println("1.) Serialize an object");
        System.out.println("2.) Deserialize an object");

        //  Take user input
        int userChoice = scanner.nextInt();
        if (userChoice != 1 && userChoice != 2){
            System.err.println("Invalid input! Please enter 1 or 2 after relaunching.");
            System.exit(1);
        }
        return userChoice == 1;
    }
}