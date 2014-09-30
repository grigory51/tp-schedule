package ru.mail.tp.schedule.utils;

/**
 * author: grigory51
 * date: 30/09/14
 */
public class StringHelper {
    public static String quotesFormat(String targetString) {
        if (targetString != null) {
            StringBuilder buffer = new StringBuilder(targetString);

            int leftQuotePosition = 0, rightQuotePosition = 0;
            for (int i = 0; i < targetString.length() / 2; i++) {
                if (leftQuotePosition == 0 && targetString.charAt(i) == '"') {
                    leftQuotePosition = i;
                }
                if (rightQuotePosition == 0 && targetString.charAt(targetString.length() - i - 1) == '"') {
                    rightQuotePosition = targetString.length() - i - 1;
                }
                if (leftQuotePosition != 0 && rightQuotePosition != 0) {
                    buffer.setCharAt(leftQuotePosition, '«');
                    buffer.setCharAt(rightQuotePosition, '»');
                    leftQuotePosition = rightQuotePosition = 0;
                }
            }
            return buffer.toString();
        } else {
            return null;
        }
    }
}
