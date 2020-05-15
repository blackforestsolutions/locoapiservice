package de.blackforestsolutions.apiservice.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import java.util.Set;

class CombinationUtilTest {

    @Test
    void test_buildCombinationsPairsBy_returns_all_combination_values_between_two_lists() {
        Set<Character> firstThreeLetters = Set.of('a', 'b', 'c');
        Set<Character> lastThreeLetters = Set.of('x', 'y', 'z');

        Set<Pair<Character, Character>> result = CombinationUtil.buildCombinationsPairsBy(firstThreeLetters, lastThreeLetters);

        Assertions.assertThat(result).containsExactlyInAnyOrder(
                Pair.of('a', 'x'),
                Pair.of('a', 'y'),
                Pair.of('a', 'z'),
                Pair.of('b', 'x'),
                Pair.of('b', 'y'),
                Pair.of('b', 'z'),
                Pair.of('c', 'x'),
                Pair.of('c', 'y'),
                Pair.of('c', 'z')
        );
    }
}
