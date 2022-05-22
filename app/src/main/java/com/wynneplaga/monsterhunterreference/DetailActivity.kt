package com.wynneplaga.monsterhunterreference

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wynneplaga.monsterhunterreference.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityDetailBinding.inflate(layoutInflater).apply {
            itemModel = intent.getParcelableExtra("item")!!
            setContentView(root)

            setSupportActionBar(topAppBar)
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}