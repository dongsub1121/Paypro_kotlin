package kr.nicepayment.paypro

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import java.text.NumberFormat
import java.util.Locale

class PaymentHistoryAdapter(private val context: Context) : RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder>() {
    private var items: List<NicePaymentsResult> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<NicePaymentsResult>) {
        this.items = items
        notifyDataSetChanged() // This will trigger the ListView to refresh
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_item_history_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            textViewServiceName.text = item.pay
            textViewTransactionDate.text = item.authDate
            textViewAccountNumber.text = item.authNum
            textViewTransactionAmount.text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(item.amount)}원"
            textViewStatus.text = getStatusText(item.status)
            imageViewStatus.setImageResource(getStatusDrawable(item.status))
            imageViewPaymentMethod.setImageResource(getPaymentMethodDrawable(item.method))
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemCount(): Int {
        return items.size
    }

    private fun getStatusText(status: PaymentStatus?): String {
        return when (status) {
            PaymentStatus.READY -> "대기"
            PaymentStatus.AUTHORIZATION -> "결제"
            PaymentStatus.REFUNDED -> "환불"
            else -> "??"
        }
    }

    private fun getStatusDrawable(status: PaymentStatus?): Int {
        // Replace with your actual drawables
        return when (status) {
            PaymentStatus.READY -> R.drawable.ready
            PaymentStatus.AUTHORIZATION -> R.drawable.done
            PaymentStatus.REFUNDED -> R.drawable.done
            // Handle other cases...
            else -> R.drawable.status_error
        }
    }

    // Helper method to get the correct drawable based on the payment method
    private fun getPaymentMethodDrawable(method: PaymentMethod?): Int {
        // Replace with your actual drawables
        return when (method) {
            PaymentMethod.ALIPAY -> R.drawable.alipay
            PaymentMethod.WECHAT -> R.drawable.wechat_ci
            PaymentMethod.LINE -> R.drawable.line_ci
            // Handle other cases...
            else -> R.drawable.paypro_ci
        }
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textViewServiceName: TextView = view.findViewById(R.id.textViewServiceName)
        val textViewTransactionDate: TextView = view.findViewById(R.id.textViewTransactionDate)
        val textViewAccountNumber: TextView = view.findViewById(R.id.textViewAccountNumber)
        val textViewTransactionAmount: TextView = view.findViewById(R.id.textViewTransactionAmount)
        val textViewStatus: TextView = view.findViewById(R.id.textViewStatus)
        val imageViewStatus: ImageView = view.findViewById(R.id.imageViewStatus)
        val imageViewPaymentMethod: ImageView = view.findViewById(R.id.imageViewPaymentMethod)
    }
}