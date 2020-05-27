package com.example.restservice.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Band {
    private String name;
    private List<Festival> festivals = new ArrayList<>();
}