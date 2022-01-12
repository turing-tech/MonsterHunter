package com.wynneplaga.monsterhunterreference

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.turingtechnologies.materialscrollbar.INameableAdapter
import com.wynneplaga.monsterhunterreference.databinding.LayoutDecorationBinding
import com.wynneplaga.monsterhunterreference.databinding.LayoutItemBinding

class ItemListAdapter: RecyclerView.Adapter<ItemListAdapter.ItemListHolder>(), INameableAdapter {

    private val diffCallback = object: DiffUtil.ItemCallback<ItemModel>() {
        override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel) = oldItem == newItem
        override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel) = oldItem == newItem
    }

    /**
     * Used to quickly update the contents automatically and efficiently
     */
    private val differUtils: AsyncListDiffer<ItemModel> = AsyncListDiffer(this, diffCallback)

    /**
     * Propose list to the adapter, which will update its contents accordingly
     */
    fun submitList(itemList: List<ItemModel>) = differUtils.submitList(itemList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false)
        return ItemListHolder(view)
    }

    override fun onBindViewHolder(holder: ItemListHolder, position: Int) {
        val item = differUtils.currentList[position]
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

    override fun getItemCount() = differUtils.currentList.size

    class ItemListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = LayoutItemBinding.bind(itemView)

    }

    // INameableAdapter
    override fun getCharacterForElement(element: Int) = differUtils.currentList[element].name[0]

}