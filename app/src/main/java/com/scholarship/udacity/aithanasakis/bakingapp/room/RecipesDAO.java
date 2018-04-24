package com.scholarship.udacity.aithanasakis.bakingapp.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.scholarship.udacity.aithanasakis.bakingapp.model.Recipe;

import java.util.List;

/**
 * Created by 3piCerberus on 24/04/2018.
 */
@Dao
public interface RecipesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEntry(Recipe entry);

    // Removes an entry from the database
    @Delete
    void delete(Recipe entry);

    /*@Query("delete from RecipeEntries where nasaId = :id")
    void deleteById(String id);

    // Gets all entries in the database
    @Query("SELECT * FROM RecipeEntries")
    List<Recipe> getAllEntries();

    // Gets all entries in the database
    @Query("SELECT * FROM RecipeEntries where description LIKE :queryString")
    List<Recipe> getEntriesContaining(String queryString);

    @Query("SELECT COUNT(*) from RecipeEntries")
    int countEntries();*/

}
