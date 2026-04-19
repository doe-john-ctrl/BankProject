package util;
public class PasswordChecker {

    public static boolean isStrong(String password) {
        boolean len = password.length() >= 8;
        boolean upper = false, lower = false, num = false, special = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) upper = true;
            else if (Character.isLowerCase(ch)) lower = true;
            else if (Character.isDigit(ch)) num = true;
            else special = true;
        }

        int score = 0;
        if (len) score++;
        if (upper) score++;
        if (lower) score++;
        if (num) score++;
        if (special) score++;

        System.out.println("Password strength: " + score + "/5");

        return score == 5;
    }
}