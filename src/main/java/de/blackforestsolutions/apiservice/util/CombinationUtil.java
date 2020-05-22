package de.blackforestsolutions.apiservice.util;

import org.springframework.data.util.Pair;

import java.util.Set;
import java.util.stream.Collectors;

public class CombinationUtil {

    public static <V> Set<Pair<V, V>> buildCombinationsPairsBy(Set<V> firstSet, Set<V> secondSet) {
        return firstSet
                .stream()
                .flatMap(firstMapValue -> secondSet
                        .stream()
                        .map(secondMapValue -> Pair.of(firstMapValue, secondMapValue))
                )
                .collect(Collectors.toSet());
    }
}
