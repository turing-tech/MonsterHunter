package com.wynneplaga.monsterhunterreference

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wynneplaga.materialScrollBar2.inidicators.AlphabeticIndicator
import com.wynneplaga.monsterhunterreference.databinding.LayoutDecorationBinding
import com.wynneplaga.monsterhunterreference.databinding.LayoutItemBinding

class ItemListAdapter: ListAdapter<ItemModel, ItemListAdapter.ItemListHolder>(DiffCallback), AlphabeticIndicator.INameableAdapter {

    private object DiffCallback: DiffUtil.ItemCallback<ItemModel>() {
        override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel) = oldItem == newItem
        override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListHolder {
        val view = LayoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemListHolder(view)
    }

    override fun onBindViewHolder(holder: ItemListHolder, position: Int) {
        val item = currentList[position]
        holder.binding.itemModel = item
        holder.binding.apply {
            decorationHolder.removeAllViews()
            item.slots.sortedDescending().map { it.rank.toString() }.forEach { rank ->
                val root = LayoutInflater.from(holder.itemView.context).inflate(R.layout.layout_decoration, decorationHolder, false)
                LayoutDecorationBinding.bind(root.findViewById(R.id.decorationLayout)).decorationRank.text = rank
                decorationHolder.addView(root)
            }
        }
    }

    inner class ItemListHolder(val binding: LayoutItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("item", currentList[adapterPosition])
                itemView.context.startActivity(intent)
            }
        }

    }

    // INameableAdapter
    override fun getCharacterForElement(element: Int) = currentList[element].name[0]

}