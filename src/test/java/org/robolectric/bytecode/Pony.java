package org.robolectric.bytecode;

import org.robolectric.internal.Implementation;
import org.robolectric.internal.Implements;
import org.robolectric.internal.Instrument;

@Instrument
public class Pony {
    public Pony() {
    }

    public Pony(String abc) {
        System.out.println("abc = " + abc);
    }

    public String ride(String where) {
        return "Whinny! You're on my " + where + "!";
    }

    public static String prance(String where) {
        return "I'm prancing to " + where + "!";
    }

    public String saunter(String where) {
        return "Off I saunter to " + where + "!";
    }

    @Implements(Pony.class)
    public static class ShadowPony {
        @Implementation
        public String ride(String where) {
            return "Fake whinny! You're on my " + where + "!";
        }

        @Implementation
        public static String prance(String where) {
            return "I'm shadily prancing to " + where + "!";
        }
    }
}
