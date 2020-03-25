package com.michs.github_repo_search.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.michs.github_repo_search.network.Result
import com.michs.github_repo_search.network.ResultsResponse

fun hideSoftKeyboard(context: Context?, view: View?){
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun <T> MutableLiveData<Result<ResultsResponse<T>>>.notifyObserver(result: Result<ResultsResponse<T>>) = postValue(result)


@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?){
    if (!url.isNullOrEmpty())
        Glide.with(imageView.context).load(url).into(imageView)
}

@BindingAdapter("repoDescription")
fun TextView.truncateText(description: String?){
    if (!description.isNullOrEmpty())
        text = description.smartTruncate(50)
}


private val PUNCTUATION = listOf(", ", "; ", ": ", " ")

fun String.smartTruncate(length: Int): String {
    val words = split(" ")
    var added = 0
    var hasMoreWords = false
    val builder = StringBuilder()
    for (word in words) {
        if (builder.length > length) {
            hasMoreWords = true
            break
        }
        builder.append(word)
        builder.append(" ")
        added += 1
    }
    PUNCTUATION.map {
        if (builder.endsWith(it)) {
            builder.replace(builder.length - it.length, builder.length, "")
        }
    }
    if (hasMoreWords) {
        builder.append("...")
    }
    return builder.toString()
}