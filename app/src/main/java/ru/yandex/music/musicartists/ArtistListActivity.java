package ru.yandex.music.musicartists;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import ru.yandex.music.musicartists.database.ArtistsTable;
import ru.yandex.music.musicartists.database.ArtistsTable_Table;
import ru.yandex.music.musicartists.utilities.Constants;

public class ArtistListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_artist_list);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setEmptyView(findViewById(R.id.emptyList));

        Long countArtists = SQLite.select().from(ArtistsTable.class).count();
        if (countArtists == 0) {
            if (isConnected()) {
                // Записей в БД нет и есть интернет, загружаем из json
                addArtistsToDB();
            }
        }
        else {
            // Загружаем записи из БД
            addArtistsFromDB();
        }
    }

    private void addArtistsToDB() {

        RequestParams params = new RequestParams();

        AsyncHttpClient httpClient = new AsyncHttpClient();

            httpClient.get(Constants.jsonURL, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    parseJSON(new String(responseBody));
                    addArtistsFromDB();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.error_load, Toast.LENGTH_SHORT);
                    toast.show();
                    Log.e(ArtistListActivity.class.toString(), responseBody.toString());
                }
            });

    }

    private void parseJSON(String responseJSON) {
        // Парсим json и добавляем данные в БД
        Delete.table(ArtistsTable.class);
        try {
            JSONArray jsonArray = new JSONArray(responseJSON);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonArtist = jsonArray.getJSONObject(i);
                    ArtistsTable newArtist = new ArtistsTable();
                    newArtist.id = jsonArtist.getLong("id");
                    newArtist.name = jsonArtist.getString("name");
                    if (jsonArtist.has("genres")) {
                        String genres = "";
                        JSONArray genresArray = jsonArtist.getJSONArray("genres");
                        for (int j=0; j < genresArray.length(); j++) {
                            if (genres.equals("")) {
                                genres = genresArray.getString(j);
                            }
                            else {
                                genres = genres + ", " + genresArray.getString(j);
                            }
                        }
                        newArtist.genres = genres;
                    }
                    if (jsonArtist.has("tracks")) {
                        newArtist.tracks = jsonArtist.getInt("tracks");
                    }
                    if (jsonArtist.has("albums")) {
                        newArtist.albums = jsonArtist.getInt("albums");
                    }
                    if (jsonArtist.has("link")) {
                        newArtist.link = jsonArtist.getString("link");
                    }
                    if (jsonArtist.has("description")) {
                        newArtist.description = jsonArtist.getString("description");
                    }
                    if (jsonArtist.has("cover")) {
                        JSONObject jsonCover = jsonArtist.getJSONObject("cover");
                        if (jsonCover.has("small")) {
                            newArtist.coverSmall = jsonCover.getString("small");
                        }
                        if (jsonCover.has("big")) {
                            newArtist.coverBig = jsonCover.getString("big");
                        }
                    }
                    newArtist.save();
                }
        }
        catch (JSONException ignored) {
        }

    }

    private void addArtistsFromDB() {
        Long countArtists = SQLite.select().from(ArtistsTable.class).count();
        List<ArtistsTable> listArtists = SQLite.select().from(ArtistsTable.class).orderBy(ArtistsTable_Table.name, true).queryList();

        final ArtistListAdapter adapter = new ArtistListAdapter(this, listArtists);
        ListView listViewArtists = (ListView)findViewById(R.id.list);
        listViewArtists.setAdapter(adapter);

        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ArtistInfoActivity.class);
                intent.putExtra("id", adapter.getItem(position).id);
                startActivity(intent);
            }
        });
    }

    private boolean isConnected() {
        boolean isConnected = false;
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            isConnected = activeNetwork.isConnectedOrConnecting();
        }
        return isConnected;
    }


    public void btnRefreshOnClick(View view) {
        if (isConnected()) {
            addArtistsToDB();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                if (isConnected()) {
                    addArtistsToDB();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
        return true;
    }
}

