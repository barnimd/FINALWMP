package com.example.enroll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
    private List<Subject> subjectList;
    private Context context;
    private OnSubjectEnrollListener enrollListener;

    private int totalCredits = 0; // Menyimpan jumlah kredit yang dipilih
    private static final int MAX_CREDITS = 24; // Maksimal 24 kredit

    public interface OnSubjectEnrollListener {
        void onEnrollUpdated(int totalCredits);
    }

    public SubjectAdapter(Context context, List<Subject> subjectList, OnSubjectEnrollListener listener) {
        this.context = context;
        this.subjectList = subjectList;
        this.enrollListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subject subject = subjectList.get(position);
        holder.subjectName.setText(subject.getName());
        holder.subjectCredit.setText(subject.getCredit() + " Credits");

        holder.enrollButton.setOnClickListener(v -> {
            if (totalCredits + subject.getCredit() <= MAX_CREDITS) {
                totalCredits += subject.getCredit();
                Toast.makeText(context, subject.getName() + " enrolled!", Toast.LENGTH_SHORT).show();
                enrollListener.onEnrollUpdated(totalCredits); // Kirim total kredit ke UserPage
            } else {
                Toast.makeText(context, "Cannot exceed 24 credits!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName, subjectCredit;
        Button enrollButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subject_name);
            subjectCredit = itemView.findViewById(R.id.subject_credit);
            enrollButton = itemView.findViewById(R.id.enroll_button);
        }
    }
}
