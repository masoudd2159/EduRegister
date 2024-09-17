package ir.masouddabbaghi.eduregister.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.masouddabbaghi.eduregister.R
import ir.masouddabbaghi.eduregister.data.model.StudentList
import ir.masouddabbaghi.eduregister.databinding.ItemStudentBinding
import ir.masouddabbaghi.eduregister.utils.Tools.displayImageWithGlide
import ir.masouddabbaghi.eduregister.utils.Tools.formatBirthdayDate
import javax.inject.Inject

class StudentAdapter
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : RecyclerView.Adapter<StudentAdapter.MyViewHolder>() {
        private var studentList = mutableListOf<StudentList.StudentListItem>()
        private var studentListClickInterface: OnItemClickListener<StudentList.StudentListItem>? = null

        fun updateItems(studentList: List<StudentList.StudentListItem>) {
            this.studentList.clear()
            this.studentList.addAll(studentList)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): MyViewHolder = MyViewHolder(ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemCount(): Int = studentList.size

        override fun onBindViewHolder(
            holder: MyViewHolder,
            position: Int,
        ) {
            holder.bind(student = studentList[position])
        }

        fun setItemClickInterface(studentListClickInterface: OnItemClickListener<StudentList.StudentListItem>?) {
            this.studentListClickInterface = studentListClickInterface
        }

        inner class MyViewHolder(
            private val binding: ItemStudentBinding,
        ) : RecyclerView.ViewHolder(binding.root) {
            @SuppressLint("SetTextI18n")
            fun bind(student: StudentList.StudentListItem) {
                with(binding) {
                    inputFullName.text = "${student.firstName} ${student.middleName} ${student.lastName}"
                    inputMajor.text = String.format(context.resources.getString(R.string.major_placeholder), student.major)
                    inputBirthdate.text =
                        String.format(context.resources.getString(R.string.birthdate_placeholder), formatBirthdayDate(student.birthDate))
                    when (student.gender.lowercase()) {
                        "male" -> {
                            displayImageWithGlide(
                                context = context,
                                imageView = iconGender,
                                imageSource = "graduate_male",
                                isImageDrawable = true,
                            )
                            iconGender.setTint(Color.parseColor("#EB6434"))
                        }

                        "female" -> {
                            displayImageWithGlide(
                                context = context,
                                imageView = iconGender,
                                imageSource = "graduate_female",
                                isImageDrawable = true,
                            )
                            iconGender.setTint(Color.parseColor("#33B1FF"))
                        }
                    }
                }
            }
        }
    }
