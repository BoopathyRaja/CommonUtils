package com.br.commonutils.util;

import android.content.Context;
import android.location.Geocoder;
import android.support.annotation.NonNull;

import com.br.commonutils.data.common.Address;
import com.br.commonutils.validator.Validator;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddressUtil {

    public static Address getAddress(@NonNull Context context, @NonNull LatLng latLng) throws IOException {
        Address retVal = null;

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<android.location.Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        if (Validator.isValid(addresses)) {
            android.location.Address address = addresses.get(0);
            ArrayList<String> addressList = new ArrayList<>();
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressList.add(address.getAddressLine(i));
            }

            String addressLine = CommonUtil.toCSV(addressList);
            String line1 = address.getThoroughfare() + ", " + address.getSubThoroughfare();
            String line2 = address.getSubLocality();
            String city = address.getLocality();
            String state = address.getAdminArea();
            String country = address.getCountryCode();
            String pinCode = address.getPostalCode();

            retVal = new Address();
            retVal.setAddressLine(addressLine);
            retVal.setLine1(line1);
            retVal.setLine2(line2);
            retVal.setCity(city);
            retVal.setState(state);
            retVal.setCountry(country);
            retVal.setZipCode(pinCode);
        }

        return retVal;
    }

    public static LatLng getLatLng(@NonNull Context context, @NonNull String address) throws IOException {
        LatLng retVal = null;

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<android.location.Address> addresses = geocoder.getFromLocationName(address, 1);
        if (Validator.isValid(addresses)) {
            android.location.Address tempAddress = addresses.get(0);
            retVal = new LatLng(tempAddress.getLatitude(), tempAddress.getLongitude());
        }

        return retVal;
    }
}
