
package ssm170730;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//Driver program for skip list implementation.

public class SkipListDriver {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc;
        if (args.length > 0) {
            File file = new File(args[0]);
            sc = new Scanner(file);
        } else {
            sc = new Scanner(System.in);
        }
        String operation = "";
        long operand = 0;
        int modValue = 999983;
        long result = 0;
        Long returnValue = null;
        SkipList<Long> skipList = new SkipList<>();
        // Initialize the timer
        Timer timer = new Timer();

        while (!((operation = sc.next()).equals("End"))) {
            switch (operation) {
                case "a": { //Add
                    operand = sc.nextLong();
                    if(skipList.add(operand)) {
                        result = (result + 1) % modValue;
                    } else {
                        System.out.println("Element already present");
                    }
                    break;
                }
                case "ce": { //Ceiling
                    operand = sc.nextLong();
                    returnValue = skipList.ceiling(operand);
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                    }
                    break;
                }
                case "f": { //First
                    returnValue = skipList.first();
                    System.out.println(returnValue);
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                    }
                    break;
                }
                case "g": {//Get
                    int intOperand = sc.nextInt();
                    returnValue = skipList.get(intOperand);
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                    }
                    break;
                }
                case "l": { // Last
                    returnValue = skipList.last();
                    System.out.println(returnValue);
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                    } else {
                        System.out.println("null");
                    }
                    break;
                }
                case "fl": { //Floor
                    operand = sc.nextLong();
                    returnValue = skipList.floor(operand);
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                        //System.out.println(result);
                    } else {
                        System.out.println("null");
                    }
                    break;
                }
                case "r": { //Remove
                    operand = sc.nextLong();
                    if (skipList.remove(operand) != null) {
                        result = (result + 1) % modValue;
                    }
                    break;
                }
                case "c": { //Contains
                    operand = sc.nextLong();
                    if (skipList.contains(operand)) {
                        System.out.println("Found");
                        result = (result + 1) % modValue;
                    } else {
                        System.out.println("Not found");
                    }

                    break;
                }
                case "p": { //Print list
                    skipList.printListAmeya();
                }
                break;


            }
        }

        // End Time
        timer.end();

        System.out.println(result);
        System.out.println(timer);
    }
}