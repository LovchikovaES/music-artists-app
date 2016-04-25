package ru.yandex.music.musicartists;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import java.util.List;

import ru.yandex.music.musicartists.database.ArtistsTable;
import ru.yandex.music.musicartists.utilities.Constants;
import ru.yandex.music.musicartists.utilities.Utils;


public class ArtistListAdapter extends ArrayAdapter<ArtistsTable>{

    private final Activity activity;
    private final List<ArtistsTable> artistsTableList;


    public ArtistListAdapter(Activity activity, List<ArtistsTable> artistsTableList) {
        super(activity, R.layout.layout_artist_list, artistsTableList);
        this.activity = activity;
        this.artistsTableList = artistsTableList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Заполняем строку ListView
        LayoutInflater inflater= activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.layout_artist_list, null, true);

        TextView name = (TextView) rowView.findViewById(R.id.name);
        SmartImageView image = (SmartImageView) rowView.findViewById(R.id.image);
        TextView genres = (TextView) rowView.findViewById(R.id.genres);
        TextView albumsAndSongs = (TextView) rowView.findViewById(R.id.albums_songs);

        name.setText(artistsTableList.get(position).name);
        genres.setText(artistsTableList.get(position).genres);

        String albumsSongsDesc =
                Utils.getQuantityDescription(getContext(), artistsTableList.get(position).albums, Constants.QuantityType.album);
        albumsSongsDesc = albumsSongsDesc + ", " +
                Utils.getQuantityDescription(getContext(), artistsTableList.get(position).tracks, Constants.QuantityType.track);
        albumsAndSongs.setText(albumsSongsDesc);
        image.setImageResource(R.drawable.no_photo);
        image.setImageUrl(artistsTableList.get(position).coverSmall);

        return rowView;
    }
}
