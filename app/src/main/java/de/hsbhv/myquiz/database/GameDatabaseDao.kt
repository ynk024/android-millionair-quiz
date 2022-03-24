package de.hsbhv.myquiz.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GameDatabaseDao {
    @Insert
    fun insert(game: Game)

    @Update
    fun update(game: Game)

    @Query("SELECT * FROM games_table ORDER BY cash DESC, timePlayedMilli ASC")
    fun getAllGames(): LiveData<List<Game>>

    @Query("SELECT * FROM games_table ORDER BY cash DESC, timePlayedMilli ASC LIMIT 1")
    fun getLeader(): LiveData<Game>

    @Query("SELECT * FROM games_table ORDER BY gameId LIMIT 1")
    fun getLastGame(): Game?

    @Query("DELETE FROM games_table")
    fun clear()
}


