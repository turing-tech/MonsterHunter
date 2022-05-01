package com.wynneplaga.monsterhunterreference

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.turingtechnologies.materialscrollbar.INameableAdapter
import com.wynneplaga.monsterhunterreference.databinding.LayoutDecorationBinding
import com.wynneplaga.monsterhunterreference.databinding.LayoutItemBinding

class ItemListAdapter: ListAdapter<ItemModel, ItemListAdapter.ItemListHolder>(DiffCallback), INameableAdapter {

    private object DiffCallback: DiffUtil.ItemCallback<ItemModel>() {
        override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel) = oldItem == newItem
        override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false)
        return ItemListHolder(view)
    }

    override fun onBindViewHolder(holder: ItemListHolder, position: Int) {
        val item = currentList[position]
        holder.binding.apply {
            itemName.text = item.name
            itemRank.text = item.rank.name.replaceFirstChar { it.uppercaseChar() }
            baseDefense.text = "${item.defense.base}%"
            decorationHolder.removeAllViews()
            item.slots.sortedDescending().map { it.rank.toString() }.forEach { rank ->
                val root = LayoutInflater.from(holder.itemView.context).inflate(R.layout.layout_decoration, decorationHolder, false)
                LayoutDecorationBinding.bind(root.findViewById(R.id.decorationLayout)).decorationRank.text = rank
                decorationHolder.addView(root)
            }
            itemTypeImage.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.resources, item.type.image, holder.itemView.context.theme))
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("item", item)
            holder.itemView.context.startActivity(intent)
        }
    }

    class ItemListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = LayoutItemBinding.bind(itemView)

    }

    // INameableAdapter
    override fun getCharacterForElement(element: Int) = currentList[element].name[0]

}