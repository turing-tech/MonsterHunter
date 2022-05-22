package com.wynneplaga.monsterhunterreference

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


object BindingAdapters {

    @JvmStatic
    @BindingAdapter("items")
    fun <T> setRecyclerViewItems(view: RecyclerView, items: List<T>) {
        (view.adapter as ListAdapter<T, *>).submitList(items)
    }

    @JvmStatic
    @BindingAdapter(value = ["picassoSrc", "picassoPlaceholder"], requireAll = false)
    fun setImageWithPicasso(imageView: ImageView, srcUrl: String?, @DrawableRes placeholderRes: Int?) {
        if (srcUrl == null) {
            placeholderRes?.let(imageView::setImageResource)
            return
        }
        val picassoCall = Picasso.get().load(srcUrl)
        placeholderRes?.let(picassoCall::placeholder)
        picassoCall.into(imageView)
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageUri(view: ImageView, imageUri: String?) {
        if (imageUri == null) {
            view.setImageURI(null)
        } else {
            view.setImageURI(Uri.parse(imageUri))
        }
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageUri(view: ImageView, imageUri: Uri?) {
        view.setImageURI(imageUri)
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageDrawable(view: ImageView, drawable: Drawable?) {
        view.setImageDrawable(drawable)
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

}