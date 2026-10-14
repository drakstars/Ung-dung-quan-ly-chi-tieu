package com.example.quanlychitieu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Expense expense, int position);
    }

    private Context context;
    private List<Expense> expenseList;
    private OnItemClickListener listener;

    public ExpenseAdapter(Context context, List<Expense> expenseList, OnItemClickListener listener) {
        this.context = context;
        this.expenseList = expenseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.tvExpenseName.setText(expense.getName());
        holder.tvExpenseAmount.setText(expense.getFormattedAmount());
        holder.tvExpenseDate.setText(expense.getDate());
        holder.tvExpenseCategory.setText(expense.getCategory());

        // Định kiểu màu sắc pill badge cho từng danh mục
        styleCategoryBadge(holder.tvExpenseCategory, expense.getCategory());

        // Xử lý sự kiện nhấn vào item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPos = holder.getAdapterPosition();
                if (listener != null && adapterPos != RecyclerView.NO_POSITION) {
                    listener.onItemClick(expenseList.get(adapterPos), adapterPos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList != null ? expenseList.size() : 0;
    }

    private void styleCategoryBadge(TextView tvCategory, String category) {
        int bgColor;
        int textColor;

        if (category == null) {
            category = "Khác";
        }

        switch (category) {
            case "Ăn uống":
                bgColor = ContextCompat.getColor(context, R.color.tag_food_bg);
                textColor = ContextCompat.getColor(context, R.color.tag_food_text);
                break;
            case "Đi lại":
                bgColor = ContextCompat.getColor(context, R.color.tag_transport_bg);
                textColor = ContextCompat.getColor(context, R.color.tag_transport_text);
                break;
            case "Học tập":
                bgColor = ContextCompat.getColor(context, R.color.tag_study_bg);
                textColor = ContextCompat.getColor(context, R.color.tag_study_text);
                break;
            case "Giải trí":
                bgColor = ContextCompat.getColor(context, R.color.tag_entertain_bg);
                textColor = ContextCompat.getColor(context, R.color.tag_entertain_text);
                break;
            default:
                bgColor = ContextCompat.getColor(context, R.color.tag_other_bg);
                textColor = ContextCompat.getColor(context, R.color.tag_other_text);
                break;
        }

        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(30f);
        shape.setColor(bgColor);

        tvCategory.setBackground(shape);
        tvCategory.setTextColor(textColor);
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvExpenseName;
        TextView tvExpenseAmount;
        TextView tvExpenseCategory;
        TextView tvExpenseDate;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExpenseName = itemView.findViewById(R.id.tvExpenseName);
            tvExpenseAmount = itemView.findViewById(R.id.tvExpenseAmount);
            tvExpenseCategory = itemView.findViewById(R.id.tvExpenseCategory);
            tvExpenseDate = itemView.findViewById(R.id.tvExpenseDate);
        }
    }
}
