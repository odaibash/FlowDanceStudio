package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;
import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {

    private List<Session> sessionList;
    private Context context;

    public SessionAdapter(Context context, List<Session> sessionList) {
        this.context = context;
        this.sessionList = sessionList;
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_item, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        Session session = sessionList.get(position);
        holder.bind(session);
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    public class SessionViewHolder extends RecyclerView.ViewHolder {

        TextView sessionName, sessionDate, sessionTime, sessionCoach;
        Button registerButton;

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            sessionName = itemView.findViewById(R.id.session_name);
            sessionDate = itemView.findViewById(R.id.session_date);
            sessionTime = itemView.findViewById(R.id.session_time);
            sessionCoach = itemView.findViewById(R.id.session_coach);
            registerButton = itemView.findViewById(R.id.register_button);
        }

        void bind(final Session session) {
            sessionName.setText(session.getName());
            sessionDate.setText(session.getDate().toString());
            sessionTime.setText(session.getStartTime() + " - " + session.getEndTime());
            sessionCoach.setText(session.getCoach());
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toggle the register button state and text
                    if (registerButton.getText().toString().equals("Register")) {
                        registerButton.setText("Registered");
                        registerButton.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                        // Perform registration logic here
                        Toast.makeText(context, "Registered for " + session.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}