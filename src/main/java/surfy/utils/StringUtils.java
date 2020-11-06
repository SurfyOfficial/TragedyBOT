package surfy.utils;

public class StringUtils {

    private static final String ALPHA_NUMERIC_STRING1 = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING1.length());
            builder.append(ALPHA_NUMERIC_STRING1.charAt(character));
        }
        return builder.toString();
    }

}
