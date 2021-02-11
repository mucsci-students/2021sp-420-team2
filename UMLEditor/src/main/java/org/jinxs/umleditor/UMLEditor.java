package org.jinxs.umleditor;

import java.util.ArrayList;

public class UMLEditor {
    
    private ArrayList<UMLClass> classes;

    public UMLEditor() {
        classes = new ArrayList<UMLClass>();
    }

    // Adds a class to the list of classes given a new class name that is not already in use
    public void addClass(String className) {
        classes.ensureCapacity(classes.size() + 1);
        // Loop through the list of classes to ensure that a duplicate class name is not trying to be inserted
        for (int i = 0; i < classes.size(); ++i) {
            if (classes.get(i).name.equals(className)) {
                System.out.println("The requested class name already exists");
                return;
            }
        }
        // Add the new class to the list of classes
        classes.add(new UMLClass(className));
        System.out.println("Class \"" + className + "\" was added successfully");
    }

    // Deletes a class from the list of classes given a class name that exists
    public void deleteClass(String className) {
        // Loop through the list of classes to ensure that the requested class to delete exists
        for (int i = 0; i < classes.size(); ++i) {
            if (classes.get(i).name.equals(className)) {
                // Save the requested class to delete in order to delete relationships first
                UMLClass deletedClass = classes.get(i);
                // Save the list of relationships
                ArrayList<ArrayList<String>> deletedRels = deletedClass.getRels();
                // Loop through the list of relationships and delete each one from the related class
                for (int j = 0; j < deletedRels.size(); ++j) {
                    delRel(deletedClass.name, deletedRels.get(j).get(0));
                }
                // Finally remove the requested class to delete from the class list
                classes.remove(i);
                System.out.println("Class \"" + className + "\" was deleted successfully");
                return;
            }
        }
        System.out.println("The requested class to delete does not exist");
    }

    public void renameClass(String oldName, String newName) {

    }

    // Adds a relationship between class1 and class2 where class1 is the source
    // and class2 is the destination
    public void addRel(String class1, String class2) {
        // Make sure each given class name is unique
        if (class1.equals(class2)) {
            System.out.println("Class names must be different");
            return;
        }

        // Store classes when found in the class list
        UMLClass c1 = null;
        UMLClass c2 = null;

        // Look through the class list for both classes
        for (int i = 0; i < classes.size(); ++i) {
            if (classes.get(i).name.equals(class1) && c1 == null) {
                c1 = classes.get(i);
            }
            if (classes.get(i).name.equals(class2) && c2 == null) {
                c2 = classes.get(i);
            }
        }

        if (c1 == null || c2 == null){
            System.out.println("Class does not exsist");
            return;
        }

        // Get the current relationships from class1
        ArrayList<ArrayList<String>> c1Rels = c1.getRels();

        // Ensure a relationship between class1 and class2 does not already exist
        for (int i = 0; i < c1Rels.size(); ++i) {
            if (c1Rels.get(i).get(0).equals(class2)) {
                // Notify user of already existing relationship
                System.out.println("Relationship between \"" + class1 + "\" and \"" + class2 + "\" already exists");
                return;
            }
        }

        // Add the relationship to both class's rel lists
        if (c1 != null && c2 != null) {
            c1.addRel(class2, false);
            c2.addRel(class1, true);
        }
        // Notify user of successful relationship addition
        System.out.println("Relationship between \"" + class1 + "\" and \"" + class2 + "\" added successfully");
    }

    public void delRel(String class1, String class2){
        // Make sure each given class name is unique
        if (class1.equals(class2)) {
            System.out.println("Class names must be different");
            return;
        }

        // Store classes when found in the class list
        UMLClass c1 = null;
        UMLClass c2 = null;

        // Look through the class list for both classes
        for (int i = 0; i < classes.size(); ++i) {
            if (classes.get(i).name.equals(class1) && c1 == null) {
                c1 = classes.get(i);
            }
            if (classes.get(i).name.equals(class2) && c2 == null) {
                c2 = classes.get(i);
            }
        }

        if (c1 == null || c2 == null){
            System.out.println("Class does not exsist");
            return;
        }

        // Get the current relationships from class1
        ArrayList<ArrayList<String>> c1Rels = c1.getRels();

        boolean found = false;

        for (int i = 0; i < c1Rels.size(); ++i) {
            if (c1Rels.get(i).get(0).equals(class2)){
                found = true;
            }
        }

        if (found == false){
            System.out.println("Relationship between \"" + class1 + "\" and \"" + class2 + "\" does not exsist");
            return;
        }

        else {
            c1.deleteRel(c2.name);
            c2.deleteRel(c1.name);
            System.out.println("Relationship deleted");
        }
    }

    // Adds an attribute (attrName) to a given class (className) if it exists,
    // and the class does not currently have that attribute
    public void addAttr(String className, String attrName) {
        // Will store whether the given "className" exists
        boolean classExists = false;
        // Will store the result of attempting to add an attribute to the class
        boolean attrAdded = false;

        // Search through the class list to find the desired class
        for (int i = 0; i < classes.size(); ++i) {
            // If the class is found, attempt to add the desired attribute
            if (classes.get(i).name.equals(className)) {
                classExists = true;

                UMLClass currClass = classes.get(i);
                // True if added succesfully, false if duplicate
                attrAdded = currClass.addAttr(attrName);
            }
        }

        // Notify the user of the resuls of the attribute addition

        if (!classExists) {
            System.out.println("Class \"" + className + "\" does not exist");
            return;
        }

        if (attrAdded) {
            System.out.println("Attribute \"" + attrName + "\" added to class \"" + className + "\" succesfully");
        } else {
            System.out.println("Attribute \"" + attrName + "\" is already an attribute of class \"" + className);
        }
    }

    public void delAttr() {

    }

    public void renameAttr() {

    }

    // Prints the relationships of a given classname 
    public void printRel(String className){
        //intialize an empty arraylist which will hold our relationships
        ArrayList<ArrayList<String>> rels = null; 

        /*
        loop will go through the class list, find the given classname and populatates our relationship
        arraylist with relationships from the given classname
        */
        for (int i = 0; i < classes.size(); ++i) {
            if (classes.get(i).name.equals(className)) {
                rels = classes.get(i).getRels();
                break;  
            }
        }
        System.out.println(className + " is a source to these classes: ");
        
        // Determines if our class is source prints out the relationships
        for(int i = 0; i < rels.size(); ++i){
            if(rels.get(i).get(1).equals("src")){
                System.out.println(rels.get(i).get(0));
            }
        }
        System.out.println(className + " is a destination for these classes: ");
    
        // Determines if our class is destination and prints out the relationships
        for(int i = 0; i < rels.size(); ++i){
            if(rels.get(i).get(1).equals("dest")){
                System.out.println(rels.get(i).get(0));
            }
        }
    }

    public void printClassContents(String className) {

        // finds the class of the specified names
        UMLClass printClass = null;
        for (int i = 0; i < classes.size(); ++i) {
            if (classes.get(i).name.equals(className)) {
                printClass = classes.get(i);
                break;
            }
        }

        // returns if the class does not exsist and prints an error
        if (printClass == null) {
            System.out.println("Class does not exsist");
            return;
        }

        // prints out the class's name
        System.out.println("Class: " + className);

        // prints out the class's attributes
        System.out.println("Attributes: ");
        ArrayList<String> attrs = printClass.getAttrs();
        for (int i = 0; i < attrs.size(); ++i) {
            System.out.println(attrs.get(i));
        }

        // prints out the class's relationships
        printRel(className);

        return;
    }

    public void printClassList() {
        // Prints out the name of each class
        for (int i = 0; i < classes.size(); ++i) {
            System.out.println(classes.get(i).name);
        }
    }
}