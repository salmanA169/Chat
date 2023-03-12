package com.swalif.sa.datasource.local.dao

import androidx.room.*
import com.swalif.sa.datasource.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("SELECT * FROM userEntity WHERE uidUser= :uid ")
    suspend fun getUserByUid(uid: String): UserEntity?

    @Query("SELECT * FROM userentity")
    fun getUsersFlow(): Flow<List<UserEntity>>


}