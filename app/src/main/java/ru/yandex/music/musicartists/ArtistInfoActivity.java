package ru.yandex.music.musicartists;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import ru.yandex.music.musicartists.database.ArtistsTable;
import ru.yandex.music.musicartists.database.ArtistsTable_Table;
import ru.yandex.music.musicartists.utilities.Constants;
import ru.yandex.music.musicartists.utilities.Utils;

public class ArtistInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_info);
        // ID выбранного артиста из списка
        Bundle intentExtras = getIntent().getExtras();
        Long idArtist = intentExtras.getLong("id");

        // Инфо по артисту
        ArtistsTable artistSelected = SQLite.select().from(ArtistsTable.class).where(ArtistsTable_Table.id.eq(idArtist)).querySingle();

        SmartImageView image = (SmartImageView) this.findViewById(R.id.image);
        TextView genres = (TextView) this.findViewById(R.id.genres);
        TextView albumsAndSongs = (TextView) this.findViewById(R.id.albums_songs);
        TextView bioDescription = (TextView) this.findViewById(R.id.bio_desc);

        genres.setText(artistSelected.genres);
        String albumsSongsDesc = "";
        albumsSongsDesc = Utils.getQuantityDescription(this, artistSelected.albums, Constants.QuantityType.album);
        albumsSongsDesc = albumsSongsDesc + "    •    " + Utils.getQuantityDescription(this, artistSelected.tracks, Constants.QuantityType.track);
        albumsAndSongs.setText(albumsSongsDesc);

        String description = artistSelected.description;
        String firstSymbol = description.substring(0, 1);
        String newFirstSymbol = firstSymbol.toUpperCase();
        description = newFirstSymbol + description.substring(1, description.length());
        bioDescription.setText(description);

        image.setImageUrl(artistSelected.coverBig);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(artistSelected.name);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // По кнопке домой попадаем в предыдущий экран, без перезагрузки активности
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
