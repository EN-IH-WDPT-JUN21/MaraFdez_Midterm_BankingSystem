package com.ironhack.midtermproject.utils;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import java.math.BigDecimal;

public class Constants {

    // If any account drops below the minimumBalance, the penaltyFee should be deducted from the balance automatically
    public static final BigDecimal PENALTY_FEE = new BigDecimal(40.0);

    public static final BigDecimal CCARD_DEFAULT_INTEREST_RATE = new BigDecimal("0.2");

    public static final BigDecimal CCARD_DEFAULT_CREDIT_LIMIT = new BigDecimal(100);

    public static final BigDecimal SAVINGS_ACC_DEFAULT_INTEREST_RATE = new BigDecimal("0.0025");

    public static final BigDecimal SAVINGS_ACC_DEFAULT_MIN_BALANCE = new BigDecimal("1000");

    public static final BigDecimal CHECKING_ACC_DEFAULT_MONTHLY_FEE = new BigDecimal(12.0);

    public static final BigDecimal CHECKING_ACC_MIN_BALANCE = new BigDecimal(250.0);
}
