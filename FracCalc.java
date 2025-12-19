// Lucky Kadiam
// Period 6
// Fraction Calculator Project

import java.util.*;

// TODO: Description of what this program does goes here.
// This program is a calculator that can perform arithmetic expressions for 
// addition, subtraction, multiplication, and division. Using multiple helper methods
// to perform specific steps and purposes, loops, if-else statements, symbols, and the
// String class, the program is able to produce mixed and simplified fraction answers, 
// provide help when the user asks, and quits the program when inputted by user.
public class FracCalc {

   // It is best if we have only one console object for input
   public static Scanner console = new Scanner(System.in);
   
   // This main method will loop through user input and then call the
   // correct method to execute the user's request for help, test, or
   // the mathematical operation on fractions. or, quit.
   // DO NOT CHANGE THIS METHOD!!
   public static void main(String[] args) {
   
      // initialize to false so that we start our loop
      boolean done = false;
      
      // When the user types in "quit", we are done.
      while (!done) {
         // prompt the user for input
         String input = getInput();
         
         // special case the "quit" command
         if (input.equalsIgnoreCase("quit")) {
            done = true;
         } else if (!UnitTestRunner.processCommand(input, FracCalc::processCommand)) {
        	   // We allowed the UnitTestRunner to handle the command first.
            // If the UnitTestRunner didn't handled the command, process normally.
            String result = processCommand(input);
            
            // print the result of processing the command
            System.out.println(result);
         }
      }
      
      System.out.println("Goodbye!");
      console.close();
   }

   // Prompt the user with a simple, "Enter: " and get the line of input.
   // Return the full line that the user typed in.
   public static String getInput() {
      // TODO: Implement this method
      System.out.print("Enter: ");
      String input = console.nextLine();
      return input;
   }
   
   // processCommand will process every user command except for "quit".
   // It will return the String that should be printed to the console.
   // This method won't print anything.
   // DO NOT CHANGE THIS METHOD!!!
   public static String processCommand(String input) {

      if (input.equalsIgnoreCase("help")) {
         return provideHelp();
      }
      
      // if the command is not "help", it should be an expression.
      // Of course, this is only if the user is being nice.
      return processExpression(input);
   }
   
   // Lots work for this project is handled in here.
   // Of course, this method will call LOTS of helper methods
   // so that this method can be shorter.
   // This will calculate the expression and RETURN the answer.
   // This will NOT print anything!
   // Input: an expression to be evaluated
   //    Examples: 
   //        1/2 + 1/2
   //        2_1/4 - 0_1/8
   //        1_1/8 * 2
   // Return: the fully reduced mathematical result of the expression
   //    Value is returned as a String. Results using above examples:
   //        1
   //        2 1/8
   //        2 1/4
   public static String processExpression(String input) {
      // TODO: implement this method!
      Scanner parser = new Scanner(input);
      char op = ' '; int whole1 = 0; int num1 = 0; int den1 = 1; int whole2 = 0; int num2 = 0; int den2 = 1; boolean firstFrac = true;
      while (parser.hasNext()) {
         String token = parser.next();
         if (token.equals("+")||token.equals("-")||token.equals("*")||token.equals("/")) {
            op = getOperator(token);
         } else {
            int underscore = token.indexOf("_"); int slash = token.indexOf("/");
            int whole = getWhole(underscore, slash, token); // gets whole number
            int num = getNumerator(underscore, slash, token); // gets numerator
            int den = getDenominator(underscore, slash, token); // gets denominator
            if (firstFrac) { // stores values for first fraction
               whole1 = whole; num1 = num; den1 = den;
               firstFrac = false;
            } else { // stores values for second fraction
               whole2 = whole; num2 = num; den2 = den;
            }
         }
      }
      if (whole1 < 0) { // checks for negative whole numbers for first number
         num1 = whole1 * den1 - num1; 
      } else {
         num1 = whole1 * den1 + num1;
      }
      if (whole2 < 0) { // checks for negative whole numbers for second number
         num2 = whole2 * den2 - num2; 
      } else {
         num2 = whole2 * den2 + num2;
      }
      int resultDen = 0; int resultNum = 0;
      if (op == '+') { // finds numerator and denominator for addition 
         resultDen = getFinalDenominator(den1, den2);
         resultNum = getFinalNumerator(num1, den1, num2, den2);
      } else if (op == '-') { // finds numerator and denominator for subtraction
         resultNum = subtractionNumerator(den1, den2, num1, num2);
         resultDen = getFinalDenominator(den1, den2);
      } else if (op == '*') { // finds numerator and denominator for multiplication
         resultNum = multiplicationNumerator(num1, num2);
         resultDen = multiplicationDen(den1, den2);
      } else if (op == '/') { // finds numerator and denominator for division
         resultNum = divisionNumerator(num1, den2);
         resultDen = divisionDenominator(num2, den1);
      }
      if (resultDen == 0) { // Gives message when denominator is zero 
         System.out.println("Denominator cannot be zero.");
      }
      if (resultDen != 0) { // continues code when demoniator is not zero
         int gcf = getGCF(resultNum, resultDen);
         resultNum /= gcf;
         resultDen /= gcf;
         if ((resultNum < 0 && resultDen < 0) || (resultDen < 0 && resultNum > 0)) {
            resultNum *= -1;
            resultDen *= -1;
         }
         return getFormattedAnswer(resultDen, resultNum); 
      }
      return "";
   }
      
   
   // Takes in a parameter of the operation as a string
   // Gets the operator and returns it as a char
   public static char getOperator(String token) {
      char op = token.charAt(0);
      return op;
   }

   // Parameters in take indexes of symbols
   // to find and return the whole numbers of the fractions
   public static int getWhole(int underscore, int slash, String token) {
      int whole = 0;
      if (underscore != -1 && slash != -1) {
         String whole1 = token.substring(0,underscore);
         whole = Integer.valueOf(whole1);
      }
      else if (slash != -1 && underscore == -1) {
         whole = 0;
      }

      else {
         whole = Integer.valueOf(token);
      }
      return whole;
   }

   // Parameters in take indexes of symbols
   // to find and return the numerator of the fraction
   public static int getNumerator(int underscore, int slash, String token) {
      int numerator = 0;
      if (underscore != -1 && slash != -1) {
         String numerator1 = token.substring(underscore + 1, slash);
         numerator = Integer.valueOf(numerator1);
      }

      else if (slash != -1 && underscore == -1) {
         String numerator1 = token.substring(0, slash);
         numerator = Integer.valueOf(numerator1);
      }
      return numerator;
      }

   // Parameters in take indexes of symbols
   // to find and return the denominator of the fraction
   public static int getDenominator(int underscore, int slash, String token) {
      int den = 1;
      if (underscore != -1 && slash != -1) {
         String den1 = token.substring(slash + 1);
         den = Integer.valueOf(den1); 
      }

      else if (slash != -1 && underscore == -1) {
         String den1 = token.substring(slash + 1);
         den = Integer.valueOf(den1);
      }

      return den;
   }

   // Parameters take the denominators of each fraction
   // and finds and returns the final denominator based
   // on if the denominators were already the same
   public static int getFinalDenominator(int den1, int den2 ) {
      int finalDen = 0;
      if (den1 == den2) {
         finalDen = den1;
      }

      else {
         finalDen = den1 * den2;
      }
      return finalDen;
   }

   // Parameters take the numerators and denominators of each fraction
   // and if the denominators are the same the numerators are added, if not
   // numerators and denominators are cross multiplied to get both fractions
   // with the same denominator. Returns the final numerator.
   public static int getFinalNumerator(int num1, int den1, int num2, int den2) {
      int finalNumerator = 0;
      if (den1 == den2) {
         finalNumerator = num1 + num2;
      }
      else {
         num1 = num1 * den2;
         num2 = num2 * den1;
         finalNumerator = num1 + num2;
      }
      return finalNumerator;
   }

   // Takes in the final denominator and numerator as parameters
   // and formats the fractions in mixed fractions and returns 
   // the final answer as a string.
   public static String getFormattedAnswer(int den, int num) {
      if (num == 0) { // returns 0 if numerator is 0.
         return "0";
      }
      
      boolean negative = num < 0; 
      num = Math.abs(num);

      int whole = num / den;
      int remainder = Math.abs(num % den); // finds remainder for mixed fraction numerator

      String result = "";

      if (whole != 0 && remainder != 0) {
         result = whole + " " + remainder + "/" + den;
      } else if (whole != 0) {
         result += whole;
      } else {
         result = remainder + "/" + den;
      }

      if (negative){ // adds negative symbol when needed
         result = "-" + result;
      }

      return result;
   }

   // Takes in the final numerator and denominator as parameters
   // and finds the greatest common factor between them
   // to simplify the final fraction in the processExpression method.
   // returns the GCD to the method.
   public static int getGCF(int num, int den) {
      int a = Math.abs(num);
      int b = Math.abs(den);

      while ( b != 0) {
         int remainder = a % b;
         a = b;
         b = remainder;
      }
      return a;
   }

   // Takes both denominators and numerators to perform
   // subtraction operation and return the numerator answer.
   public static int subtractionNumerator(int den1, int den2,int num1, int num2) {
      int finalNumerator = 0;
      if (den1 == den2) { // checks if denominators are the same
         finalNumerator = num1 - num2;
      }
      else {
         num1 = num1 * den2;
         num2 = num2 * den1;
         finalNumerator = num1 - num2;
      }
      return finalNumerator;
   }

   // Takes both numerators as parameters and multiplies them
   // to find and return numerator answer when operation is
   // multiplication.
   public static int multiplicationNumerator(int num1, int num2) {
      int finalNumerator = num1 * num2;
      return finalNumerator;
   }

   // Takes both denominators as parameters and multiplies them
   // to find and return the denominator answer when the operation is
   // multiplication.
   public static int multiplicationDen(int den1, int den2) {
      int finalDen = den1 * den2;
      return finalDen;
   }

   // Takes numerator of first fraction and denominator of 
   // second fraction and multiplies them to find and return
   // the numerator answer when the operation is division.
   public static int divisionNumerator(int num1, int den2) {
      int finalNumerator = num1 * den2;
      return finalNumerator;
   }

   // Takes numerator of second fraction and denominator of first fraction
   // and multiplies them to find and return the denominator answer
   // when the operation is division.
   public static int divisionDenominator(int num2, int den1) {
      int finalDen = num2 * den1;
      return finalDen;
   } 
   
   
   // Returns a string that is helpful to the user about how
   // to use the program. These are instructions to the user.
   public static String provideHelp() {
      // TODO: Update this help text!
     
      String help = "Here is your help.";
      help += "Input one or two numbers into the program and one operator to compute with the numbers.";
      
      return help;
   }
}