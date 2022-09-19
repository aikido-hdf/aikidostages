package stages.aikidoliguehdf.data

import androidx.room.*


@Entity(tableName = "Stages")
data class Stages(
    @PrimaryKey @ColumnInfo(name = "idstages") var idstages: Long? = null,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "startdate") var startdate: String,
    @ColumnInfo(name = "media") var featured_media: String,
    @ColumnInfo(name = "idcategory") var mec_category: String,
    @ColumnInfo(name = "cost") var cost: String,
    @ColumnInfo(name = "starthours") var starthours: String,
    @ColumnInfo(name = "endhours") var endhours: String,
    @ColumnInfo(name = "startminutes") var startminutes: String,
    @ColumnInfo(name = "endminutes") var endminutes: String,
    @ColumnInfo(name = "startampm") var startampm: String,
    @ColumnInfo(name = "endampm") var endampm: String,
    @ColumnInfo(name = "places") var places: String,
    @ColumnInfo(name = "excerpt") var excerpt: String,
    @ColumnInfo(name = "link") var link: String,
    @ColumnInfo(name = "content") var content: String,
)

@Entity(tableName = "Categories")
data class Categories(
    @PrimaryKey @ColumnInfo(name = "idcat") var idcat: String,
    @ColumnInfo(name = "namecat") var name: String,
    @ColumnInfo(name = "countcat") var count : String,

    )

@Entity(tableName = "Places")
data class Places(
    @PrimaryKey @ColumnInfo(name = "idplace") var idplace: String,
    @ColumnInfo(name = "nameplace") var name: String,
    @ColumnInfo(name = "countplace") var count : String,
    @ColumnInfo(name = "address") var address : String,

    )

@Entity(primaryKeys = ["idstagesmap", "idcatmap"])
data class StagesCatMap(
    var idstagesmap: String,
    @ColumnInfo(index = true)
    var idcatmap: String,
)

@Entity(tableName = "Favorites")
data class Favorites(
    @PrimaryKey (autoGenerate = true) @ColumnInfo(name = "idfav") var idfav: Int? = null,
    @ColumnInfo(name = "idstagesfav") var idstagesfav: String
)

@Entity(primaryKeys = ["idstagesmap", "idfavmap"])
data class StagesFavMap(
    var idstagesmap: String,
    @ColumnInfo(index = true)
    var idfavmap: String,
)

