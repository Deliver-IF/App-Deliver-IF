package com.deliverif.app.models.map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntersectionTest {
    @Test
    public void testConstructor() {
        String id = "25336175";
        Float longitude = (float) 4.863642;
        Float latitude = (float) 45.75301;
        Intersection intersection = new Intersection(id, longitude, latitude);
        assertEquals(intersection.getId(),id);
        assertEquals(intersection.getLongitude(), longitude);
        assertEquals(intersection.getLatitude(), latitude);
    }

    //TODO : Constructeur - Qu'est ce qui se passe si en paramètre longitude on passe un nombre négatif ??
    //TODO : Constructeur - Qu'est ce qui se passe si en paramètre latitude on passe un nombre négatif ???
    //TODO : Méthode create - à tester
}
