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
                "e) Exit"
        };
        String option = "0";

        Scanner scanner = new Scanner(System.in);

        while(!option.equals("e")){
            System.out.println(STR_OPTIONS);
            for (String s : options) {
                System.out.println("\t"+s);
            }
            System.out.println(STR_OPTION_INPUT);
            option = scanner.nextLine().toLowerCase();

            if(option.equals("e")) {
                System.out.println(STR_EXIT);
            }else{
                try {
                    int optionIndex = Integer.parseInt(option);
                    if (optionIndex >= 1 && optionIndex < options.length) {
                        String optionString = options[Integer.parseInt(option) - 1];
                        ConvertOptions opt = new ConvertOptions(optionString);
                        System.out.println(STR_TEMPLATE_AMOUNT.replace("[FROM]", opt.getFrom()));
                        double amount = Double.parseDouble(scanner.nextLine());
                        String converted = opt.convertToCurrency(amount);
                        System.out.println(opt.getFrom() + " " + opt.getFormattedAmount(opt.getFrom(), amount) + " is equivalent to: " + opt.getTo() + " " + converted);

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

            // Clear the screen
//            ScreenClearer.clear();
        }
    }
}