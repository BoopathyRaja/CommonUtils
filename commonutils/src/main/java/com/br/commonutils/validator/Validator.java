package com.br.commonutils.validator;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

public class Validator {

    final static Pattern FULL_NAME = Pattern.compile("[a-zA-Z ]*");
    final static Pattern FIRST_NAME = Pattern.compile("[a-zA-Z]*");
    final static Pattern LAST_NAME = Pattern.compile("[a-zA-z]+([ '-][a-zA-Z]+)*");
    final static Pattern ATLEAST_ONE_DIGIT = Pattern.compile("[0-9]");
    final static Pattern ATLEAST_ONE_UPPERCASE_CHARACTER = Pattern.compile("[A-Z]");
    final static Pattern ATLEAST_ONE_LOWERCASE_CHARACTER = Pattern.compile("[a-z]");
    final static Pattern ATLEAST_ONE_LETTER = Pattern.compile("[a-zA-Z]");
    final static Pattern ATLEAST_ONE_SPECIAL_CHARACTER = Pattern.compile("[^a-zA-Z0-9\\s]");
    final static Pattern ALPHANUMERIC = Pattern.compile("^[a-zA-Z0-9_]+$");
    final static Pattern ALPHA = Pattern.compile("[a-zA-Z]+");
    final static Pattern NUMERIC = Pattern.compile("^[0-9]+");
    final static Pattern DECIMAL_NUMBER = Pattern.compile("^[0-9]*\\.?[0-9]*$");
    final static Pattern PINCODE = Pattern.compile("^[0-9]{6}$");
    final static Pattern MAC_ADDRESS = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
    final static Pattern HEXADECIMAL = Pattern.compile("\\p{XDigit}+");
    final static Pattern MD5 = Pattern.compile("[a-fA-F0-9]{32}");

    public static boolean isValid(Object object) {
        return object != null;
    }

    public static boolean isValid(String data) {
        return !TextUtils.isEmpty(data);
    }

    public static boolean isValid(String[] data) {
        return isValid((Object) data) && data.length > 0;
    }

    public static boolean isValid(Map data) {
        return isValid((Object) data) && !data.isEmpty();
    }

    public static boolean isValid(Collection data) {
        return isValid((Object) data) && !data.isEmpty();
    }

    public static boolean isValidFullName(String firstName) {
        return validate(firstName, FULL_NAME);
    }

    public static boolean isValidFirstName(String firstName) {
        return validate(firstName, FIRST_NAME);
    }

    public static boolean isValidLastName(String lastName) {
        return validate(lastName, LAST_NAME);
    }

    public static boolean isValidEmail(String email) {
        return validate(email, Patterns.EMAIL_ADDRESS);
    }

    public static boolean hasAtleastOneDigit(String data) {
        return validate(data, ATLEAST_ONE_DIGIT);
    }

    public static boolean hasAtleastOneUpperCase(String data) {
        return validate(data, ATLEAST_ONE_UPPERCASE_CHARACTER);
    }

    public static boolean hasAtleastOneLowerCase(String data) {
        return validate(data, ATLEAST_ONE_LOWERCASE_CHARACTER);
    }

    public static boolean hasAtleastOneLetter(String data) {
        return validate(data, ATLEAST_ONE_LETTER);
    }

    public static boolean hasAtleastOneSpecialCharacter(String data) {
        return validate(data, ATLEAST_ONE_SPECIAL_CHARACTER);
    }

    public static boolean isAlphaNumeric(String data) {
        return validate(data, ALPHANUMERIC);
    }

    public static boolean isAlpha(String data) {
        return validate(data, ALPHA);
    }

    public static boolean isNumeric(String data) {
        return validate(data, NUMERIC);
    }

    public static boolean isDecimalNumber(String data) {
        return validate(data, DECIMAL_NUMBER);
    }

    public static boolean isPincode(String data) {
        return validate(data, PINCODE);
    }

    public static boolean isMACAddress(String data) {
        return validate(data, MAC_ADDRESS);
    }

    public static boolean isHexaDecimal(String data) {
        return validate(data, HEXADECIMAL);
    }

    public static boolean isMD5(String data) {
        return validate(data, MD5);
    }

    public static boolean contains(String data, String contains) {
        return Validator.isValid(data) && Validator.isValid(contains) && data.contains(contains);
    }

    private static boolean validate(String data, Pattern pattern) {
        return isValid(data) && pattern.matcher(data).matches();
    }
}
