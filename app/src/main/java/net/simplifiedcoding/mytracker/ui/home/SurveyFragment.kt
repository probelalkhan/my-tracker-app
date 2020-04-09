package net.simplifiedcoding.mytracker.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import net.simplifiedcoding.mytracker.R
import net.simplifiedcoding.mytracker.data.db.Coordinate
import net.simplifiedcoding.mytracker.databinding.FragmentSurveyBinding
import net.simplifiedcoding.mytracker.ui.BaseFragment
import kotlin.math.abs
import kotlin.math.sin


/**
 * A simple [Fragment] subclass.
 */
class SurveyFragment : BaseFragment<FragmentSurveyBinding, HomeViewModel>() {


    companion object {
        const val KEY_SURVEY_ID = "key_survey_id"
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            val surveyID = it.getInt(KEY_SURVEY_ID, -1)
            if (surveyID != -1) {
                displaySurveyArea(surveyID)
            }
        }
    }

    private fun displaySurveyArea(surveyID: Int) {
        viewModel.getCoordinates(surveyID).observe(viewLifecycleOwner, Observer {
            val area = calculateArea(it)
            binding.textViewArea.text = getString(R.string.meter_square, area)
        })
    }

    private fun calculateArea(coordinates: List<Coordinate>): Double {
        var area = 0.0
        if (coordinates.size > 2) {
            for (i in 0 until coordinates.size - 1) {
                val p1 = coordinates[i]
                val p2 = coordinates[i + 1]
                area += convertToRadian(p2.lat - p1.lng) * (2 + sin(convertToRadian(p1.lat)) + sin(
                    convertToRadian(p2.lat)
                ))
            }
            area = area * 6378137 * 6378137 / 2
        }
        return abs(area)
    }

    private fun convertToRadian(input: Double): Double {
        return input * Math.PI / 180
    }

    override fun getFragmentView() = R.layout.fragment_survey

    override fun getViewModel() = HomeViewModel::class.java

}
