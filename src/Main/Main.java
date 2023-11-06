package Main;

import Objects.Simple;
import Objects.Complex;
import static Objects.TemplateCreator.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        org.jdom2.Document doc;
        org.jdom2.Document doc2 = null; // Used in case of circular references
        // Create loop that keeps asking user for objects until they want to serialize, storing created objects in an arraylist
        while (true) {
            doc2 = null;
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
                    Complex complex = createReferencingComplex();
                    doc = Serializer.serialize(complex);
                }
                case 3 -> {
                    Complex complex1 = createCircularComplex();
                    Complex complex2 = createCircularComplex();
                    complex1.setObjectField(complex2);
                    complex2.setObjectField(complex1);
                    doc = Serializer.serialize(complex1);
                    doc2 = Serializer.serialize(complex2);
                }
                case 4 -> {
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
                case 5 -> {
                    Object[] objectArray = createObjectArray();
                    doc = Serializer.serialize(objectArray);
                }
                default -> {
                    HashSet<?> hashSet = createHashSet();
                    doc = Serializer.serialize(hashSet);
                }
            }
            try {
                System.out.println("Produced XML:");
                XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
                StringWriter sw = new StringWriter();
                xmlOutputter.output(doc, sw);
                StringBuffer sb = sw.getBuffer();
                System.out.println(sb.toString());
                if (doc2 != null) {
                    System.out.println("Produced XML for second object with circular reference:");
                    xmlOutputter.output(doc2, sw);
                    sb = sw.getBuffer();
                    System.out.println(sb.toString());
                }
            } catch (IOException e) {
                System.out.println("Error occurred while outputting XML document!");
                throw new RuntimeException(e);
            }
            Sender.send(doc);
            if (doc2 != null) {
                try {
                    // Give time for deserializer to process first object
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Sender.send(doc2);
            }
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
        System.out.println("3.) Pair of Circularly Referenced Objects");
        // TODO Allow selection of previous objects
        System.out.println("4.) Array of Primitives");
        System.out.println("5.) Array of Object References");
        System.out.println("6.) HashSet (Java Collection)");
        //  Take user input
        int userChoice = scanner.nextInt();
        if (userChoice < 1 || userChoice > 6){
            System.err.println("Invalid input! Please enter a number from 1 to 6 after relaunching.");
            System.exit(1);
        }
        return userChoice;
    }




}