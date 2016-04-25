package ru.yandex.music.musicartists.utilities;

import java.util.Locale;

public interface Constants {
    String jsonURL = "http://cache-default03e.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/artists.json";

    // Для согласования числительного с существительным
    // Тип числительного
    enum QuantityNumberType {
        one, two_four, others
    }

    // Тип существительного
    enum QuantityType {
        track, album
    }

    String russianLang = "ru";
}
