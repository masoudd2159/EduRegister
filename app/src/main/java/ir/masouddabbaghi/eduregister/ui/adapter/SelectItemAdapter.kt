package ir.masouddabbaghi.eduregister.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.masouddabbaghi.eduregister.R
import ir.masouddabbaghi.eduregister.data.model.SelectItem
import ir.masouddabbaghi.eduregister.databinding.ItemSelectBinding
import javax.inject.Inject

class SelectItemAdapter
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : RecyclerView.Adapter<SelectItemAdapter.MyViewHolder>() {
        private var selectItems = mutableListOf<SelectItem>()
        private var categoryClickInterface: OnItemClickListener<SelectItem>? = null
        var index = -1

        fun updateItems(selectItems: List<SelectItem>) {
            this.selectItems.clear()
            this.selectItems.addAll(selectItems)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): MyViewHolder = MyViewHolder(ItemSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemCount(): Int = selectItems.size

        override fun onBindViewHolder(
            holder: MyViewHolder,
            position: Int,
        ) {
            holder.bind(selectItem = selectItems[position])
        }

        fun setItemClickInterface(categoryClickInterface: OnItemClickListener<SelectItem>?) {
            this.categoryClickInterface = categoryClickInterface
        }

        inner class MyViewHolder(
            private val binding: ItemSelectBinding,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(selectItem: SelectItem) {
                with(binding) {
                    item.text = selectItem.title

                    if (index == adapterPosition) {
                        content.background = ContextCompat.getDrawable(context, R.drawable.bg_selected_seller)
                        iconUnselected.visibility = View.INVISIBLE
                        animationSelected.visibility = View.VISIBLE
                        animationSelected.playAnimation()
                    } else {
                        content.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected_seller)
                        iconUnselected.visibility = View.VISIBLE
                        animationSelected.visibility = View.INVISIBLE
                    }

                    root.setOnClickListener {
                        index = adapterPosition
                        categoryClickInterface?.onItemClick(selectItem)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }
