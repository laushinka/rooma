package com.rooma.scraper;

public class BerlinDistricts {
    public static String[] getBerlinDistricts() {
        return new String[]{"Mitte", "Moabit", "Hansaviertel", "Tiergarten", "Wedding", "Gesundbrunen",
                "Friedrichshain", "Kreuzberg",
                "Prenzlauer Berg", "prenzlauerberg", "Prenzlauer", "Weißensee", "Blankenburg", "Heinersdorf", "Karow", "Stadtrandsiedlung", "Pankow", "Blankenfelde", "Buch", "Französisch Buchholz", "Niederschönhausen", "Rosenthal", "Wilhelmsruh",
                "Charlottenburg", "Wilmersdorf", "Schmargendorf", "Grunewald", "Charlottenburg-Nord", "Halensee",
                "Spandau", "Haselhorst", "Siemensstadt", "Staaken", "Gatow", "Kladow", "Hakenfelde", "Falkenhagener Feid", "Wilhelmstadt",
                "Steglitz", "Lichterfelde", "Lankwitz", "Zehlendorf", "Dahlem", "Nikolassee", "Wannsee",
                "Schöneberg", "Friedenau", "Tempelhof", "Mariendorf", "Marienfelde", "Lichtenrade",
                "Neukölln", "Neukoelln", "Britz", "Buckow", "Rudow", "Gropiusstadt",
                "Alt-Treptow", "Plänterwald", "Baumschulenweg", "Johannisthal", "Niederschöneweide", "Altglienicke", "Adlershof", "Bohnsdorf", "Oberschöneweide", "Köpenick", "Friedrichshagen", "Rahnsdorf", "Grünau", "Müggelheim", "Schmöckwitz",
                "Marzahn", "Biesdorf", "Kaulsdorf", "Mahlsdorf", "Hellesdorf",
                "Friedrichsfelde", "Karlshorst", "Lichtenberg","Falkenberg", "Malchow", "Wartenberg", "Neu-Hohenschönhausen", "Alt-Hohenschönhausen", "Fennpfuhl", "Rummelsburg",
                "Reinickendorf", "Tegel", "Konradshöhe", "Heiligensee", "Frohnau", "Hermsdorf", "Waidmannslust", "Lübars", "Wittenau", "Märkisches Viertel", "Borsigwalde"};
    }

    static String convertCommonMisspelling(String foundDistrict) {
        switch (foundDistrict.toLowerCase()) {
            case "prenzlauer":
            case "prenzlauerberg":
                return "Prenzlauer Berg";
            case "neukoelln":
                return "Neukölln";
            default:
                return foundDistrict;
        }
    }
}
