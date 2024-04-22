package kr.nicepayment.paypro.screen.view

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kr.nicepayment.paypro.R

import kr.nicepayment.paypro.databinding.FragmentCalendarBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DateRecyclerViewAdapter(
) : RecyclerView.Adapter<DateRecyclerViewAdapter.DateViewHolder>() {

    private val dates: List<Date> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false) as TextView
        return DateViewHolder(textView)
    }

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.textView.text = dateFormat.format(dates[position])
    }

    override fun getItemCount() = dates.size


    class DateViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView) {

    }
}