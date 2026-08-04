package com.jticktrader.platform.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class FuturesContractCalculator {

    // Standard ES quarterly contract months: March(3), June(6), September(9), December(12)
    private static final int[] CONTRACT_MONTHS = {3, 6, 9, 12};
    private static final char[] CONTRACT_LETTERS = {'H', 'M', 'U', 'Z'};

    /**
     * Calculates the front month ES futures contract code for a given date.
     *
     * @param date The date to calculate for
     * @return The contract symbol (e.g., "ESH26", "ESM26")
     */
    public static String getFrontMonthContract(LocalDate date) {
        int currentYear = date.getYear();
        int currentMonth = date.getMonthValue();

        // 1. Identify the upcoming or current contract month target
        int targetMonthIdx = 0;
        while (targetMonthIdx < CONTRACT_MONTHS.length && CONTRACT_MONTHS[targetMonthIdx] < currentMonth) {
            targetMonthIdx++;
        }

        int contractMonth;
        int contractYear = currentYear;

        // If the current month is past December, it rolls into March of next year
        if (targetMonthIdx >= CONTRACT_MONTHS.length) {
            contractMonth = CONTRACT_MONTHS[0];
            contractYear++;
        } else {
            contractMonth = CONTRACT_MONTHS[targetMonthIdx];
        }

        // 2. If we are exactly in a contract month, check if we have crossed the rollover day
        if (currentMonth == contractMonth) {
            // Find the second Friday of this month
            LocalDate firstOfMonth = date.withDayOfMonth(1);
            LocalDate firstFriday = firstOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
            LocalDate secondFriday = firstFriday.plusWeeks(1);

            // If today is on or after the second Friday, roll to the next quarterly contract
            if (!date.isBefore(secondFriday)) {
                targetMonthIdx++;
                if (targetMonthIdx >= CONTRACT_MONTHS.length) {
                    contractMonth = CONTRACT_MONTHS[0];
                    contractYear++;
                } else {
                    contractMonth = CONTRACT_MONTHS[targetMonthIdx];
                }
            }
        }

        // 3. Resolve the letter code corresponding to the final contract month
        char contractLetter = ' ';
        for (int i = 0; i < CONTRACT_MONTHS.length; i++) {
            if (CONTRACT_MONTHS[i] == contractMonth) {
                contractLetter = CONTRACT_LETTERS[i];
                break;
            }
        }

        // Extract the last two digits of the year (e.g., 2026 -> 26)
        int yearCode = contractYear % 100;

        return String.format("ES%c%02d", contractLetter, yearCode);
    }

    public static void main(String[] args) {
        // Test Case 1: In May 2026 (Before June cycle shift) -> Should return June (ESM26)
        System.out.println("May 14, 2026: " + getFrontMonthContract(LocalDate.of(2026, 5, 14)));

        // Test Case 2: June 5, 2026 (First Friday of June) -> Should still be June (ESM26)
        System.out.println("June 5, 2026: " + getFrontMonthContract(LocalDate.of(2026, 6, 5)));

        // Test Case 3: June 12, 2026 (Second Friday of June) -> Switches to September (ESU26)
        System.out.println("June 12, 2026: " + getFrontMonthContract(LocalDate.of(2026, 6, 12)));

        // Test Case 4: Late December 2026 -> Switches to March next year (ESH27)
        System.out.println("December 25, 2026: " + getFrontMonthContract(LocalDate.of(2026, 12, 25)));

        // Test Case 5: today
        System.out.println("Today: " + getFrontMonthContract(LocalDate.of(2026, 05, 14)));

    }
}
