package com.example.restservice.model.provider;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MusicFestival {
    private String name;
    private List<Band> bands = new ArrayList<>();
}
