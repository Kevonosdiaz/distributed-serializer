package Main;

import Objects.Simple;
import Objects.Complex;
import org.jdom2.Document;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String addr = "localhost";
        int port = 8765;
        Sender sender = new Sender(addr, port);
        Scanner scanner = new Scanner(System.in);
        ArrayList<Object> toSerialize = new ArrayList<>();
        // Create loop that keeps asking user for objects until they want to serialize, storing created objects in an arraylist
        while (true) {
            System.out.println("Would you like to create an object? (y/n)");
            String userChoice = scanner.next();
            if (!userChoice.equals("y")) break;
            // Ask user for object type
            int objChoice = userChoiceObject();
            // Create object based on user choice
            switch (objChoice) {
                case 1 -> {
                    Simple simple = createSimple();
                    toSerialize.add(simple);
                }
                case 2 -> {
                    Complex complex = createComplex();
                    toSerialize.add(complex);
                }
                case 3 -> {
                    // Prompt user to either make int or char array
                    System.out.println("Would you like to create an int or char array? (int/char)");
                    String arrayChoice = scanner.next();
                    if (arrayChoice.equals("int")) {
                        int[] intArray = createIntArray();
                        toSerialize.add(intArray);
                    } else {
                        char[] charArray = createCharArray();
                        toSerialize.add(charArray);
                    }
                }
                case 4 -> {
                    Object[] objectArray = createObjectArray();
                    toSerialize.add(objectArray);
                }
                default -> {
                    HashSet<?> hashSet = createHashSet();
                    toSerialize.add(hashSet);
                }
            }
        }

        if (toSerialize.isEmpty()) {
            System.out.println("No objects created! Exiting...");
            System.exit(0);
        }
        // Establish network connection and send over the XML documents

        // Make use of toSerialize() to keep serializing
        System.out.println("Beginning serializiation of created objects...");
        for (Object object : toSerialize) {
            Document doc = Serializer.serialize(object);
            // Send doc over network
        }
        System.out.println("Serialization complete! Exiting...");
        System.exit(0);
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

    private static Object[] createObjectArray() {
        // Ask user for size of object array
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the size of the array:");
        int size = scanner.nextInt();
        Object[] objectArray = new Object[size];
        // Loop size times, asking user to add either a simple or complex object
        for (int i = 0; i < size; i++) {
            System.out.println("Would you like to add a Simple or Complex object? (simple/complex)");
            String userChoice = scanner.next();
            if (userChoice.equals("simple")) {
                Simple simple = createSimple();
                objectArray[i] = simple;
            } else {
                Complex complex = createComplex();
                objectArray[i] = complex;
            }
        }
        return objectArray;
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
        } while (Objects.equals(dataType, "int") || Objects.equals(dataType, "double") || Objects.equals(dataType, "char")) ;
            // Keep getting input until user enters a valid data type

        if (Objects.equals(dataType, "int")) return createHashSetInt();
        if (Objects.equals(dataType, "double")) return createHashSetDouble();
        return createHashSetChar();
    }

    // Consider just merging or extracting from all three since only the loop differs

    private static HashSet<Integer> createHashSetInt() {
        // Ask user for size of HashSet
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the size of the HashSet:");
        int size = scanner.nextInt();
        HashSet<Integer> hashSet = new HashSet<>(size);
        for (int i = 0; i < size; i++) {
            System.out.println("Please enter an integer value for the HashSet at index " + i + ":");
            hashSet.add(scanner.nextInt());
        }
        return hashSet;
    }

    private static HashSet<Double> createHashSetDouble() {
        // Ask user for size of HashSet
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the size of the HashSet:");
        int size = scanner.nextInt();
        HashSet<Double> hashSet = new HashSet<>(size);
        for (int i = 0; i < size; i++) {
            System.out.println("Please enter a double value for the HashSet at index " + i + ":");
            hashSet.add(scanner.nextDouble());
        }
        return hashSet;
    }

    private static HashSet<Character> createHashSetChar() {
        // Ask user for size of HashSet
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the size of the HashSet:");
        int size = scanner.nextInt();
        HashSet<Character> hashSet = new HashSet<>(size);
        for (int i = 0; i < size; i++) {
            System.out.println("Please enter a character value for the HashSet at index " + i + ":");
            hashSet.add(scanner.next().charAt(0));
        }
        return hashSet;
    }


}