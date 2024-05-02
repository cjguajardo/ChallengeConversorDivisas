import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    private static final String STR_OPTIONS="Available options: ";
    private static final String STR_OPTION_INPUT="Type your option: ";
    private static final String STR_EXIT="See ya!";
    private static final String STR_INVALID_OPTION="Invalid option: ";
    private static final String STR_CONTINUE="\nDo you want to convert another currency? (Y/n)";
    private static final String STR_TEMPLATE_AMOUNT="Insert the [FROM] you want to convert: ";

    public static void main(String[] args) {
        String[] options = {
                "1) USD >>> ARS", "2) ARS >>> USD",
                "3) USD >>> BRL", "4) BRL >>> USD",
                "5) USD >>> COP", "6) COP >>> USD",
                "7) USD >>> CLP", "8) CLP >>> USD",
                "9) USD >>> GBP", "10) GBP >>> USD",
                "11) USD >>> EUR", "12) EUR >>> USD",
                "l) Get historical log",
                "e) Exit"
        };
        String option = "0";

        Scanner scanner = new Scanner(System.in);

        Storage storage = new Storage();

        while(!option.equals("e")){
            System.out.println(STR_OPTIONS);
            for (String s : options) {
                System.out.println("\t"+s);
            }
            System.out.println(STR_OPTION_INPUT);
            option = scanner.nextLine().toLowerCase();

            if(option.equals("e")) {
                System.out.println(STR_EXIT);
            }else if(option.equals("l")) {
                System.out.println("------------------------------------ LOG ------------------------------------");
                try {
                    ResultSet result = storage.getLog();
                    System.out.println("-----------------------------------------------------------------------------");
                    System.out.println(" DATE\t\t| CONVERSION\t| AMOUNT\t| CONVERTED ");
                    System.out.println("-----------------------------------------------------------------------------");
                    while (result.next()) {
                        String date = result.getString("date");
                        String from  = result.getString("from");
                        String to = result.getString("to");
                        double amount = result.getDouble("amount");
                        double converted = result.getDouble("converted");

                        System.out.print(date+"\t|");
                        System.out.print("\t"+from+" >>> "+to+"\t|");
                        System.out.print("\t"+amount+"\t|");
                        System.out.println("\t"+converted);
                        System.out.println("-----------------------------------------------------------------------------");
                    }
                    System.out.println("\n");
                }catch(SQLException e) {
                    System.out.println(e.getMessage());
                }
            }else{
                try {
                    int optionIndex = Integer.parseInt(option);
                    if (optionIndex >= 1 && optionIndex < options.length) {
                        String optionString = options[Integer.parseInt(option) - 1];
                        ConvertOptions opt = new ConvertOptions(optionString);
                        System.out.println(STR_TEMPLATE_AMOUNT.replace("[FROM]", opt.getFrom()));
                        double amount = Double.parseDouble(scanner.nextLine());
                        String converted = opt.convertToCurrency(amount);
                        System.out.println(opt.getFrom() + " " + opt.getFormattedAmount(opt.getFrom(), amount) + " is equivalent to " + opt.getTo() + ": " + converted);
                        storage.insertFromConvertOptions(opt);

                        System.out.println(STR_CONTINUE);
                        option = scanner.nextLine().toLowerCase();
                        if (option.equals("n")) {
                            option = "e";
                        }
                    } else {
                        System.out.println(STR_INVALID_OPTION);
                    }
                }catch (NumberFormatException e) {
                    System.out.println(STR_INVALID_OPTION);
                }
            }
        }
    }
}