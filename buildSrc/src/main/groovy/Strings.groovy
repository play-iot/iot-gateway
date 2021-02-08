class Strings {

    static boolean isBlank(String text) {
        return text == null || "" == text.trim()
    }

    static String requireNotBlank(String text) {
        return requireNotBlank(text, "Not blank")
    }

    static String requireNotBlank(String text, String message) {
        if (isBlank(text)) {
            throw new IllegalArgumentException(message)
        }
        return text.trim()
    }

    static String toSnakeCase(String text, boolean upper = true) {
        if (upper && text == text.toUpperCase()) {
            return text
        }
        if (!upper && text == text.toLowerCase()) {
            return text
        }
        def regex = upper ? "A-Z" : "a-z"
        def t = text.replaceAll(/([$regex])/, /_$1/).replaceAll(/^_/, '')
        return upper ? t.toUpperCase() : t.toLowerCase()
    }

    static String replaceJsonSuffix(String name) {
        return name.replaceAll(toRegexIgnoreCase("_JSON(_ARRAY)?|_ARRAY\$"), "")
    }

    static String toRegexIgnoreCase(String name) {
        return "(?i:${name})"
    }
}
