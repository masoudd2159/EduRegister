package ir.masouddabbaghi.eduregister.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.masouddabbaghi.eduregister.data.model.Slider
import ir.masouddabbaghi.eduregister.databinding.ItemSliderBinding
import ir.masouddabbaghi.eduregister.utils.Tools
import javax.inject.Inject

class SliderAdapter
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : RecyclerView.Adapter<SliderAdapter.MyViewHolder>() {
        private var sliders = mutableListOf<Slider.Result>()
        private var slidersClickInterface: OnItemClickListener<Slider.Result>? = null

        fun updateItems(sliders: List<Slider.Result>) {
            this.sliders.clear()
            this.sliders.addAll(sliders)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): MyViewHolder = MyViewHolder(ItemSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemCount(): Int = sliders.size

        override fun onBindViewHolder(
            holder: MyViewHolder,
            position: Int,
        ) {
            holder.bind(slider = sliders[position])
        }

        fun setItemClickInterface(factorListClickInterface: OnItemClickListener<Slider.Result>?) {
            this.slidersClickInterface = factorListClickInterface
        }

        inner class MyViewHolder(
            private val binding: ItemSliderBinding,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(slider: Slider.Result) {
                with(binding) {
                    root.setOnClickListener { slidersClickInterface?.onItemClick(slider) }
                    Tools.displayImageWithGlide(
                        context = context,
                        imageView = slideImage,
                        imageSource = slider.image,
                        isImageDrawable = true,
                    )
                }
            }
        }
    }
