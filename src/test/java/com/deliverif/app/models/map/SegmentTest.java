package com.deliverif.app.models.map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SegmentTest {

    @Test
    public void testConstructor() {
        String name = "Rue Jean Renoir";
        Float lenght = (float) 179.80255;
        Intersection origin = new Intersection("25336175", (float) 4.863642, (float) 45.75301);
        Intersection destination = new Intersection("25336178", (float) 4.8658633, (float) 45.752552);
        Segment segment = new Segment(name, lenght, origin, destination);
        assertEquals(segment.getName(),name);
        assertEquals(segment.getLength(), lenght);
        assertEquals(segment.getOrigin(), origin);
        assertEquals(segment.getDestination(),destination);
    }

    //TODO : Constructeur - Qu'est ce qui se passe si en paramètre lenght on passe un nombre négatif ???
    //TODO : Constructeur - Qu'est ce qui se passe si en paramètre longitude on passe un nombre négatif ??
    //TODO : Constructeur - Qu'est ce qui se passe si en paramètre latitude on passe un nombre négatif ???
    //TODO : Méthode create - à tester
}
