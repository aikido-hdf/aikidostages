package stages.aikidoliguehdf.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
abstract interface StagesDao {



    /*stages*/
    @Query("SELECT * FROM Stages ORDER BY startdate ASC")
    abstract fun viewAllStages(): Flow<List<Stages>>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Insert Single Course
    abstract fun insert(stages : Stages ): Long

    @Query("DELETE FROM Stages")
    abstract suspend fun deleteAll()

    @Query("SELECT * FROM Stages ORDER BY startdate ASC")
    abstract fun getAll(): List<Stages>

    @Query("SELECT * FROM Stages WHERE startdate > :xmonthsold ORDER BY startdate ASC")
    abstract fun getAllxMonths(xmonthsold: String): List<Stages>

    @Query("SELECT * FROM Stages WHERE startdate > :now ORDER BY startdate ASC")
    abstract fun getNext(now: String): List<Stages>


    @Query("SELECT * FROM Stages WHERE idcategory LIKE :idcat ORDER BY startdate ASC")
    abstract fun getAllbyCat(idcat: String): List<Stages>

    @Query("SELECT * FROM Stages WHERE idcategory LIKE '%' || :idcat || '%'")
    abstract fun getByCat(idcat: String): List<Stages>

    @Query("SELECT * FROM Stages WHERE places LIKE :idplace ORDER BY startdate ASC")
    abstract fun getAllbyPlace(idplace: String): List<Stages>

    @Query("SELECT * FROM Stages WHERE idstages = :idstages")
    abstract fun loadAllByIds(idstages: String): List<Stages>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(vararg stages: Stages)

    @Delete
    abstract fun delete(stage: Stages)



    //
    //Categories
    //

    @Query("SELECT * FROM Categories")
    abstract fun viewAllCategories(): Flow<List<Categories>>


    @Insert(onConflict = OnConflictStrategy.IGNORE) // Insert single Category
    abstract fun insertCategory(category: Categories): Long


    @Query("DELETE FROM Categories")
    abstract suspend fun deleteAllCategories()

    @Query("SELECT * FROM Categories")
    abstract fun getAllCategories(): List<Categories>

    @Query("SELECT * FROM Categories WHERE idcat IN (:userIds)")
    abstract fun loadAllByIds(userIds: IntArray): List<Categories>

    @Query("SELECT idcat FROM Categories")
    abstract fun listIdCat(): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertAll(vararg categories: Categories)

    @Delete
    abstract fun delete(categories: Categories)

    @Insert(onConflict = OnConflictStrategy.IGNORE) // Insert single Category
    abstract fun insert(categories : Categories ): Long

    //
    //Places
    //

    @Query("SELECT * FROM Places")
    fun viewAllPlaces(): Flow<List<Places>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaces(places : Places )

    @Query("DELETE FROM Places")
    suspend fun deleteAllPlaces()

    @Query("SELECT * FROM Places")
    fun getAllPlaces(): List<Places>

    @Query("SELECT nameplace FROM Places WHERE idplace = :idplace")
    fun loadNamePlaceById(idplace: String): String

    @Query("SELECT address FROM Places WHERE idplace = :idplace")
    fun loadAddressPlaceById(idplace: String): String

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllPlaces(vararg places: Places)

    @Delete
    fun delete(places: Places)


    //
    //ManytoMany Categories -> Stages
    //

    @Insert(onConflict = OnConflictStrategy.IGNORE) // Insert Single CourseCategoryMap
    abstract fun insertStagesCatMap(StagesCatMap: StagesCatMap): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertAllStagesCatMap(StagesCatMap: List<StagesCatMap>): List<Long>

    @Query("SELECT namecat FROM Categories INNER JOIN StagesCatMap ON  Categories.idcat=StagesCatMap.idcatmap WHERE StagesCatMap.idstagesmap = :idstages")
    fun getCatName(idstages: String): List<String>

    //
    //Favorites
    //

    @Insert
    fun insertFav(favorites : Favorites )

    @Query("DELETE FROM Favorites")
    suspend fun deleteAllFav()

    @Query("DELETE FROM Favorites WHERE idstagesfav = :id")
    fun deleteFavbyId(id: String)

    @Query("SELECT * FROM Favorites")
    fun getAllFavs(): List<Favorites>

    @Query("SELECT idstagesfav FROM Favorites WHERE idstagesfav = :idstage")
    fun getFavsbyStageId(idstage: String): String

    @Delete
    fun delete(favorites: Favorites)

}