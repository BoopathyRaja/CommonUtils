package com.br.commonutils.data.common;

import com.br.commonutils.validator.Validator;

import java.io.Serializable;

public class Address implements Serializable {

    private String name;
    private String addressLine;
    private String line1;       // ThroughFare, SubThroughFare
    private String line2;       // SubLocality
    private String city;        // Locality
    private String state;       // AdminArea
    private String country;     // CountryName
    private String zipCode;     // PostCode
    private String landmark;
    private Location location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressLine() {
        return addressLine.replace("\n", ", ");
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String formattedAddress() {
        StringBuilder retVal = new StringBuilder();

        if (Validator.isValid(line1)) {
            retVal.append(line1);
            retVal.append(",");
        }

        if (Validator.isValid(line2)) {
            retVal.append("\n");
            retVal.append(line2);
            retVal.append(",");
        }

        if (Validator.isValid(city) || Validator.isValid(zipCode)) {
            retVal.append("\n");

            if (Validator.isValid(city))
                retVal.append(city);

            if (Validator.isValid(zipCode)) {
                retVal.append(" - ");
                retVal.append(zipCode);
            }
        }

        if (Validator.isValid(state)) {
            retVal.append("\n");
            retVal.append(state);
            retVal.append(", ");
        }

        if (Validator.isValid(country)) {
            retVal.append("\n");
            retVal.append(country);
        }

        return retVal.toString();
    }
}
