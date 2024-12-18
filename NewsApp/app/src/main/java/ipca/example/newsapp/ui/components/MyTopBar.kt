package ipca.example.newsapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import ipca.example.newsapp.AppDatabase
import ipca.example.newsapp.models.Article
import ipca.example.newsapp.models.ArticleCache
import ipca.example.newsapp.ui.theme.NewsAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(title : String,
             isBaseScreen : Boolean = true,
             article: Article? = null
) {

    val context = LocalContext.current

    TopAppBar(
        title = {
            if (article != null) {
                Text(text = article.title?:"",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
            }else{
                Text(text = title)
            }
        },
        actions = {
            if (!isBaseScreen) {

                IconButton(
                    onClick = {
                        article?.let{
                            val articleCache = ArticleCache(
                                it.url!!,
                                it.toJsonString()
                            )
                            GlobalScope.launch(Dispatchers.IO) {
                                AppDatabase
                                    .getDatabase(context)
                                    ?.articleCacheDao()
                                    ?.insert(articleCache)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = "Bookmark"
                    )
                }
            }
        }

    )
}

@Preview(showBackground = true)
@Composable
fun MyTopBarPreview(){
    NewsAppTheme {
        MyTopBar(title = "Test")
    }
}
