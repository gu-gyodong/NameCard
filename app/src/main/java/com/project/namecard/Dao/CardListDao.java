package com.project.namecard.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.project.namecard.models.CardListModel;

import java.util.List;

@Dao
public interface CardListDao {

    @Insert
    void insert(CardListModel cardListModel);

    @Update
    void update(CardListModel cardListModel);

    @Delete
    void delete(CardListModel cardListModel);

    @Query("SELECT * FROM CardListModel")
    List<CardListModel> getAll();
}
