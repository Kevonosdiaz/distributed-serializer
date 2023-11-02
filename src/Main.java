import Objects.Simple;
import Objects.Complex;

import java.util.HashSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Create loop that keeps asking user for objects until they want to serialize, storing created objects in an arraylist
        while (true) {
            System.out.println("Would you like to create an object? (y/n)");
            String userChoice = scanner.next();
            if (userChoice.equals("y")) {
                // Ask user for object type
                int objChoice = userChoiceObject();
                // Create object based on user choice
                switch (objChoice) {
                    case 1 -> {
                        Simple simple = createSimple();
                    }
                    case 2 -> {
                        Complex complex = createComplex();
                    }
                    case 3 -> {
                        int[] intArray = createIntArray();
                    }
                    case 4 -> {
                        char[] charArray = createCharArray();
                    }
                    default -> {
                        HashSet<?> hashSet = createHashSet();
                    }
                }
            } else {
                break;
            }
        }
    }

    private static int userChoiceObject() {
        Scanner scanner = new Scanner(System.in);
        // Print menu options
        System.out.println("Please select one of the following object options:");
        System.out.println("1.) Simple Object (storing primitives only)");
        System.out.println("2.) Complex Object (storing primitives and object refs)");
        System.out.println("3.) Array of Primitives");
        System.out.println("4.) Array of Object References");
        System.out.println("5.) HashSet of Ints (Java Collection)");
        //  Take user input
        int userChoice = scanner.nextInt();
        if (userChoice < 1 || userChoice > 5){
            System.err.println("Invalid input! Please enter a number from 1 to 5 after relaunching.");
            System.exit(1);
        }
        return userChoice;
    }

    private static Simple createSimple() {
        // Ask user for params of Simple object
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the following parameters for the Simple object:");
        System.out.println("Please enter an integer value for the int field:");
        int intField = scanner.nextInt();
        System.out.println("Please enter a double value for the double field:");
        double doubleField = scanner.nextDouble();
        System.out.println("Please enter a character value for the char field:");
        char charField = scanner.next().charAt(0);

        // Create Simple object
        return new Simple(intField, doubleField, charField);
    }

    private static Complex createComplex() {
        // Ask user for params of Complex object
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the following parameters for the Complex object:");
        System.out.println("Please enter an integer value for the int field:");
        int intField = scanner.nextInt();
        System.out.println("Please enter a double value for the double field:");
        double doubleField = scanner.nextDouble();
        System.out.println("Please enter a string value for the string field:");
        String stringField = scanner.next();
        Simple simpleField = createSimple();
        return new Complex(intField, doubleField, stringField, simpleField);
    }

    private static int[] createIntArray() {
        // Ask user for size of int array
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the size of the array:");
        int size = scanner.nextInt();
        int[] intArray = new int[size];
        for (int i = 0; i < size; i++) {
            System.out.println("Please enter an integer value for the array at index " + i + ":");
            intArray[i] = scanner.nextInt();
        }
        return intArray;
    }

    private static char[] createCharArray() {
        // Ask user for size of char array
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the size of the array:");
        int size = scanner.nextInt();
        char[] charArray = new char[size];
        for (int i = 0; i < size; i++) {
            System.out.println("Please enter a character value for the array at index " + i + ":");
            charArray[i] = scanner.next().charAt(0);
        }
        return charArray;
    }

    private static HashSet<?> createHashSet() {
        // Prompt user for data type of HashSet
        Scanner scanner = new Scanner(System.in);
        String dataType = "";
        do {
            System.out.println("Please enter the data type of the HashSet (int, double, or char):");
            dataType = scanner.next();
        } while (true) {
            // Keep getting input until user enters a valid data type
            if (dataType == "int" || dataType == "double" || dataType == "char") {
                break;

        }

    }


}