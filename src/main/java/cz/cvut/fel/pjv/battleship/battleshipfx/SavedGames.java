package cz.cvut.fel.pjv.battleship.battleshipfx;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;


public class SavedGames {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void saveJson(GameData data, String fileName) {
        try {
            objectMapper.writeValue(new File(fileName), data);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public GameData loadJson(String fileName) {
        try {
            return objectMapper.readValue(new File(fileName), GameData.class);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

}
