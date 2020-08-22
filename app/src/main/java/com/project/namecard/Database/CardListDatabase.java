package com.project.namecard.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.project.namecard.Dao.CardListDao;
import com.project.namecard.models.CardListModel;

@Database(entities = {CardListModel.class}, version = 1)
public abstract class CardListDatabase extends RoomDatabase {
    public abstract CardListDao getCardListDao();
}
