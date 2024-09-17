package ir.masouddabbaghi.eduregister.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.masouddabbaghi.eduregister.data.model.HomeList
import ir.masouddabbaghi.eduregister.databinding.ItemHomeListBinding
import ir.masouddabbaghi.eduregister.utils.Tools.displayImageWithGlide
import javax.inject.Inject

class HomeListAdapter
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : RecyclerView.Adapter<HomeListAdapter.MyViewHolder>() {
        private var homeList = mutableListOf<HomeList.Result>()
        private var homeListClickInterface: OnItemClickListener<HomeList.Result>? = null

        fun updateItems(homeList: List<HomeList.Result>) {
            this.homeList.clear()
            this.homeList.addAll(homeList)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): MyViewHolder = MyViewHolder(ItemHomeListBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemCount(): Int = homeList.size

        override fun onBindViewHolder(
            holder: MyViewHolder,
            position: Int,
        ) {
            holder.bind(homeListItem = homeList[position])
        }

        fun setItemClickInterface(homeListClickInterface: OnItemClickListener<HomeList.Result>?) {
            this.homeListClickInterface = homeListClickInterface
        }

        inner class MyViewHolder(
            private val binding: ItemHomeListBinding,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(homeListItem: HomeList.Result) {
                with(binding) {
                    title.text = homeListItem.title
                    displayImageWithGlide(context = context, imageView = icon, imageSource = homeListItem.icon, isImageDrawable = true)
                    icon.setTint(Color.parseColor(homeListItem.color))

                    root.setOnClickListener {
                        homeListClickInterface?.onItemClick(homeListItem)
                    }
                }
            }
        }
    }
