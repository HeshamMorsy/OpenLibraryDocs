package com.task.openlibrarydocs.ui.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.task.openlibrarydocs.R
import kotlinx.android.synthetic.main.error_dialog_layout.*

class ErrorDialog(private val title: String,
                  private val message: String, private val btnText: String
) :
    DialogFragment() {

    private fun initView() {
        tv_title.text = title
        tv_message.text = message
        closeBtn.text = btnText
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity(), android.R.style.Theme_Translucent_NoTitleBar)
        val view = requireActivity().layoutInflater.inflate(R.layout.error_dialog_layout, null)

        val d = ColorDrawable(Color.BLACK)
        d.alpha = 130

        dialog.window!!.setBackgroundDrawable(d)
        dialog.window!!.setContentView(view)

        val params = dialog.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        params.gravity = Gravity.CENTER
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.error_dialog_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
                closeBtn.setOnClickListener {
                this.isCancelable = true
                dismiss()
        }

        initView()
    }
}
