package com.example.restservice.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Record {
    private String recordLabel;
    private List<Band> bands = new ArrayList<>();
}
