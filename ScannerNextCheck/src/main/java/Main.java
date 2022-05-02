import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String option;
        Scanner input = new Scanner(System.in);
        while (true) {
            option = input.next();
            if (option.equals("y")) {
                System.err.println(option);
            }
        }
    }
}
