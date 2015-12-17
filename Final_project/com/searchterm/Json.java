package com.searchterm;

import java.io.FileWriter;
import java.io.IOException;

import javax.json.Json.*;
import javax.json.stream.JsonGenerator;
//import javax.json.Json;


public class Json {
public static void main(String[] args) {
    FileWriter Writer = null;
    try {
        Writer = new FileWriter("C:\\Users\\Joseph White\\Downloads\\jsontext.txt");
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    JsonGenerator gen = Json.createGenerator(Writer);

    gen.writeStartObject().write("name", "sample")
        .writeStartArray("def")
          .writeStartObject().write("setId", 1)
             .writeStartArray("setDef")
                .writeStartObject().write("name", "ABC").write("type", "STRING")
                .writeEnd()
                .writeStartObject().write("name", "XYZ").write("type", "STRING")
                .writeEnd()
            .writeEnd()
          .writeEnd()
            .writeStartObject().write("setId", 2)
              .writeStartArray("setDef")
                .writeStartObject().write("name", "abc").write("type", "STRING")
                .writeEnd()
                .writeStartObject().write("name", "xyz").write("type", "STRING")
                .writeEnd()
              .writeEnd()
            .writeEnd()
          .writeEnd()
        .writeEnd();

    gen.close();

}

 }