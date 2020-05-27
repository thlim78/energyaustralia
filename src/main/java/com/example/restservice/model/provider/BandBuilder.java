package com.example.restservice.model.provider;

import lombok.Data;

public class BandBuilder {
    private String name;
    private String recordLabel;

    public BandBuilder(String name, String recordLabel) {
        this.name = name;
        this.recordLabel = recordLabel;
    }

    public BandBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public BandBuilder setRecordLabel(String recordLabel) {
        this.recordLabel = recordLabel;
        return this;
    }

    public Band build() {
        Band band = new Band();
        band.setName(this.name);
        band.setRecordLabel(this.recordLabel);
        return band;
    }
}