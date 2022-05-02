package tech.avito.generator;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Component;

@Component
public class HashGenerator {

    private RandomStringGenerator randomStringGenerator;

    public HashGenerator() {
        this.randomStringGenerator = new RandomStringGenerator
                .Builder()
                .filteredBy(HashGenerator::isLatinLetterOrDigit)
                .build();
    }

    public String generate(int length) {
        return randomStringGenerator.generate(length);
    }

    private static boolean isLatinLetterOrDigit(int codePoint) {
        return ('a' <= codePoint && codePoint <= 'z')
                || ('A' <= codePoint && codePoint <= 'Z')
                || ('0' <= codePoint && codePoint <= '9')
                || ('+' == codePoint)
                || ('_' == codePoint)
                || ('-' == codePoint);
    }
}
