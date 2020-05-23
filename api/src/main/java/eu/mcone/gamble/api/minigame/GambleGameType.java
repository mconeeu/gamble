package eu.mcone.gamble.api.minigame;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

/**
 * Datei erstellt von: Felix Schmid in Projekt: mcone-gamble
 */
public enum GambleGameType {

    TEST(new File("./gamble-games/gamble-test-game.jar"), "Testspiel", "Schmiddinger"),
    TRAFFIC_RACE(new File("./gamble-games/gamble-trafficrace.jar"), "Ampelrennen", "Schmiddinger");

    @Getter
    private File jarFile;

    @Getter
    private String displayName, author;

    @Getter
    @Setter
    private boolean active;

    GambleGameType(File jarFile, String displayName, String author) {
        this.jarFile = jarFile;
        this.displayName = displayName;
        this.author = author;
        if (jarFile.exists()) {
            active = true;
        }
    }

}
