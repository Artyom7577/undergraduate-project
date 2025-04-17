package com.artyom.undergraduateproject.service.util;

import java.util.List;

public class Constants {

    private Constants() {}

    public static final String PROMPT = " supermarkets in Yerevan";

    public static final List<String> SUPERMARKETS = List.of("Yerevan City", "SAS", "Carrefour", "Evrika", "Nor Zovq");

    public static final String API = "X-Goog-Api-Key";
    public static final String FIELD_MASK_KEY = "X-Goog-FieldMask";
    public static final String FIELD_MASK_LOCATION = "places.displayName,places.location";
    public static final String CONTENT_TYPE = "Content-Type";


    public static String textQuery(String prompt) {
        //language=json
        return """
            {
              "textQuery" : "%s"
            }
            """.formatted(prompt);
    }


}
