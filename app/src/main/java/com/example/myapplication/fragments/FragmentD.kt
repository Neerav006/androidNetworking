package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_d.*

class FragmentD :Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_d,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnFragmentD.setOnClickListener {
           val backStackCount:Int? = activity?.supportFragmentManager?.backStackEntryCount

            for (i in 0 until backStackCount!!){
                Log.e("fragment_tag",activity?.supportFragmentManager?.getBackStackEntryAt(i)?.name?:"no")
                activity?.supportFragmentManager?.popBackStack()
           }

          // activity?.supportFragmentManager?.popBackStack(FragmentB::class.simpleName,FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }
}