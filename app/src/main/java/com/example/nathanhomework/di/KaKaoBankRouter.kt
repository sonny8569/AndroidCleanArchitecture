package com.example.nathanhomework.di

import android.content.Context
import android.content.Intent
import com.example.detail.DetailActivity
import com.example.domain.model.Router
import com.example.domain.model.SearchResult
import com.example.domain.model.Serializar
import java.io.Serializable

class KaKaoBankRouter(private val context : Context) : Router {
    override fun navigateToDetail(data: SearchResult, isLike: Boolean) {
        context.startActivity(
            Intent(context , DetailActivity::class.java)
                .putExtra(
                    Router.KEY_SEARCH_RESULT,
                    Serializar.json.encodeToString(SearchResult.serializer() , data)
                ).putExtra(Router.KEY_IS_LIKE, isLike)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}