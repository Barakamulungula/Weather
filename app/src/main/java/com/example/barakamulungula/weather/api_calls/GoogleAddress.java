package com.example.barakamulungula.weather.api_calls;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoogleAddress {
    @SerializedName("results")
    private List<Results> mResultsList;

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public List<Results> getResultsList() {
        return mResultsList;
    }

    public class Results {

        @SerializedName("formatted_address")
        private String mAddressName;

        @SerializedName("geometry")
        private Geometry mGeometry;

        public String getAddressName() {
            return mAddressName;
        }

        public Geometry getGeometry() {
            return mGeometry;
        }

        public class Geometry {

            @SerializedName("location")
            private GoogleLocation mGoogleLocation;

            public GoogleLocation getGoogleLocation() {
                return mGoogleLocation;
            }

            public class GoogleLocation {

                @SerializedName("lat")
                private double latitude;

                @SerializedName("lng")
                private double longitude;

                public double getLatitude() {
                    return latitude;
                }

                public double getLongitude() {
                    return longitude;
                }
            }
        }
    }
}
