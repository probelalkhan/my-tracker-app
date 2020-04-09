package net.simplifiedcoding.mytracker.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import net.simplifiedcoding.mytracker.R
import net.simplifiedcoding.mytracker.data.db.Survey
import net.simplifiedcoding.mytracker.databinding.RecyclerViewSurveyBinding

class SurveyAdapter : RecyclerView.Adapter<SurveyAdapter.SurveyViewHolder>() {

    private var surveys: List<Survey>? = null

    var listener: RecyclerViewClickListener<Survey>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SurveyViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_view_survey,
            parent,
            false
        )
    )

    override fun getItemCount() = if (surveys.isNullOrEmpty()) 0 else surveys!!.size

    override fun onBindViewHolder(holder: SurveyViewHolder, position: Int) {
        holder.binding.textViewName.text = surveys!![position].name
        holder.binding.root.setOnClickListener {
            listener?.onRecyclerViewClick(it, surveys!![position])
        }
    }

    fun setSurveys(surveys: List<Survey>?) {
        this.surveys = surveys
        notifyDataSetChanged()
    }

    inner class SurveyViewHolder(val binding: RecyclerViewSurveyBinding) :
        RecyclerView.ViewHolder(binding.root)
}