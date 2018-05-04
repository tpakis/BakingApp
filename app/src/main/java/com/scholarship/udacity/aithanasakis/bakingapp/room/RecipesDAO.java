package com.scholarship.udacity.aithanasakis.bakingapp.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scholarship.udacity.aithanasakis.bakingapp.model.Recipe;

import java.util.List;

/**
 * Created by 3piCerberus on 24/04/2018.
 */
@Dao
public interface RecipesDAO {
    //it will return -1 if the item was already in db and not inserted
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertEntry(Recipe entry);

    @Query("SELECT * FROM Recipe WHERE id = :id")
    Recipe getSpecifigEntryById(int id);

    @Query("SELECT * FROM Recipe WHERE forWidget = 1")
    Recipe getEntryForWidget();


    @Update(onConflict = OnConflictStrategy.IGNORE)
    int update(Recipe entry);

    // Removes an entry from the database
    @Delete
    void delete(Recipe entry);
    // Gets all entries in the database
    @Query("SELECT * FROM Recipe")
    List<Recipe> getAllEntries();

    /*@Query("delete from RecipeEntries where nasaId = :id")
    void deleteById(String id);



    // Gets all entries in the database
    @Query("SELECT * FROM RecipeEntries where description LIKE :queryString")
    List<Recipe> getEntriesContaining(String queryString);

    @Query("SELECT COUNT(*) from RecipeEntries")
    int countEntries();*/

}
