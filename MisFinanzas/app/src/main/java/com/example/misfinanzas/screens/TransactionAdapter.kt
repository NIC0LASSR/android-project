package com.example.misfinanzas.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.misfinanzas.R
import com.example.misfinanzas.models.FinancialTransaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    private var transactions: List<FinancialTransaction> = emptyList()

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFecha: TextView = itemView.findViewById(R.id.tvTransactionDate)
        val tvCategoria: TextView = itemView.findViewById(R.id.tvCategoria)
        val tvMonto: TextView = itemView.findViewById(R.id.tvTransactionAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        val context = holder.itemView.context

        // Formatear fecha
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.tvFecha.text = dateFormat.format(Date(transaction.date))

        // Mostrar categoría
        holder.tvCategoria.text = transaction.category

        // Formatear y mostrar monto con color según tipo
        val amount = abs(transaction.amount)
        val formattedAmount = String.format("$%,.2f", amount)
        holder.tvMonto.apply {
            text = formattedAmount
            setTextColor(
                context.getColor(
                    if (transaction.amount >= 0) R.color.verde else R.color.rojo
                )
            )
        }
    }

    override fun getItemCount() = transactions.size

    fun updateTransactions(newTransactions: List<FinancialTransaction>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }
}