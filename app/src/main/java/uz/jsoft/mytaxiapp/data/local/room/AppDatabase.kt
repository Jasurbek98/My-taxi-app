package uz.jsoft.mytaxiapp.data.local.room

import androidx.room.Room
import androidx.room.RoomDatabase
import uz.jsoft.mytaxiapp.app.App

//@Database(entities = [ContactData::class], version = 1)
//@TypeConverters(CustomTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
//    abstract fun contactsDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    App.instance,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}