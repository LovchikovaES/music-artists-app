package ru.yandex.music.musicartists.utilities;

import android.content.Context;

import java.util.Locale;

import ru.yandex.music.musicartists.R;

public class Utils {

    // Согласование существительного с числительным
    public static String getQuantityDescription(Context context, Integer quantity, Constants.QuantityType quantityType) {
        String quantityDescription;
        String description = "";
        Constants.QuantityNumberType quantityNumberType;
        String currentLang = Locale.getDefault().getLanguage();

        char lastSymbol = quantity.toString().charAt(quantity.toString().length() - 1);
        char beforeLastSymbol = ' ';
        if (quantity.toString().length() > 1) {
            beforeLastSymbol = quantity.toString().charAt(quantity.toString().length() - 2);
        }

        switch (currentLang) {
            case Constants.russianLang:
                if( (lastSymbol == '1') && (beforeLastSymbol != '1') ) {
                    quantityNumberType = Constants.QuantityNumberType.one;
                }
                else if ( (lastSymbol == '2' || lastSymbol == '3' || lastSymbol == '4') && beforeLastSymbol != '1') {
                    quantityNumberType = Constants.QuantityNumberType.two_four;
                }
                else {
                    quantityNumberType = Constants.QuantityNumberType.others;
                }
                break;
            default:
                if( quantity == 1 ) {
                    quantityNumberType = Constants.QuantityNumberType.one;
                }
                else {
                    quantityNumberType = Constants.QuantityNumberType.others;
                }
        }

        switch (quantityType) {
            case track:
                switch (quantityNumberType) {
                    case one:
                        description = context.getResources().getString(R.string.song);
                        break;
                    case two_four:
                        description = context.getResources().getString(R.string.songs_between_2_and_4);
                        break;
                    case others:
                        description = context.getResources().getString(R.string.songs);
                        break;
                }
                break;
            case album:
                switch (quantityNumberType) {
                    case one:
                        description = context.getResources().getString(R.string.album);
                        break;
                    case two_four:
                        description = context.getResources().getString(R.string.albums_between_2_and_4);
                        break;
                    case others:
                        description = context.getResources().getString(R.string.albums);
                        break;
                }
                break;
        }
        quantityDescription = quantity.toString() + " " + description;
        return quantityDescription;
    }

}
