package com.example.uradio;

public class radio_station {

    String station_name;
    String station_url;

    public radio_station(String station_name, String station_url) {
        this.station_name = station_name;
        this.station_url = station_url;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public String getStation_url() {
        return station_url;
    }

    public void setStation_url(String station_url) {
        this.station_url = station_url;
    }


    // Overrides the array adapter toString method to display the station name
    @Override
    public String toString() {
        return getStation_name();
    }
}
