package com.wynneplaga.monsterhunterreference

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.squareup.picasso.Picasso
import com.wynneplaga.monsterhunterreference.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        item = intent.getParcelableExtra("item")!!

        binding.apply {
            // General
            itemTitle.text = item.name
                // load image
            Picasso.get()
                .load(item.assetImage)
                .placeholder(ResourcesCompat.getDrawable(resources, item.type.image, theme)!!)
                .into(itemImage)
            itemRank.text = item.rank.name.replaceFirstChar { it.uppercaseChar() }
            itemDefense.text = "Base: ${item.defense.base}%, augmented: ${item.defense.augmented}%, max: ${item.defense.max}%"
            itemType.text = item.type.name.replaceFirstChar { it.uppercaseChar() }

            // Skills
            val skillText = item.skills.joinToString(separator = "<br/>") { skill -> "<b>${skill.skillName}</b>(${skill.level})- ${skill.description}" }
            if (skillText.isEmpty()) {
                detailLayout.removeView(skillsCard)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    skills.text = Html.fromHtml(skillText, 0, null, null)
                } else {
                    skills.text = Html.fromHtml(skillText)
                }
            }

            // Crafting
            val craftingText = item.crafting.materials.map { Pair(it.quantity, it.item) }.joinToString(separator = "<br/>") { item -> "<b>${item.second.name}</b> x${item.first}- ${item.second.description}" }
            if (craftingText.isEmpty()) {
                detailLayout.removeView(craftingCard)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    crafting.text = Html.fromHtml(craftingText, 0, null, null)
                } else {
                    crafting.text = Html.fromHtml(craftingText)
                }
            }

            // Resistances
            fire.text = Resistances.formatResistanceAsString(item.resistances.fire)
            water.text = Resistances.formatResistanceAsString(item.resistances.water)
            ice.text = Resistances.formatResistanceAsString(item.resistances.ice)
            thunder.text = Resistances.formatResistanceAsString(item.resistances.thunder)
            dragon.text = Resistances.formatResistanceAsString(item.resistances.dragon)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}