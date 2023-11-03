package Main;

import Objects.Simple;
import Objects.Complex;
import static Objects.TemplateCreator.*;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        org.jdom2.Document doc;
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
                    doc = Serializer.serialize(simple);
                }
                case 2 -> {
                    Complex complex = createComplex();
                    doc = Serializer.serialize(complex);
                }
                case 3 -> {
                    // Prompt user to either make int or char array
                    System.out.println("Would you like to create an int or char array? (int/char)");
                    String arrayChoice = scanner.next();
                    if (arrayChoice.equals("int")) {
                        int[] intArray = createIntArray();
                        doc = Serializer.serialize(intArray);
                    } else {
                        char[] charArray = createCharArray();
                        doc = Serializer.serialize(charArray);
                    }
                }
                case 4 -> {
                    Object[] objectArray = createObjectArray();
                    doc = Serializer.serialize(objectArray);
                }
                default -> {
                    HashSet<?> hashSet = createHashSet();
                    doc = Serializer.serialize(hashSet);
                }
            }
            try {
                XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
                StringWriter sw = new StringWriter();
                xmlOutputter.output(doc, sw);
            } catch (IOException e) {
                System.out.println("Error occurred while outputting XML document!");
                throw new RuntimeException(e);
            }
            Sender.send(doc);
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