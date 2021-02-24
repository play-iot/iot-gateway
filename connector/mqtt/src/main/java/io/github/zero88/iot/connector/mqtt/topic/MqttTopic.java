package io.github.zero88.iot.connector.mqtt.topic;

import java.nio.charset.StandardCharsets;

import io.github.zero88.utils.Strings;

/**
 * Represents a topic destination, used for publish/subscribe messaging.
 */
public class MqttTopic {

    private static final int INDEX_NOT_FOUND = -1;

    /**
     * The forward slash (/) is used to separate each level within a topic tree and provide a hierarchical structure to
     * the topic space. The use of the topic level separator is significant when the two wildcard characters are
     * encountered in topics specified by subscribers.
     */
    public static final String TOPIC_LEVEL_SEPARATOR = "/";

    /**
     * Multi-level wildcard The number sign (#) is a wildcard character that matches any number of levels within a
     * topic.
     */
    public static final String MULTI_LEVEL_WILDCARD = "#";

    /**
     * Single-level wildcard The plus sign (+) is a wildcard character that matches only one topic level.
     */
    public static final String SINGLE_LEVEL_WILDCARD = "+";

    /**
     * Multi-level wildcard pattern(/#)
     */
    public static final String MULTI_LEVEL_WILDCARD_PATTERN = TOPIC_LEVEL_SEPARATOR + MULTI_LEVEL_WILDCARD;

    /**
     * Topic wildcards (#+)
     */
    public static final String TOPIC_WILDCARDS = MULTI_LEVEL_WILDCARD + SINGLE_LEVEL_WILDCARD;

    // topic name and topic filter length range defined in the spec
    private static final int MIN_TOPIC_LEN = 1;
    private static final int MAX_TOPIC_LEN = 65535;
    private static final char NUL = '\u0000';

    private String name;

    /**
     * @param name The Topic Name
     */
    public MqttTopic(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the queue or topic.
     *
     * @return the name of this destination.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of this topic.
     *
     * @return a string representation of this topic.
     */
    public String toString() {
        return getName();
    }

    /**
     * Validate the topic name or topic filter
     *
     * @param topicString     topic name or filter
     * @param wildcardAllowed true if validate topic filter, false otherwise
     * @throws IllegalArgumentException if the topic is invalid
     */
    public static void validate(String topicString, boolean wildcardAllowed) throws IllegalArgumentException {
        int topicLen = topicString.getBytes(StandardCharsets.UTF_8).length;

        // Spec: length check
        // - All Topic Names and Topic Filters MUST be at least one character
        // long
        // - Topic Names and Topic Filters are UTF-8 encoded strings, they MUST
        // NOT encode to more than 65535 bytes
        if (topicLen < MIN_TOPIC_LEN || topicLen > MAX_TOPIC_LEN) {
            throw new IllegalArgumentException(
                String.format("Invalid topic length, should be in range[%d, %d]!", MIN_TOPIC_LEN, MAX_TOPIC_LEN));
        }

        // *******************************************************************************
        // 1) This is a topic filter string that can contain wildcard characters
        // *******************************************************************************
        if (wildcardAllowed) {
            // Only # or +
            if (equalsAny(topicString, new String[] {MULTI_LEVEL_WILDCARD, SINGLE_LEVEL_WILDCARD})) {
                return;
            }

            // 1) Check multi-level wildcard
            // Rule:
            // The multi-level wildcard can be specified only on its own or next
            // to the topic level separator character.

            // - Can only contains one multi-level wildcard character
            // - The multi-level wildcard must be the last character used within
            // the topic tree
            if (countMatches(topicString, MULTI_LEVEL_WILDCARD) > 1 ||
                (topicString.contains(MULTI_LEVEL_WILDCARD) && !topicString.endsWith(MULTI_LEVEL_WILDCARD_PATTERN))) {
                throw new IllegalArgumentException(
                    "Invalid usage of multi-level wildcard in topic string: " + topicString);
            }

            // 2) Check single-level wildcard
            // Rule:
            // The single-level wildcard can be used at any level in the topic
            // tree, and in conjunction with the
            // multilevel wildcard. It must be used next to the topic level
            // separator, except when it is specified on
            // its own.
            validateSingleLevelWildcard(topicString);

            return;
        }

        // *******************************************************************************
        // 2) This is a topic name string that MUST NOT contains any wildcard characters
        // *******************************************************************************
        if (containsAny(topicString, TOPIC_WILDCARDS)) {
            throw new IllegalArgumentException("The topic name MUST NOT contain any wildcard characters (#+)");
        }
    }

    private static void validateSingleLevelWildcard(String topicString) {
        char singleLevelWildcardChar = SINGLE_LEVEL_WILDCARD.charAt(0);
        char topicLevelSeparatorChar = TOPIC_LEVEL_SEPARATOR.charAt(0);

        char[] chars = topicString.toCharArray();
        int length = chars.length;
        char prev = NUL, next = NUL;
        for (int i = 0; i < length; i++) {
            prev = (i - 1 >= 0) ? chars[i - 1] : NUL;
            next = (i + 1 < length) ? chars[i + 1] : NUL;

            if (chars[i] == singleLevelWildcardChar) {
                // prev and next can be only '/' or none
                if (prev != topicLevelSeparatorChar && prev != NUL || next != topicLevelSeparatorChar && next != NUL) {
                    throw new IllegalArgumentException(
                        String.format("Invalid usage of single-level wildcard in topic string '%s'!", topicString));
                }
            }
        }
    }

    /**
     * Check the supplied topic name and filter match
     *
     * @param topicFilter topic filter: wildcards allowed
     * @param topicName   topic name: wildcards not allowed
     * @return true if the topic matches the filter
     * @throws IllegalArgumentException if the topic name or filter is invalid
     */
    public static boolean isMatched(String topicFilter, String topicName) throws IllegalArgumentException {
        int topicPos = 0;
        int filterPos = 0;
        int topicLen = topicName.length();
        int filterLen = topicFilter.length();

        MqttTopic.validate(topicFilter, true);
        MqttTopic.validate(topicName, false);

        if (topicFilter.equals(topicName)) {
            return true;
        }

        while (filterPos < filterLen && topicPos < topicLen) {
            if (topicFilter.charAt(filterPos) == '#') {
                /*
                 * next 'if' will break when topicFilter = topic/# and topicName topic/A/,
                 * but they are matched
                 */
                topicPos = topicLen;
                filterPos = filterLen;
                break;
            }
            if (topicName.charAt(topicPos) == '/' && topicFilter.charAt(filterPos) != '/') {
                break;
            }
            if (topicFilter.charAt(filterPos) != '+' && topicFilter.charAt(filterPos) != '#' &&
                topicFilter.charAt(filterPos) != topicName.charAt(topicPos)) {
                break;
            }
            if (topicFilter.charAt(filterPos) == '+') { // skip until we meet the next separator, or end of string
                int nextpos = topicPos + 1;
                while (nextpos < topicLen && topicName.charAt(nextpos) != '/') {
                    nextpos = ++topicPos + 1;
                }
            }

            filterPos++;
            topicPos++;
        }

        if ((topicPos == topicLen) && (filterPos == filterLen)) {
            return true;
        } else {
            /*
             * https://github.com/eclipse/paho.mqtt.java/issues/418
             * Covers edge case to match sport/# to sport
             */
            if ((topicFilter.length() - filterPos > 0) && (topicPos == topicLen)) {
                if (topicName.charAt(topicPos - 1) == '/' && topicFilter.charAt(filterPos) == '#') {
                    return true;
                }
                return topicFilter.length() - filterPos > 1 && topicFilter.startsWith("/#", filterPos);
            }
        }
        return false;
    }

    /**
     * Checks if the CharSequence contains any character in the given set of characters.
     *
     * @param cs          the CharSequence to check, may be null
     * @param searchChars the chars to search for, may be null
     * @return the {@code true} if any of the chars are found, {@code false} if no match or null input
     */
    public static boolean containsAny(CharSequence cs, CharSequence searchChars) {
        if (searchChars == null) {
            return false;
        }
        return containsAny(cs, toCharArray(searchChars));
    }

    /**
     * Checks if the CharSequence contains any character in the given set of characters.
     *
     * @param cs          the CharSequence to check, may be null
     * @param searchChars the chars to search for, may be null
     * @return the {@code true} if any of the chars are found, {@code false} if no match or null input
     */
    public static boolean containsAny(CharSequence cs, char[] searchChars) {
        if (Strings.isBlank(cs.toString()) || (searchChars == null || searchChars.length == 0)) {
            return false;
        }
        int csLength = cs.length();
        int searchLength = searchChars.length;
        int csLast = csLength - 1;
        int searchLast = searchLength - 1;
        for (int i = 0; i < csLength; i++) {
            char ch = cs.charAt(i);
            for (int j = 0; j < searchLength; j++) {
                if (searchChars[j] == ch) {
                    if (Character.isHighSurrogate(ch)) {
                        if (j == searchLast) {
                            // missing low surrogate, fine, like String.indexOf(String)
                            return true;
                        }
                        if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
                            return true;
                        }
                    } else {
                        // ch is in the Basic Multilingual Plane
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if the CharSequence equals any character in the given set of characters.
     *
     * @param cs   the CharSequence to check
     * @param strs the set of characters to check against
     * @return true if equals any
     */
    public static boolean equalsAny(CharSequence cs, CharSequence[] strs) {
        boolean eq = false;
        if (cs == null) {
            eq = strs == null;
        }

        if (strs != null) {
            for (CharSequence str : strs) {
                eq = eq || str.equals(cs);
            }
        }

        return eq;
    }

    /**
     * Green implementation of toCharArray.
     *
     * @param cs the {@code CharSequence} to be processed
     * @return the resulting char array
     */
    private static char[] toCharArray(CharSequence cs) {
        if (cs instanceof String) {
            return ((String) cs).toCharArray();
        } else {
            int sz = cs.length();
            char[] array = new char[cs.length()];
            for (int i = 0; i < sz; i++) {
                array[i] = cs.charAt(i);
            }
            return array;
        }
    }

    /**
     * Counts how many times the substring appears in the larger string.
     *
     * @param str the CharSequence to check, may be null
     * @param sub the substring to count, may be null
     * @return the number of occurrences, 0 if either CharSequence is {@code null}
     */
    public static int countMatches(CharSequence str, CharSequence sub) {
        if (Strings.isBlank(str.toString()) || Strings.isBlank(sub.toString())) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = indexOf(str, sub, idx)) != INDEX_NOT_FOUND) {
            count++;
            idx += sub.length();
        }
        return count;
    }

    /**
     * Used by the indexOf(CharSequence methods) as a green implementation of indexOf.
     *
     * @param cs         the {@code CharSequence} to be processed
     * @param searchChar the {@code CharSequence} to be searched for
     * @param start      the start index
     * @return the index where the search sequence was found
     */
    private static int indexOf(CharSequence cs, CharSequence searchChar, int start) {
        return cs.toString().indexOf(searchChar.toString(), start);
    }

}
