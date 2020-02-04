package com.example.a24friend.ui.survey


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.a24friend.R
import com.example.a24friend.database.getDatabase
import com.example.a24friend.databinding.FragmentSurveyBinding
import com.example.a24friend.ui.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class SurveyFragment : Fragment() {
    private lateinit var activity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity = getActivity() as MainActivity

        val binding: FragmentSurveyBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_survey, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = getDatabase(requireContext()).userDao
        val viewModelFactory = SurveyViewModelFactory(dataSource, activity.mUserId, application)
        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(SurveyViewModel::class.java)

        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        viewModel.error.observe(this, Observer {e ->
            if (e) {
                Toast.makeText(context, "Failed to connect the server.", Toast.LENGTH_LONG)
            }
        })
        viewModel.userId.observe(this, Observer { userId ->
            if (userId!!.isNotEmpty()) {
                viewModel.setUser()
            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }


}
