package io.github.flexibletech.camunda.tools.process.variables;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProcessVariablesCollector implements Collector<ProcessVariable, Map<String, String>, Map<String, String>> {

    @Override
    public Supplier<Map<String, String>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<String, String>, ProcessVariable> accumulator() {
        return (map, processVariable) -> map.put(processVariable.name(), processVariable.value());
    }

    @Override
    public BinaryOperator<Map<String, String>> combiner() {
        return (map1, map2) -> Stream.concat(map1.entrySet().stream(), map2.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue));
    }

    @Override
    public Function<Map<String, String>, Map<String, String>> finisher() {
        return Collections::unmodifiableMap;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED);
    }

    public static ProcessVariablesCollector toValuesMap() {
        return new ProcessVariablesCollector();
    }

}
