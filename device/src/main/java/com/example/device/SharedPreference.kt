package com.example.device

import android.content.Context
import com.example.device.dataSource.SharedDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreference @Inject constructor(@ApplicationContext private val context: Context) :
    SharedDataSource {
    private val preference = context.getSharedPreferences("dataKey", Context.MODE_PRIVATE)
    override fun saveData(key: String, data: String): Boolean {
        return try {
            preference.edit().putString(key, data).apply()
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun deleteData(key: String, data: String): Boolean {
        return try {
            preference.edit().remove(data).apply()
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun getData(key: String): String? {
        return preference.getString(key, "")
    }
}