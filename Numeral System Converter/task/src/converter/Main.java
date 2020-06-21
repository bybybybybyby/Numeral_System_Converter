package converter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int sourceRadix = 0;
        int targetRadix = 0;
        String sourceNumber = null;

        // Get Source Radix
        System.out.println("Input Source Radix: ");
        String sourceRadixAsString = sc.nextLine();
        if (sourceRadixAsString.matches("\\d+")) {
            sourceRadix = Integer.valueOf(sourceRadixAsString);
        } else {
            System.out.println("ERROR: Need to enter an int!");
            return;
        }
        if (sourceRadix < 1 || sourceRadix > 36) {
            System.out.println("ERROR: Radix out of range!");
            return;
        }

        // Get Source Number
        System.out.println("Input Source Number: ");
        if (sourceRadix > 10) {
            char charAllowed = 'a';
            for (int i = 11; i < sourceRadix; i++) {
                charAllowed++;
                System.out.println("charAllowed:" + charAllowed);
            }

            // Creating regex to fix allowable characters based on source Radix
            String replaceTemplate = "[a-z0-9]+\\.?[a-z0-9]*";
            String regexForRadix = replaceTemplate.replace("z", String.valueOf(charAllowed));

             sourceNumber = sc.nextLine();
            if (!sourceNumber.matches(regexForRadix)) {
                System.out.println("ERROR: Number not valid for radix!");
                return;
            }
        } else {
             sourceNumber = sc.nextLine();
            if (!sourceNumber.matches("[0-9]+\\.?[0-9]*")) {
                System.out.println("ERROR: Need to enter a valid number!");
                return;
            }
        }

        // Get Target Radix
        System.out.println("Input Target Radix: ");
        String targetRadixAsString = sc.nextLine();
        if (targetRadixAsString.matches("\\d+")) {
            targetRadix = Integer.valueOf(targetRadixAsString);
        } else {
            System.out.println("ERROR: Need to enter an int!");
            return;
        }
        if (targetRadix < 1 || targetRadix > 36) {
            System.out.println("ERROR: Radix out of range!");
            return;
        }

        String integerPart = "";
        String fractionalPart = "";

        int indexOfPoint = sourceNumber.indexOf(".");

        if (indexOfPoint == -1) {   // There is no fractional part
            integerPart = sourceNumber;
        } else {
            // Separate integer and fractional parts
            integerPart = sourceNumber.substring(0, indexOfPoint);
            fractionalPart = sourceNumber.substring(indexOfPoint);  // includes decimal point
        }

        fractionalPart = convertFractionalToDecimal(fractionalPart, sourceRadix);

        // Convert integer and fractional to target Radixes
        String integerConverted = convertRadix(sourceRadix, integerPart, targetRadix);
        String fractionalConverted = convertFractionalRadix(sourceRadix, fractionalPart, targetRadix);

        System.out.println(integerConverted + "." + fractionalConverted);
    }

    public static String convertFractionalToDecimal(String fractionalPart, int sourceRadix) {
        double sum = 0.0;
        int workingInt = 0;

        for (int i = 1; i < fractionalPart.length(); i++) {

            // Get one symbol
            String current = fractionalPart.substring(i, i + 1);

            // Convert to decimal base if it is a letter.
            if (current.matches("[a-z]")) {
                workingInt = Integer.parseInt(current, sourceRadix);
            } else {
                workingInt = Integer.parseInt(current);
            }

            sum += (workingInt / Math.pow(sourceRadix, i));
        }

        return Double.toString(sum);
    }

    public static String convertFractionalRadix(int sourceRadix, String sourceNumber, int targetRadix) {
        StringBuilder sb = new StringBuilder();

        double workingNumber = Double.parseDouble(sourceNumber);

        // Converting fractional from decimal to targetRadix, to 5 places
        for (int i = 0; i < 5; i++) {
            workingNumber *= targetRadix;
            int integerPart = (int) workingNumber;
            sb.append(Integer.toString(integerPart, targetRadix));

            workingNumber -= integerPart;  // Take new decimal portion only
        }
        return sb.toString();
    }

    public static String convertRadix(int sourceRadix, String sourceNumber, int targetRadix) {
        int decNumber = 0;
        String result = "";

        // First, convert to decimal if it isn't
        if (sourceRadix == 1) {   // Convert source radix of 1 manually
            decNumber = sourceNumber.length();
        } else if (sourceRadix != 10) {
            try {
                decNumber = Integer.parseInt(sourceNumber, sourceRadix);
            } catch (NumberFormatException e) {
                System.out.println("Error: Impossible Radix conversion with Source number!! : " + e);
            }
        } else {
            decNumber = Integer.parseInt(sourceNumber);
        }

        // Convert to Target radix
        if (targetRadix <= 1) {
            for (int i = 0; i < Integer.parseInt(sourceNumber); i++) {
                result += "1";
            }
        } else {
            result = Integer.toString(decNumber, targetRadix);
        }

        return result;
    }
}
