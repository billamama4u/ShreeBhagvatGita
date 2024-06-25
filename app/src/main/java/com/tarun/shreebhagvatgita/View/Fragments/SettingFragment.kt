package com.tarun.shreebhagvatgita.View.Fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.tarun.shreebhagvatgita.R
import com.tarun.shreebhagvatgita.databinding.FragmentSettingBinding



class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentSettingBinding.inflate(layoutInflater)


        binding.savedChapters.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_savedChapterFragment) }

        binding.savedVerses.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_savedVerseFragment)
        }
        statusbarcolour()
        return binding.root
    }

    private fun statusbarcolour(){
        activity?.window?.apply {
            val statusbarcolour = ContextCompat.getColor(requireContext(),R.color.white)
            statusBarColor= statusbarcolour
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }


}