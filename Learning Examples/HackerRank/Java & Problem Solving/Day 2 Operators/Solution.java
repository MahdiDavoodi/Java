import java.util.*;

public class Solution {

    static void solve(double mealCost, int tipPercent, int taxPercent) {
        double totalCost = (mealCost
                + ((mealCost * tipPercent) / 100)
                + ((mealCost * taxPercent) / 100));
        System.out.println(Math.round(totalCost));

    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        double mealCost = scanner.nextDouble();
        int tipPercent = scanner.nextInt();
        int taxPercent = scanner.nextInt();

        solve(mealCost, tipPercent, taxPercent);
        scanner.close();
    }
}
