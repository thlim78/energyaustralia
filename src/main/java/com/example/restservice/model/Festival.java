package com.example.restservice.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Festival {
    private String name;
    public Festival(String name) {
        this.name = name;
    }
}
