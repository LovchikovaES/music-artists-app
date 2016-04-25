package ru.yandex.music.musicartists.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = Database.class)
public class ArtistsTable extends BaseModel {
    @PrimaryKey
    public Long id;

    @Column
    public String name;

    @Column
    public String genres;

    @Column
    public Integer tracks;

    @Column
    public Integer albums;

    @Column
    public String link;

    @Column
    public String description;

    @Column
    public String coverSmall;

    @Column
    public String coverBig;

}
