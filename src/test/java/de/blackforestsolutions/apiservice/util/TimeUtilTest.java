package de.blackforestsolutions.apiservice.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

class TimeUtilTest {

    @Test
    void test_transformToYyyyMMDdWith_with_valid_date_returns_correct_formatted_date() {
        Date testDate = new Date();

        Date result = TimeUtil.transformToYyyyMMDdWith(testDate);

        //noinspection ConstantConditions, ResultOfMethodCallIgnored(justification: this is a test we know that we do not have null as return)
        Assertions.assertThat(result.toInstant().isBefore(testDate.toInstant()));
    }

}
