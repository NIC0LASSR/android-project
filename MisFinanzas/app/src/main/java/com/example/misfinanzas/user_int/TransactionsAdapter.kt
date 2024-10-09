package com.example.misfinanzas.user_int
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.misfinanzas.R
import com.example.misfinanzas.models.Transaction



class TransactionsAdapter : RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {

    private var transactions: List<Transaction> = emptyList()

    fun setTransactions(transactions: List<Transaction>) {
        this.transactions = transactions
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.bind(transaction)
    }

    override fun getItemCount(): Int = transactions.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCategory: TextView = itemView.findViewById(R.id.spinnerCategoria)
        private val tvAmount: TextView = itemView.findViewById(R.id.editMonto)
        private val tvDate: TextView = itemView.findViewById(R.id.editFecha)
        private val tvType: TextView = itemView.findViewById(R.id.radioGroupTipo)

        fun bind(transaction: Transaction) {
            tvCategory.text = transaction.category
            tvAmount.text = "$${transaction.amount}"
            tvDate.text = transaction.date.toString() // Convertir a formato legible si es necesario
            tvType.text = if (transaction.type == "Ingreso") "Ingreso" else "Gasto"
        }
    }
}