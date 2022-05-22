package com.wynneplaga.monsterhunterreference

import android.os.Build
import android.os.Parcelable
import android.text.Html
import androidx.annotation.DrawableRes
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

enum class RANK {
    low,
    medium,
    high,
    master;

    val formattedName: String = name.replaceFirstChar { it.uppercaseChar() }

}

enum class TYPE(@DrawableRes val image: Int) {
    head(R.drawable.ic_head),
    chest(R.drawable.ic_chest),
    gloves(R.drawable.ic_gloves),
    waist(R.drawable.ic_waist),
    legs(R.drawable.ic_legs);

    val formattedName: String = name.replaceFirstChar { it.uppercaseChar() }

}

@Parcelize
data class DefenseInfo(val base: Int, val max: Int, val augmented: Int) : Parcelable

@Parcelize
data class Slot(val rank: Int): Comparable<Slot>, Parcelable {
    override fun compareTo(other: Slot) = rank.compareTo(other.rank)
}

@Parcelize
data class Skill(val skillName: String, val description: String, val level: Int): Parcelable

@Parcelize
data class Material(val name: String, val description: String): Parcelable

@Parcelize
data class MaterialQuantity(val quantity: Int, val item: Material): Parcelable

@Parcelize
data class Crafting(val materials: List<MaterialQuantity>): Parcelable

@Parcelize
data class Resistances(val fire: Int, val water: Int, val ice: Int, val thunder: Int, val dragon: Int): Parcelable {

    companion object {

        @JvmStatic
        fun formatResistanceAsString(value: Int): String {
            return if (value > 0)
                "+$value"
            else
                value.toString()
        }

    }

}

@Parcelize
data class Images(val imageMale: String?, val imageFemale: String?): Parcelable

@Parcelize
data class ItemModel(
    val id: Int,
    val name: String,
    val type: TYPE,
    val rank: RANK,
    val defense: DefenseInfo,
    val slots: List<Slot>,
    val skills: List<Skill>,
    val crafting: Crafting,
    val resistances: Resistances,
    val assets: Images?
    ): Comparable<ItemModel>, Parcelable {

    // Use the id to get a consistent but even gender split in the image we use
    val assetImage: String?
        get() = if (id % 2 == 0) (assets?.imageFemale ?: assets?.imageMale) else (assets?.imageMale ?: assets?.imageFemale)

    @IgnoredOnParcel
    val skillsText: CharSequence by lazy<CharSequence> {
        val skillText = skills.joinToString(separator = "<br/>") { skill -> "<b>${skill.skillName}</b>(${skill.level})- ${skill.description}" }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(skillText, 0, null, null)
        } else {
            Html.fromHtml(skillText)
        }
    }

    @IgnoredOnParcel
    val craftingText: CharSequence by lazy<CharSequence> {
        val craftingText = crafting.materials.map { Pair(it.quantity, it.item) }.joinToString(separator = "<br/>") { item -> "<b>${item.second.name}</b> x${item.first}- ${item.second.description}" }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(craftingText, 0, null, null)
        } else {
            Html.fromHtml(craftingText)
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other is ItemModel) {
            id == other.id
        } else {
            false
        }
    }

    override fun hashCode() = id.hashCode()

    override fun compareTo(other: ItemModel) = name.compareTo(other.name)

}