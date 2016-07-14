package com.epicodus.chatapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.epicodus.chatapp.DividerItemDecoration;
import com.epicodus.chatapp.MessageHolder;
import com.epicodus.chatapp.R;
import com.epicodus.chatapp.models.Message;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    final ArrayList<String> messages = new ArrayList<>();

    @Bind(R.id.newMessageButton) Button mNewMessageButton;
    @Bind(R.id.messageEditText) EditText mMessageEditText;

    private FirebaseRecyclerAdapter mAdapter;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        RecyclerView recycler = (RecyclerView) findViewById(R.id.messages_recycler);

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recycler.addItemDecoration(itemDecoration);

        mAdapter = new FirebaseRecyclerAdapter<Message, MessageHolder>(Message.class, android.R.layout.two_line_list_item, MessageHolder.class, ref) {
            @Override
            public void populateViewHolder(MessageHolder messageViewHolder, Message message, int position) {
                messageViewHolder.setText(message.getMessage());
                messageViewHolder.setName(message.getUser());
            }
        };
        recycler.setAdapter(mAdapter);

        mNewMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mMessageEditText.getText().toString();
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                String email = currentUser.getEmail();
                ref.push().setValue(new Message(message, email));
                mMessageEditText.setText("");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
