package com.nivekaa.icepurpykt.infra.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nivekaa.icepurpykt.R
import com.nivekaa.icepurpykt.infra.activity.MainActivity

/**
 * A simple [Fragment] subclass.
 * Use the [PaymentResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaymentResultFragment     // Required empty public constructor
    (private val resourceId: Int) : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(resourceId, container, false)
        view.findViewById<View>(R.id.backshopping).setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    context, MainActivity::class.java
                )
            )
        }
        return view
    }

    companion object {
        fun newInstance(res: Int): PaymentResultFragment {
            val fragment = PaymentResultFragment(res)
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}