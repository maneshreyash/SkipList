
package ssm170730;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

//Driver program for skip list implementation.

public class SkipListDriver {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc;
        int i = 0;
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
        Iterator<Long> it = skipList.iterator();
        // Initialize the timer
        Timer timer = new Timer();

        while (!((operation = sc.next()).equals("End"))) {
            switch (operation) {
                case "Add": {
                    i++;
                    operand = sc.nextLong();
                    if(skipList.add(operand)) {
                        System.out.println(i + "\t Add " + " : " + operand + " " + true);
                        result = (result + 1) % modValue;
                    } else {
                        System.out.println(i + "\t Add " + " : " + operand + " " + false);
                    }
                    break;
                }
                case "Ceiling": {
                    i++;
                    operand = sc.nextLong();
                    returnValue = skipList.ceiling(operand);
                    System.out.println(i + "\t Ceiling " + returnValue);
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                        //System.out.println(result);
                    }
                    break;
                }
                case "First": {
                    i++;
                    returnValue = skipList.first();
                    System.out.println(i + "\t First " + returnValue);
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                    }
                    break;
                }
                case "Get": {
                    i++;
                    int intOperand = sc.nextInt();
                    returnValue = skipList.get(intOperand);
                    System.out.println(i + "\t Get " + returnValue);
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                    }
                    break;
                }
                case "Last": {
                    i++;
                    returnValue = skipList.last();
                    System.out.println(i + "\t Last " + returnValue);
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                    }
                    break;
                }
                case "Floor": {
                    i++;
                    operand = sc.nextLong();
                    returnValue = skipList.floor(operand);
                    System.out.println(i + "\tFloor =  " + returnValue);
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                    }
                    break;
                }
                case "Remove": {
                    i++;
                    operand = sc.nextLong();
                    if (skipList.remove(operand) != null) {
                        result = (result + 1) % modValue;
                        System.out.println(i + "\t Remove " + " : " + operand + " " + true);
                    } else {
                        System.out.println(i + "\t Remove " + " : " + operand + " " + false);
                    }
                    break;
                }
                case "Contains": {
                    i++;
                    operand = sc.nextLong();
                    if (skipList.contains(operand)) {
                        System.out.println(i + "\t Contains " + " : " + operand + " " + true);
                        result = (result + 1) % modValue;
                        //System.out.println(result);
                    } else {
                        System.out.println(i + "\t Contains " + " : " + operand + " " + false);
                    }
                    break;
                }
                case "p": { //Print list
                    skipList.printList(skipList);
                break;
                }
                case "pa": { //Prints elements with last array inside it
                    skipList.printListAmeya();
                    break;
                }
                case "gL": { //getLogEntry/getLinear
                    int intOperand = sc.nextInt();
                    System.out.println(skipList.getLinearEntry(intOperand).element);
                    //System.out.println(skipList.getLogEntry(intOperand).element);
                    break;
                }
                case "reb": { //Contains
                    skipList.rebuild();
                    result = (result + 1) % modValue;
                    break;
                }
                case "ni": { //Contains
                    it.next();
                    break;
                }
                case "ri": { //Contains
                    it.remove();
                    break;
                }

            }
        }

        // End Time
        timer.end();

        System.out.println(result);
        System.out.println(timer);
    }
}