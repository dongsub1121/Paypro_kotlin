package kr.nicepayment.paypro.screen.merchant

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.nicepayment.paypro.databinding.ItemMerchantBinding
import kr.nicepayment.paypro.model.MerchantEntity

class MerchantAdapter : ListAdapter<MerchantEntity, MerchantAdapter.MerchantViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MerchantViewHolder {
        val binding = ItemMerchantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MerchantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MerchantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MerchantViewHolder(private val binding: ItemMerchantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(merchant: MerchantEntity) {
            binding.tvMerchantNo.text = merchant.merchantNo
            binding.tvMerchantName.text = merchant.sitename
            binding.tvMerchantBusinessNo.text = merchant.businessNo
            binding.tvMerchantAddress.text = merchant.siteaddress
            binding.executePendingBindings()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MerchantEntity>() {
        override fun areItemsTheSame(oldItem: MerchantEntity, newItem: MerchantEntity): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: MerchantEntity, newItem: MerchantEntity): Boolean {
            return oldItem.equals(other = newItem)
        }

    }
}