package kr.koizi.koiziapp.test

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.databinding.FragmentSupportBinding
import kr.koizi.koiziapp.main.MainActivity.Companion.nickName

class SupportFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSupportBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSupportBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val safety = arguments?.getInt("safety")
        var supportTile = ""
        var support1 = ""
        var support2 = ""
        var support3 = ""

        when (safety) {
            1 -> {
                supportTile = getString(R.string.text_support_title_25)
                support1 = getString(R.string.text_support1_25)
                support2 = getString(R.string.text_support2_25)
                support3 = getString(R.string.text_support3_25)
            }
            2 -> {
                supportTile = getString(R.string.text_support_title_50)
                support1 = getString(R.string.text_support1_50)
                support2 = getString(R.string.text_support2_50)
                support3 = getString(R.string.text_support3_50)
            }
            3 -> {
                supportTile = getString(R.string.text_support_title_75)
                support1 = getString(R.string.text_support1_75)
                support2 = getString(R.string.text_support2_75)
                support3 = getString(R.string.text_support3_75)
            }
            4 -> {
                supportTile = getString(R.string.text_support_title_100)
                support1 = getString(R.string.text_support1_100)
                support2 = getString(R.string.text_support2_100)
                support3 = getString(R.string.text_support3_100)
        }
    }
        binding.tvSupportTitle.text = supportTile
        binding.tvSupport1.text = support1
        binding.tvSupport2.text = support2
        binding.tvSupport3.text = support3
        binding.tvTitle.text = "${nickName}님의 안전도는"
        binding.btnSupportClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setContentView(R.layout.fragment_support)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }
}