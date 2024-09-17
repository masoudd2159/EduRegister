package ir.masouddabbaghi.eduregister.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import ir.hamsaa.persiandatepicker.date.PersianDateImpl
import ir.masouddabbaghi.eduregister.R
import ir.masouddabbaghi.eduregister.data.model.DepartmentList
import ir.masouddabbaghi.eduregister.data.model.SelectItem
import ir.masouddabbaghi.eduregister.databinding.ActivityRegisterStudentBinding
import ir.masouddabbaghi.eduregister.databinding.DialogSelectBinding
import ir.masouddabbaghi.eduregister.network.NetworkResult
import ir.masouddabbaghi.eduregister.ui.adapter.SelectItemAdapter
import ir.masouddabbaghi.eduregister.ui.base.BaseActivity
import ir.masouddabbaghi.eduregister.ui.viewmodel.StudentViewModel
import ir.masouddabbaghi.eduregister.utils.Tools.formatJalaliDate
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterStudentActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterStudentBinding
    private lateinit var picker: PersianDatePickerDialog
    private lateinit var dialog: Dialog

    private val studentViewModel: StudentViewModel by viewModels()

    private var gender: String = ""
    private var major: String = ""
    private var birthdate: String = ""

    private enum class SelectItemType {
        GENDER,
        MAJOR,
    }

    @Inject
    lateinit var selectItemAdapter: SelectItemAdapter

    override fun getLayoutResourceBinding(): View {
        binding = ActivityRegisterStudentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            iconBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

            buttonStudentList.setOnClickListener {
                startActivity(Intent(this@RegisterStudentActivity, StudentListActivity::class.java))
            }

            layoutInputGender.setOnClickListener {
                showSelectItemDialog(
                    selectItem = mutableListOf(SelectItem("Male", "مرد"), SelectItem("Female", "زن")),
                    selectItemType = SelectItemType.GENDER,
                )
            }

            layoutInputMajor.setOnClickListener {
                studentViewModel.fetchDepartmentList()
            }

            layoutRegisterStudent.setOnClickListener {
                when {
                    inputName.text.toString().isEmpty() ->
                        Toast
                            .makeText(
                                this@RegisterStudentActivity,
                                resources.getString(R.string.error_student_firstname),
                                Toast.LENGTH_LONG,
                            ).show()

                    inputLastname.text.toString().isEmpty() ->
                        Toast
                            .makeText(
                                this@RegisterStudentActivity,
                                resources.getString(R.string.error_student_lastname),
                                Toast.LENGTH_LONG,
                            ).show()

                    birthdate.isEmpty() ->
                        Toast
                            .makeText(
                                this@RegisterStudentActivity,
                                resources.getString(R.string.error_student_brith_date),
                                Toast.LENGTH_LONG,
                            ).show()

                    gender.isEmpty() ->
                        Toast
                            .makeText(
                                this@RegisterStudentActivity,
                                resources.getString(R.string.error_student_gender),
                                Toast.LENGTH_LONG,
                            ).show()

                    major.isEmpty() ->
                        Toast
                            .makeText(
                                this@RegisterStudentActivity,
                                resources.getString(R.string.error_student_major),
                                Toast.LENGTH_LONG,
                            ).show()

                    else ->
                        studentViewModel.fetchRegisterStudent(
                            firstName = inputName.text.toString(),
                            middleName = "",
                            lastName = inputLastname.text.toString(),
                            gender = gender,
                            birthDate = birthdate,
                            major = major,
                            departmentId = 1,
                        )
                }
            }

            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        studentViewModel.departmentListResponse.collect { departmentListResult ->
                            when (departmentListResult) {
                                is NetworkResult.Loading ->
                                    Log.i(
                                        javaClass.simpleName,
                                        "Department List NetworkResult Loading Status -> ${departmentListResult.isLoading}",
                                    )

                                is NetworkResult.Failure ->
                                    Log.e(
                                        javaClass.simpleName,
                                        "Department List NetworkResult Failure Message -> ${departmentListResult.errorMessage}",
                                    )

                                is NetworkResult.Success ->
                                    showSelectItemDialog(
                                        convertDepartmentListToItems(departmentListResult.data),
                                        SelectItemType.MAJOR,
                                    )
                            }
                        }
                    }

                    launch {
                        studentViewModel.registerStudentResponse.collect { registerStudentResult ->
                            when (registerStudentResult) {
                                is NetworkResult.Loading ->
                                    Log.i(
                                        javaClass.simpleName,
                                        "Register Student NetworkResult Loading Status -> ${registerStudentResult.isLoading}",
                                    )

                                is NetworkResult.Failure ->
                                    Log.e(
                                        javaClass.simpleName,
                                        "Register Student NetworkResult Failure Message -> ${registerStudentResult.errorMessage}",
                                    )

                                is NetworkResult.Success -> {
                                    Log.e(
                                        javaClass.simpleName,
                                        "Register Student NetworkResult Failure Message -> ${registerStudentResult.data}",
                                    )

                                    this@RegisterStudentActivity.finish()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun showCalendar(view: View?) {
        val typeface = Typeface.createFromAsset(assets, "fonts/yekan_bakh_regular.ttf")
        picker =
            PersianDatePickerDialog(
                this,
            ).setPositiveButtonString(
                "باشه",
            ).setNegativeButton(
                "بیخیال",
            ).setTodayButton(
                "امروز",
            ).setTodayButtonVisible(
                true,
            ).setMinYear(
                1300,
            ).setAllButtonsTextSize(12)
                .setMaxYear(1450)
                .setInitDate(1376, 8, 1)
                .setActionTextColor(Color.GRAY)
                .setTypeFace(typeface) //                .setShowDayPicker(false)
                .setTitleType(PersianDatePickerDialog.DAY_MONTH_YEAR)
                .setShowInBottomSheet(true)
                .setListener(
                    object : PersianPickerListener {
                        @SuppressLint("SetTextI18n")
                        override fun onDateSelected(persianPickerDate: PersianPickerDate) {
                            Log.i(tagLog, "onDateSelected: " + persianPickerDate.timestamp) // 675930448000
                            Log.i(tagLog, "onDateSelected: " + persianPickerDate.gregorianDate) // Mon Jun 03 10:57:28 GMT+04:30 1991
                            Log.i(tagLog, "onDateSelected: " + persianPickerDate.persianLongDate) // دوشنبه  13  خرداد  1370
                            Log.i(tagLog, "onDateSelected: " + persianPickerDate.persianMonthName) // خرداد
                            Log.i(tagLog, "onDateSelected: " + PersianDateImpl.isLeapYear(persianPickerDate.persianYear)) // true
                            val textBirthDate = "${persianPickerDate.persianYear}/${persianPickerDate.persianMonth}/${persianPickerDate.persianDay}"
                            binding.inputBirthdate.text = textBirthDate
                            birthdate = formatJalaliDate(textBirthDate)
                        }

                        override fun onDismissed() {}
                    },
                )
        picker.show()
    }

    private fun showSelectItemDialog(
        selectItem: List<SelectItem>,
        selectItemType: SelectItemType,
    ) {
        dialog = Dialog(this@RegisterStudentActivity, R.style.Theme_Dialog)
        val dialogBinding = DialogSelectBinding.inflate(layoutInflater)

        dialog.window?.also {
            (it.decorView as ViewGroup)
                .getChildAt(
                    0,
                ).startAnimation(AnimationUtils.loadAnimation(this@RegisterStudentActivity, R.anim.slide_up))
            val layoutParams = it.attributes
            layoutParams.dimAmount = 0.7f
            layoutParams.gravity = Gravity.BOTTOM
            it.setGravity(Gravity.BOTTOM)
            it.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.windowAnimations = R.style.DialogAnimation
        }
        dialog.setContentView(dialogBinding.root)
        dialog.setCanceledOnTouchOutside(true)

        dialogBinding.recyclerViewCategories.setHasFixedSize(true)
        dialogBinding.recyclerViewCategories.layoutManager =
            LinearLayoutManager(this@RegisterStudentActivity, LinearLayoutManager.VERTICAL, false)
        dialogBinding.recyclerViewCategories.adapter = selectItemAdapter
        selectItemAdapter.setItemClickInterface { item ->

            when (selectItemType) {
                SelectItemType.GENDER -> {
                    binding.inputGender.text = item.title
                    gender = item.id
                }

                SelectItemType.MAJOR -> {
                    binding.inputMajor.text = item.title
                    major = item.id
                }
            }
        }
        selectItemAdapter.updateItems(selectItem)

        dialogBinding.layoutSubmit.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun convertDepartmentListToItems(departmentList: List<DepartmentList.DepartmentListItem>): List<SelectItem> =
        departmentList.map { category ->
            SelectItem(id = category.name, title = category.name)
        }
}
