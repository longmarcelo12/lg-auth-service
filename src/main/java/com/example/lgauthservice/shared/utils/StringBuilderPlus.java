package com.example.lgauthservice.shared.utils;

public class StringBuilderPlus {
    private final StringBuilder sb = new StringBuilder();

    public int length() {
        return this.sb.length();
    }

    public void append(String str) {
        this.sb.append((str != null) ? str : "");
    }
    public void appendLineSeparator() {
        this.sb.append(System.lineSeparator());
    }

    public void appendNewLine(String str) {
        this.sb.append((str != null) ? str : "").append(System.lineSeparator());
    }


    public void appendPrefixNewLine(String str) {
        this.sb.append(System.lineSeparator()).append((str != null) ? str : "");
    }


    public String toString() {
        return this.sb.toString();
    }
}
