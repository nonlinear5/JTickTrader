package com.jticktrader.platform.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class FuturesContractCalculator {
    private static final int[] CONTRACT_MONTHS = {3, 6, 9, 12};
    private static final char[] CONTRACT_LETTERS = {'H', 'M', 'U', 'Z'};
    private static final int WEEKS_TO_SECOND_FRIDAY = 1;
    private static final int YEAR_CODE_DIVISOR = 100;

    /**
     * Calculates the front month ES futures contract code for a given date.
     *
     * @param date The date to calculate for
     * @return The contract symbol (e.g., "ESH26", "ESM26")
     */
    public static String getFrontMonthContract(LocalDate date) {
        int currentYear = date.getYear();
        int currentMonth = date.getMonthValue();

        int targetMonthIdx = findNextContractMonthIndex(currentMonth);
        int contractMonth;
        int contractYear = currentYear;

        if (targetMonthIdx >= CONTRACT_MONTHS.length) {
            contractMonth = CONTRACT_MONTHS[0];
            contractYear++;
        } else {
            contractMonth = CONTRACT_MONTHS[targetMonthIdx];
        }

        if (currentMonth == contractMonth) {
            if (hasPassedRolloverDate(date)) {
                targetMonthIdx++;
                if (targetMonthIdx >= CONTRACT_MONTHS.length) {
                    contractMonth = CONTRACT_MONTHS[0];
                    contractYear++;
                } else {
                    contractMonth = CONTRACT_MONTHS[targetMonthIdx];
                }
            }
        }

        char contractLetter = getContractLetter(contractMonth);
        int yearCode = contractYear % YEAR_CODE_DIVISOR;

        return String.format("ES%c%02d", contractLetter, yearCode);
    }

    private static int findNextContractMonthIndex(int currentMonth) {
        int idx = 0;
        while (idx < CONTRACT_MONTHS.length && CONTRACT_MONTHS[idx] < currentMonth) {
            idx++;
        }
        return idx;
    }

    private static boolean hasPassedRolloverDate(LocalDate date) {
        LocalDate firstOfMonth = date.withDayOfMonth(1);
        LocalDate firstFriday = firstOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
        LocalDate secondFriday = firstFriday.plusWeeks(WEEKS_TO_SECOND_FRIDAY);
        return !date.isBefore(secondFriday);
    }

    private static char getContractLetter(int contractMonth) {
        for (int i = 0; i < CONTRACT_MONTHS.length; i++) {
            if (CONTRACT_MONTHS[i] == contractMonth) {
                return CONTRACT_LETTERS[i];
            }
        }
        throw new IllegalArgumentException("Invalid contract month: " + contractMonth);
    }
}
