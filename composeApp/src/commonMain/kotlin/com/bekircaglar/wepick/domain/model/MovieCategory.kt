package com.bekircaglar.wepick.domain.model

import com.bekircaglar.wepick.presentation.screens.innercategory.CategoryItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.jetbrains.compose.resources.StringResource
import wepick.composeapp.generated.resources.Res
import wepick.composeapp.generated.resources.category_action
import wepick.composeapp.generated.resources.category_adventure
import wepick.composeapp.generated.resources.category_animation
import wepick.composeapp.generated.resources.category_biography
import wepick.composeapp.generated.resources.category_comedy
import wepick.composeapp.generated.resources.category_crime
import wepick.composeapp.generated.resources.category_documentary
import wepick.composeapp.generated.resources.category_drama
import wepick.composeapp.generated.resources.category_family
import wepick.composeapp.generated.resources.category_fantasy
import wepick.composeapp.generated.resources.category_film_noir
import wepick.composeapp.generated.resources.category_history
import wepick.composeapp.generated.resources.category_horror
import wepick.composeapp.generated.resources.category_music
import wepick.composeapp.generated.resources.category_musical
import wepick.composeapp.generated.resources.category_mystery
import wepick.composeapp.generated.resources.category_romance
import wepick.composeapp.generated.resources.category_sci_fi
import wepick.composeapp.generated.resources.category_sport
import wepick.composeapp.generated.resources.category_thriller
import wepick.composeapp.generated.resources.category_war
import wepick.composeapp.generated.resources.category_western

@Serializable
enum class MovieCategory(
    override val titleRes: StringResource,
    override val id: String
) : CategoryItem {
    ACTION(Res.string.category_action, "action"),
    COMEDY(Res.string.category_comedy, "comedy"),
    DRAMA(Res.string.category_drama, "drama"),
    CRIME(Res.string.category_crime, "crime"),
    MUSIC(Res.string.category_music, "music"),
    SPORT(Res.string.category_sport, "sport"),
    FAMILY(Res.string.category_family, "family"),
    HORROR(Res.string.category_horror, "horror"),
    FANTASY(Res.string.category_fantasy, "fantasy"),
    HISTORY(Res.string.category_history, "history"),
    ANIMATION(Res.string.category_animation, "animation"),
    ROMANCE(Res.string.category_romance, "romance"),
    THRILLER(Res.string.category_thriller, "thriller"),
    MYSTERY(Res.string.category_mystery, "mystery"),
    ADVENTURE(Res.string.category_adventure, "adventure"),
    WAR(Res.string.category_war, "war"),
    MUSICAL(Res.string.category_musical, "musical"),
    BIOGRAPHY(Res.string.category_biography, "biography"),
    DOCUMENTARY(Res.string.category_documentary, "documentary"),
    SCI_FI(Res.string.category_sci_fi, "sci_fi"),
}

fun movieCategoryFromId(name: String): MovieCategory? {
    return MovieCategory.entries.find { it.name.equals(name, ignoreCase = true) }
}

@Serializable
data class MovieDB(
    val title: String,
    val imdbId: String,
    val categories: List<String>
)