package io.github.zero88.qwe.iot.data.unit;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.qwe.iot.data.IoTProperty;
import io.github.zero88.utils.Strings;
import io.vertx.core.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
public final class UnitAlias implements JsonData, IoTProperty {

    private static final Comparator<Node> COMPARATOR = Comparator.comparingInt((Node n) -> n.op.priority)
                                                                 .thenComparingDouble((Node n) -> n.with);
    private final SortedSet<Node> nodes = new TreeSet<>(COMPARATOR);

    @JsonCreator
    private UnitAlias(Map<String, String> map) {
        nodes.addAll(map.entrySet().stream().map(entry -> new Node(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toSet()));
    }

    static UnitAlias create(Map<String, String> map) {
        return Objects.isNull(map) ? null : new UnitAlias(map);
    }

    private static String validateAndGetKey(String expression) {
        final Entry<BinaryRelation, Double> expr = validateExpr(expression);
        return Node.toKey(expr.getKey(), expr.getValue());
    }

    private static Entry<BinaryRelation, Double> validateExpr(String expression) {
        String expr = Strings.requireNotBlank(expression, "Expression cannot be blank");
        Optional<BinaryRelation> binaryRelation = Stream.of(BinaryRelation.values())
                                                        .sorted(((o1, o2) -> o2.symbol.compareTo(o1.symbol)))
                                                        .filter(br -> expr.length() > 1 && br.symbol.length() == 2
                                                                      ? expr.substring(0, 2).equals(br.symbol)
                                                                      : expr.charAt(0) == br.symbol.charAt(0))
                                                        .findFirst();
        String rightChild = binaryRelation.map(r -> expr.substring(r.symbol.length())).orElse(expr).trim();
        try {
            return new SimpleEntry<>(binaryRelation.orElse(BinaryRelation.EQUALS), Double.parseDouble(rightChild));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Expression cannot parse. Only support some operators: " +
                                               Stream.of(BinaryRelation.values())
                                                     .map(BinaryRelation::getSymbol)
                                                     .collect(Collectors.joining(", ")), e);
        }
    }

    public UnitAlias add(String expr, String display) {
        this.nodes.add(new Node(expr, display));
        return this;
    }

    public String get(String expr) {
        return nodes.stream()
                    .filter(n -> n.toString().equals(validateAndGetKey(expr)))
                    .map(n -> n.label)
                    .findFirst()
                    .orElse(null);
    }

    public String eval(int value) {
        return eval((double) value);
    }

    public String eval(@NonNull Double value) {
        SortedSet<Node> collect = nodes.stream()
                                       .filter(node -> node.eval(value))
                                       .collect(() -> new TreeSet<>(COMPARATOR), SortedSet::add, SortedSet::addAll);
        if (collect.isEmpty()) {
            return null;
        }
        if (collect.size() == 1) {
            return collect.first().label;
        }
        final Optional<Node> any = collect.stream().filter(n -> n.op == BinaryRelation.EQUALS).findAny();
        if (any.isPresent()) {
            return any.get().label;
        }
        return collect.stream()
                      .min(Comparator.comparingDouble((Node o) -> Math.abs(value - o.with))
                                     .thenComparingDouble((Node n) -> n.with))
                      .map(n -> n.label)
                      .orElse(null);
    }

    @Override
    public JsonObject toJson() {
        return nodes.stream()
                    .collect(JsonObject::new, (json, node) -> json.put(node.toString(), node.label),
                             (json1, json2) -> json2.mergeIn(json1, true));
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        return nodes.stream().map(Node::hashCode).reduce(result, (r, n) -> r + PRIME * n);
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UnitAlias)) {
            return false;
        }
        final UnitAlias other = (UnitAlias) o;
        return nodes.stream()
                    .map(Node::toString)
                    .collect(Collectors.joining("-"))
                    .equals(other.nodes.stream().map(Node::toString).collect(Collectors.joining("-")));
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    enum BinaryRelation {

        EQUALS("=", 1),
        GREATER_THAN(">", 2),
        LESS_THAN("<", 3),
        GREATER_OR_EQUAL(">=", 4),
        LESS_OR_EQUAL("<=", 5),
        INEQUALITY("<>", 6);

        private final String symbol;
        private final int priority;
    }


    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    static final class Node {

        @NonNull
        @EqualsAndHashCode.Include
        private final BinaryRelation op;
        @NonNull
        @EqualsAndHashCode.Include
        private final Double with;
        private final String label;

        private Node(String expression, String label) {
            final Entry<BinaryRelation, Double> entry = validateExpr(expression);
            this.op = entry.getKey();
            this.with = entry.getValue();
            this.label = validateAndGetValue(label);
        }

        static String toKey(BinaryRelation binaryRelation, Double value) {
            return binaryRelation.symbol + " " + value.toString();
        }

        static String validateAndGetValue(String display) {
            final String v = Strings.requireNotBlank(display, "Display value cannot be null");
            if (v.equalsIgnoreCase("null")) {
                throw new IllegalArgumentException("Display value cannot be null");
            }
            return v;
        }

        boolean eval(@NonNull Double value) {
            int compare = value.compareTo(with);
            if (compare == 0) {
                return op == BinaryRelation.EQUALS || op == BinaryRelation.GREATER_OR_EQUAL ||
                       op == BinaryRelation.LESS_OR_EQUAL;
            }
            if (compare < 0) {
                return op == BinaryRelation.LESS_THAN || op == BinaryRelation.LESS_OR_EQUAL ||
                       op == BinaryRelation.INEQUALITY;
            }
            return op == BinaryRelation.GREATER_THAN || op == BinaryRelation.GREATER_OR_EQUAL ||
                   op == BinaryRelation.INEQUALITY;
        }

        @Override
        public String toString() {
            return toKey(op, with);
        }

    }

}
