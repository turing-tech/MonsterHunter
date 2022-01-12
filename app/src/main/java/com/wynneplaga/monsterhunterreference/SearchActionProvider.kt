package com.wynneplaga.monsterhunterreference

import android.content.Context
import android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
import android.text.InputType.TYPE_TEXT_VARIATION_FILTER
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ActionProvider

class SearchActionProvider(context: Context) : ActionProvider(context) {

    override fun onCreateActionView() = SearchView(context, null, androidx.appcompat.R.style.Widget_AppCompat_SearchView).apply {
        val textColor = ResourcesCompat.getColor(context.resources, R.color.white, context.theme)
        findViewById<EditText>(androidx.appcompat.R.id.search_src_text).apply {
            setTextColor(textColor)
            inputType = TYPE_TEXT_FLAG_NO_SUGGESTIONS or TYPE_TEXT_VARIATION_FILTER
        }
    }

}