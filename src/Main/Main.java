package Main;

import Objects.Simple;
import Objects.Complex;
import static Objects.TemplateCreator.*;
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
            // Log produced XML
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
        // TODO Allow selection of previous objects
        // TODO Circular reference
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




}